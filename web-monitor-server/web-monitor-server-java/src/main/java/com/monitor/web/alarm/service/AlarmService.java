package com.monitor.web.alarm.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.monitor.web.alarm.dto.AlarmDTO;
import com.monitor.web.alarm.entity.SubscriberEntity;
import com.monitor.web.alarm.vo.AlarmVO;
import com.monitor.web.common.api.PageResultBase;
import com.monitor.web.utils.DataConvertUtils;
import com.monitor.web.alarm.dao.AlarmDAO;
import com.monitor.web.alarm.entity.AlarmEntity;
import com.monitor.web.common.service.ServiceBase;
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

@Service
public class AlarmService extends ServiceBase {

    @Autowired
    private AlarmDAO alarmDao;

    @Autowired
    private SubscriberService subscriberService;

    /**
     * 新增
     *
     * @return Object
     */
    @Transactional(rollbackOn = {Exception.class})
    public Object add(AlarmDTO alarmDTO) throws Exception {

        // 从DTO中复制属性
        AlarmEntity alarmEntity = new AlarmEntity();
        BeanUtils.copyProperties(alarmDTO, alarmEntity);

        // 创建时间、更新时间
        Date nowTime = new Date();
        alarmEntity.setCreateTime(nowTime);
        alarmEntity.setUpdateTime(nowTime);
        alarmDao.save(alarmEntity);

        String subscriberList = alarmDTO.getSubscriberList();
        List<HashMap<String, Object>> list = DataConvertUtils.jsonStrToObject(subscriberList, List.class);
        for (HashMap<String, Object> map : list) {
            SubscriberEntity subscriberEntity = DataConvertUtils.mapToBean(map, SubscriberEntity.class);
            if (subscriberEntity == null) throw new Exception("subscriberList格式不正确");
            subscriberEntity.setAlarmId(alarmEntity.getId());
            subscriberService.add(subscriberEntity);
        }
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
        if (name != null && !name.isEmpty()) {
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
        if (rule != null && !rule.isEmpty()) {
            alarmEntity.setRule(rule);
        }
        if (startTime != null && !startTime.isEmpty()) {
            alarmEntity.setStartTime(startTime);
        }
        if (endTime != null && !endTime.isEmpty()) {
            alarmEntity.setEndTime(endTime);
        }
        if (silentPeriod != null) {
            alarmEntity.setSilentPeriod(silentPeriod);
        }
        if (isActive != null) {
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
        return alarmDao.save(alarmEntity);
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
        if (name != null && !name.isEmpty()) {
            paramSqlBuilder.append(" and t.name like :name");
            paramMap.put("name", "%" + name + "%");
        }
        // 项目标识
        if (projectIdentifier != null && !projectIdentifier.isEmpty()) {
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
        if (rule != null && !rule.isEmpty()) {
            paramSqlBuilder.append(" and t.rule like :rule");
            paramMap.put("rule", "%" + rule + "%");
        }
        // 报警时段-开始时间
        if (startTime != null && !startTime.isEmpty()) {
            paramSqlBuilder.append(" and t.start_time = :startTime");
            paramMap.put("startTime", startTime);
        }
        // 报警时段-结束时间
        if (endTime != null && !endTime.isEmpty()) {
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
        boolean isExisted = alarmDao.existsById(id);
        if (!isExisted) throw new Exception("项目不存在");

        // 先删除关联的订阅者
        subscriberService.deleteAllByAlarmId(id);

        alarmDao.deleteById(id);
        return true;
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
}
