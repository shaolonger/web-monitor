package com.monitor.web.alarm.service;

import com.monitor.web.alarm.dao.SubscriberDAO;
import com.monitor.web.alarm.entity.SubscriberEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriberService {

    @Autowired
    private SubscriberDAO subscriberDAO;

    /**
     * 新增
     *
     * @param entity entity
     * @return Object
     */
    public Object add(SubscriberEntity entity) {
        return subscriberDAO.save(entity);
    }
}
