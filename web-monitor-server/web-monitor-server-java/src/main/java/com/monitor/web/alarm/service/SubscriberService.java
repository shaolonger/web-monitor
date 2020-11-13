package com.monitor.web.alarm.service;

import com.monitor.web.alarm.dao.SubscriberDAO;
import com.monitor.web.alarm.entity.SubscriberEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubscriberService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SubscriberDAO subscriberDAO;

    /**
     * 新增
     *
     * @param subscriberEntity subscriberEntity
     * @return Object
     */
    public Object add(SubscriberEntity subscriberEntity) throws Exception {

        // 参数校验
        Long alarmId = subscriberEntity.getAlarmId();
        String subscriber = subscriberEntity.getSubscriber();

        if (alarmId == null) throw new Exception("alarmId不能为空");
        if (subscriber == null) throw new Exception("subscriber不能为空");

        return subscriberDAO.save(subscriberEntity);
    }

    /**
     * 根据alarmId删除所有关联的subscriber
     *
     * @param alarmId alarmId
     * @return Object
     */
    public Object deleteAllByAlarmId(Long alarmId) {
        subscriberDAO.deleteAllByAlarmId(alarmId);
        return true;
    }

    /**
     * 根据alarmId获取所有关联的subscriber
     *
     * @param alarmId alarmId
     * @return List
     */
    public List<SubscriberEntity> getAllByAlarmId(Long alarmId) {
        return subscriberDAO.getAllByAlarmId(alarmId);
    }
}