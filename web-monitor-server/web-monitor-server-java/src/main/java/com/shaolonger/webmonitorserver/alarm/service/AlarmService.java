package com.shaolonger.webmonitorserver.alarm.service;

import com.shaolonger.webmonitorserver.alarm.dao.AlarmDao;
import com.shaolonger.webmonitorserver.alarm.entity.AlarmEntity;
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
     * @param request request
     * @return Object
     */
    @Transactional(rollbackOn = {Exception.class})
    public Object add(HttpServletRequest request, AlarmEntity alarmEntity) {
        // 创建时间、更新时间
        Date nowTime = new Date();
        alarmEntity.setCreateTime(nowTime);
        alarmEntity.setUpdateTime(nowTime);
        return alarmDao.save(alarmEntity);
    }
}
