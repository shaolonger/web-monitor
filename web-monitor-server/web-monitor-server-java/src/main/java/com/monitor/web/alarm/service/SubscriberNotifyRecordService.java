package com.monitor.web.alarm.service;

import com.monitor.web.alarm.dao.SubscriberNotifyRecordDAO;
import com.monitor.web.alarm.dto.SubscriberNotifyRecordDTO;
import com.monitor.web.alarm.entity.SubscriberNotifyRecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class SubscriberNotifyRecordService {

    @Autowired
    SubscriberNotifyRecordDAO subscriberNotifyRecordDAO;

    /**
     * 新增
     *
     * @param dto dto
     * @return boolean
     */
    public boolean add(SubscriberNotifyRecordDTO dto) {
        SubscriberNotifyRecordEntity entity = new SubscriberNotifyRecordEntity();

        // alarmRecordId
        entity.setAlarmRecordId(dto.getAlarmRecordId());

        // subscriberId
        entity.setSubscriberId(dto.getSubscriberId());

        // state
        entity.setState(dto.getState());

        // content
        String content = dto.getContent();
        if (content == null) {
            content = "";
        }
        entity.setContent(content);

        // createTime
        Date createTime = new Date();
        entity.setCreateTime(createTime);

        subscriberNotifyRecordDAO.save(entity);
        return true;
    }
}
