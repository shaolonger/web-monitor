package com.monitor.web.alarm.service;

import com.monitor.web.alarm.dao.AlarmRecordDAO;
import com.monitor.web.alarm.dto.AlarmRecordDTO;
import com.monitor.web.alarm.entity.AlarmRecordEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AlarmRecordService {

    @Autowired
    AlarmRecordDAO alarmRecordDAO;

    /**
     * 新增
     *
     * @param alarmRecordDTO alarmRecordDTO
     * @return boolean
     */
    public boolean add(AlarmRecordDTO alarmRecordDTO) {
        AlarmRecordEntity alarmRecordEntity = new AlarmRecordEntity();

        // alarmId
        alarmRecordEntity.setAlarmId(alarmRecordDTO.getAlarmId());

        // alarmData
        alarmRecordEntity.setAlarmData(alarmRecordDTO.getAlarmData());

        // createTime
        Date createTime = new Date();
        alarmRecordEntity.setCreateTime(createTime);

        // state
        alarmRecordEntity.setState(alarmRecordDTO.getState());

        alarmRecordDAO.save(alarmRecordEntity);

        return true;
    }
}
