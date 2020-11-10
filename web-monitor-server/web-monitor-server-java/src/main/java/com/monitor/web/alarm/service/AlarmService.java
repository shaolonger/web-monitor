package com.monitor.web.alarm.service;

import com.monitor.web.common.api.PageResultBase;
import com.monitor.web.utils.DataConvertUtils;
import com.monitor.web.alarm.dao.AlarmDao;
import com.monitor.web.alarm.entity.AlarmEntity;
import com.monitor.web.common.service.ServiceBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AlarmService extends ServiceBase {

    @Autowired
    private AlarmDao alarmDao;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 新增
     *
     * @return Object
     */
    @Transactional(rollbackOn = {Exception.class})
    public Object add(AlarmEntity alarmEntity) {
        // 创建时间、更新时间
        Date nowTime = new Date();
        alarmEntity.setCreateTime(nowTime);
        alarmEntity.setUpdateTime(nowTime);
        return alarmDao.save(alarmEntity);
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
        PageResultBase<AlarmEntity> pageResultBase = new PageResultBase<>();
        pageResultBase.setTotalNum(page.getTotalElements());
        pageResultBase.setTotalPage(page.getTotalPages());
        pageResultBase.setPageNum(pageNum);
        pageResultBase.setPageSize(pageSize);
        pageResultBase.setRecords(page.getContent());
        return pageResultBase;
    }
}
