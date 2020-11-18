package com.monitor.web.alarm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.monitor.web.alarm.dto.AlarmDTO;
import com.monitor.web.alarm.dto.AlarmSchedulerRelationDTO;
import com.monitor.web.alarm.entity.AlarmSchedulerRelationEntity;
import com.monitor.web.alarm.entity.SubscriberEntity;
import com.monitor.web.alarm.scheduler.AlarmScheduler;
import com.monitor.web.alarm.vo.AlarmVO;
import com.monitor.web.auth.service.TokenService;
import com.monitor.web.common.api.PageResultBase;
import com.monitor.web.project.entity.ProjectEntity;
import com.monitor.web.project.service.ProjectService;
import com.monitor.web.schedule.dto.SchedulerDTO;
import com.monitor.web.schedule.entity.SchedulerEntity;
import com.monitor.web.schedule.service.SchedulerService;
import com.monitor.web.utils.DataConvertUtils;
import com.monitor.web.alarm.dao.AlarmDAO;
import com.monitor.web.alarm.entity.AlarmEntity;
import com.monitor.web.common.service.ServiceBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AlarmService extends ServiceBase {

    @Autowired
    private AlarmDAO alarmDao;
    @Autowired
    private SubscriberService subscriberService;
    @Autowired
    private AlarmScheduler alarmScheduler;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private SchedulerService schedulerService;
    @Autowired
    private AlarmSchedulerRelationService alarmSchedulerRelationService;

    /**
     * 新增
     *
     * @param alarmDTO alarmDTO
     * @param request  request
     * @return Object
     * @throws Exception Exception
     */
    @Transactional(rollbackOn = {Exception.class})
    public Object add(AlarmDTO alarmDTO, HttpServletRequest request) throws Exception {

        // 从DTO中复制属性
        AlarmEntity alarmEntity = new AlarmEntity();
        BeanUtils.copyProperties(alarmDTO, alarmEntity);

        // 创建时间、更新时间
        Date nowTime = new Date();
        alarmEntity.setCreateTime(nowTime);
        alarmEntity.setUpdateTime(nowTime);
        // createBy
        Long createBy = tokenService.getUserIdByRequest(request);
        if (createBy == null) {
            throw new Exception("token已失效");
        }
        alarmEntity.setCreateBy(createBy);
        // isDeleted
        alarmEntity.setIsDeleted(0);

        alarmDao.save(alarmEntity);

        String subscriberList = alarmDTO.getSubscriberList();
        List<HashMap<String, Object>> list = DataConvertUtils.jsonStrToObject(subscriberList, List.class);
        for (HashMap<String, Object> map : list) {
            SubscriberEntity subscriberEntity = DataConvertUtils.mapToBean(map, SubscriberEntity.class);
            if (subscriberEntity == null) throw new Exception("subscriberList格式不正确");
            subscriberEntity.setAlarmId(alarmEntity.getId());
            subscriberService.add(subscriberEntity);
        }

        // 启动预警定时任务
        this.startAlarmScheduler(alarmEntity);

        return alarmEntity;
    }

    /**
     * 编辑
     *
     * @param request request
     * @return Object
     */
    @Transactional(rollbackOn = {Exception.class})
    public Object update(HttpServletRequest request) throws Exception {

        // 参数校验
        Long id = DataConvertUtils.strToLong(request.getParameter("id"));
        if (id == null) {
            throw new Exception("id不能为空");
        }

        // 参数获取
        String name = request.getParameter("name");
        String projectIdentifier = request.getParameter("projectIdentifier");
        Integer level = DataConvertUtils.strToIntegerOrNull(request.getParameter("level"));
        Integer category = DataConvertUtils.strToIntegerOrNull(request.getParameter("category"));
        String rule = request.getParameter("rule");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        Integer silentPeriod = DataConvertUtils.strToIntegerOrNull(request.getParameter("silentPeriod"));
        Integer isActive = DataConvertUtils.strToIntegerOrNull(request.getParameter("isActive"));
        Long createBy = DataConvertUtils.strToLong(request.getParameter("createBy"));
        Integer isDeleted = DataConvertUtils.strToIntegerOrNull(request.getParameter("isDeleted"));
        String subscriberList = request.getParameter("subscriberList");

        // 修改
        AlarmEntity alarmEntity = alarmDao.getById(id).orElseThrow(() -> new Exception("该预警不存在"));
        if (!StringUtils.isEmpty(name)) {
            alarmEntity.setName(name);
        }
        if (projectIdentifier != null && !projectIdentifier.isEmpty()) {
            alarmEntity.setProjectIdentifier(projectIdentifier);
        }
        if (level != null) {
            alarmEntity.setLevel(level);
        }
        if (category != null) {
            alarmEntity.setCategory(category);
        }
        if (!StringUtils.isEmpty(rule)) {
            alarmEntity.setRule(rule);
        }
        if (!StringUtils.isEmpty(startTime)) {
            alarmEntity.setStartTime(startTime);
        }
        if (!StringUtils.isEmpty(endTime)) {
            alarmEntity.setEndTime(endTime);
        }
        if (silentPeriod != null) {
            alarmEntity.setSilentPeriod(silentPeriod);
        }
        if (isActive != null) {

            // 若状态改为启动，则先停止已有的定时任务，再重新启动对应的定时任务
            this.stopAlarmScheduler(alarmEntity);
            if (isActive == 1) {
                this.startAlarmScheduler(alarmEntity);
            }

            alarmEntity.setIsActive(isActive);
        }
        if (createBy != null) {
            alarmEntity.setCreateBy(createBy);
        }
        if (isDeleted != null) {
            alarmEntity.setIsDeleted(isDeleted);
        }
        if (!StringUtils.isEmpty(subscriberList)) {

            // 先删除已有的关联关系
            subscriberService.deleteAllByAlarmId(alarmEntity.getId());

            // 再创建新的关联关系
            List<HashMap<String, Object>> list = DataConvertUtils.jsonStrToObject(subscriberList, List.class);
            for (HashMap<String, Object> map : list) {
                SubscriberEntity subscriberEntity = DataConvertUtils.mapToBean(map, SubscriberEntity.class);
                if (subscriberEntity == null) throw new Exception("subscriberList格式不正确");
                subscriberEntity.setAlarmId(alarmEntity.getId());
                subscriberService.add(subscriberEntity);
            }
        }

        // 保存实体
        alarmDao.save(alarmEntity);

        return alarmEntity;
    }

    /**
     * 查询
     *
     * @param request request
     * @return Object
     */
    public Object get(HttpServletRequest request) {

        // 获取请求参数
        int pageNum = DataConvertUtils.strToInt(request.getParameter("pageNum"));
        int pageSize = DataConvertUtils.strToInt(request.getParameter("pageSize"));
        String name = request.getParameter("name");
        String projectIdentifier = request.getParameter("projectIdentifier");
        Integer level = DataConvertUtils.strToIntegerOrNull(request.getParameter("level"));
        Integer category = DataConvertUtils.strToIntegerOrNull(request.getParameter("category"));
        String rule = request.getParameter("rule");
        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        Integer silentPeriod = DataConvertUtils.strToIntegerOrNull(request.getParameter("silentPeriod"));
        Integer isActive = DataConvertUtils.strToIntegerOrNull(request.getParameter("isActive"));
        Long createBy = DataConvertUtils.strToLong(request.getParameter("createBy"));
        Integer isDeleted = DataConvertUtils.strToIntegerOrNull(request.getParameter("isDeleted"));

        // 拼接sql，分页查询
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Map<String, Object> paramMap = new HashMap<>();
        StringBuilder dataSqlBuilder = new StringBuilder("select * from ams_alarm t where 1=1");
        StringBuilder countSqlBuilder = new StringBuilder("select count(t.id) from ams_alarm t where 1=1");
        StringBuilder paramSqlBuilder = new StringBuilder();

        // 预警名称
        if (!StringUtils.isEmpty(name)) {
            paramSqlBuilder.append(" and t.name like :name");
            paramMap.put("name", "%" + name + "%");
        }
        // 项目标识
        if (!StringUtils.isEmpty(projectIdentifier)) {
            paramSqlBuilder.append(" and t.project_identifier = :projectIdentifier");
            paramMap.put("projectIdentifier", projectIdentifier);
        }
        // 报警等级
        if (level != null) {
            paramSqlBuilder.append(" and t.level = :level");
            paramMap.put("level", level);
        }
        // 过滤条件
        if (category != null) {
            paramSqlBuilder.append(" and t.category = :category");
            paramMap.put("category", category);
        }
        // 预警规则
        if (!StringUtils.isEmpty(rule)) {
            paramSqlBuilder.append(" and t.rule like :rule");
            paramMap.put("rule", "%" + rule + "%");
        }
        // 报警时段-开始时间
        if (!StringUtils.isEmpty(startTime)) {
            paramSqlBuilder.append(" and t.start_time = :startTime");
            paramMap.put("startTime", startTime);
        }
        // 报警时段-结束时间
        if (!StringUtils.isEmpty(endTime)) {
            paramSqlBuilder.append(" and t.end_time = :endTime");
            paramMap.put("endTime", endTime);
        }
        // 静默期
        if (silentPeriod != null) {
            paramSqlBuilder.append(" and t.silent_period = :silentPeriod");
            paramMap.put("silentPeriod", silentPeriod);
        }
        // 是否启用
        if (isActive != null) {
            paramSqlBuilder.append(" and t.is_active = :isActive");
            paramMap.put("isActive", isActive);
        }
        // 创建人ID
        if (createBy != null) {
            paramSqlBuilder.append(" and t.create_by = :createBy");
            paramMap.put("createBy", createBy);
        }
        // 是否已被删除
        if (isDeleted != null) {
            paramSqlBuilder.append(" and t.is_deleted = :isDeleted");
            paramMap.put("isDeleted", isDeleted);
        }
        dataSqlBuilder.append(paramSqlBuilder).append(" order by t.update_time desc");
        countSqlBuilder.append(paramSqlBuilder);
        Page<AlarmEntity> page = this.findPageBySqlAndParam(AlarmEntity.class, dataSqlBuilder.toString(), countSqlBuilder.toString(), pageable, paramMap);

        // 返回
        List<AlarmVO> voList = page.getContent().stream().map(this::transAlarmEntityToAlarmVO).collect(Collectors.toList());
        PageResultBase<AlarmVO> pageResultBase = new PageResultBase<>();
        pageResultBase.setTotalNum(page.getTotalElements());
        pageResultBase.setTotalPage(page.getTotalPages());
        pageResultBase.setPageNum(pageNum);
        pageResultBase.setPageSize(pageSize);
        pageResultBase.setRecords(voList);
        return pageResultBase;
    }

    /**
     * 删除
     *
     * @param id id
     * @return Object
     */
    @Transactional(rollbackOn = {Exception.class})
    public Object delete(Long id) throws Exception {
        AlarmEntity alarmEntity = alarmDao.findById(id).orElseThrow(() -> new Exception("项目不存在"));

        // 删除关联的订阅者
        subscriberService.deleteAllByAlarmId(id);

        // 停止预警定时任务
        this.stopAlarmScheduler(alarmEntity);

        // 删除实体记录
        alarmDao.deleteById(id);

        return true;
    }

    /**
     * 根据alarmId获取关联的项目名称
     *
     * @param alarmId alarmId
     * @return String
     */
    public String getProjectNameByAlarmId(long alarmId) {
        String projectName = "";
        AlarmEntity alarmEntity = alarmDao.getById(alarmId).orElse(null);
        if (alarmEntity != null) {
            String projectIdentifier = alarmEntity.getProjectIdentifier();
            ProjectEntity projectEntity = projectService.findByProjectIdentifier(projectIdentifier).orElse(null);
            if (projectEntity != null) {
                projectName = projectEntity.getProjectName();
            }
        }
        return projectName;
    }

    /**
     * AlarmEntity转AlarmVO
     *
     * @param alarmEntity alarmEntity
     * @return AlarmVO
     */
    private AlarmVO transAlarmEntityToAlarmVO(AlarmEntity alarmEntity) {
        AlarmVO alarmVO = new AlarmVO();
        BeanUtils.copyProperties(alarmEntity, alarmVO);
        try {
            this.setSubscriberListToAlarmVO(alarmVO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return alarmVO;
    }

    /**
     * 设置subscriberList
     *
     * @param alarmVO alarmVO
     */
    private void setSubscriberListToAlarmVO(AlarmVO alarmVO) throws JsonProcessingException {
        Long alarmId = alarmVO.getId();
        List<SubscriberEntity> subscriberEntityList = subscriberService.getAllByAlarmId(alarmId);
        String subscriberList = "";
        if (subscriberEntityList.size() > 0) {
            subscriberList = DataConvertUtils.objectToJsonStr(subscriberEntityList);
        }
        alarmVO.setSubscriberList(subscriberList);
    }

    /**
     * 启动预警定时任务
     *
     * @param alarmEntity alarmEntity
     */
    @Transactional(rollbackOn = {Exception.class})
    void startAlarmScheduler(AlarmEntity alarmEntity) {
        String params = null;
        try {
            params = DataConvertUtils.objectToJsonStr(alarmEntity);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        // 保存定时任务
        SchedulerDTO schedulerDTO = new SchedulerDTO();
        String beanName = alarmScheduler.getBeanName();
        String methodName = alarmScheduler.getMethodName();
        String cronExpression = alarmScheduler.getCronExpression();
        schedulerDTO.setBeanName(beanName);
        schedulerDTO.setMethodName(methodName);
        schedulerDTO.setParams(params);
        schedulerDTO.setCronExpression(cronExpression);
        schedulerDTO.setState(1);
        SchedulerEntity schedulerEntity = schedulerService.add(schedulerDTO);

        // 保存预警-定时任务关联表
        Long alarmId = alarmEntity.getId();
        Long schedulerId = schedulerEntity.getId();
        AlarmSchedulerRelationDTO alarmSchedulerRelationDTO = new AlarmSchedulerRelationDTO();
        alarmSchedulerRelationDTO.setAlarmId(alarmId);
        alarmSchedulerRelationDTO.setSchedulerId(schedulerId);
        alarmSchedulerRelationService.add(alarmSchedulerRelationDTO);

        // 启动定时任务
        schedulerService.startScheduler(schedulerEntity);
    }

    /**
     * 停止预警定时任务
     *
     * @param alarmEntity alarmEntity
     */
    void stopAlarmScheduler(AlarmEntity alarmEntity) {

        int isActive = alarmEntity.getIsActive();
        if (isActive == 0) return;

        // 如果为启动中的预警
        long alarmId = alarmEntity.getId();
        List<AlarmSchedulerRelationEntity> list = alarmSchedulerRelationService.findAllByAlarmId(alarmId);
        if (list.size() == 0) return;

        list.forEach(entity -> {
            long schedulerId = entity.getSchedulerId();
            SchedulerEntity schedulerEntity = schedulerService.getById(schedulerId).orElse(null);
            if (schedulerEntity == null) return;

            // 终止其关联的执行中的定时任务
            schedulerService.stopScheduler(schedulerEntity);

            // 删除定时任务
            schedulerService.deleteByEntity(schedulerEntity);

            // 删除预警-定时任务关联表
            alarmSchedulerRelationService.deleteByEntity(entity);
        });
    }
}
