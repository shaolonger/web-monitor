package com.shaolonger.webmonitorserver.alarm.service;

import com.shaolonger.webmonitorserver.alarm.dao.AlarmDao;
import com.shaolonger.webmonitorserver.alarm.entity.AlarmEntity;
import com.shaolonger.webmonitorserver.utils.DataConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Date;

@Service
public class AlarmService {

    @Autowired
    AlarmDao alarmDao;

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
}
