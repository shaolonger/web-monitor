package com.monitor.web.alarm.service;

import com.monitor.web.alarm.dao.SubscriberNotifyRecordDAO;
import com.monitor.web.alarm.dto.SubscriberNotifyRecordDTO;
import com.monitor.web.alarm.entity.SubscriberNotifyRecordEntity;
import com.monitor.web.alarm.vo.SubscriberNotifyRecordVO;
import com.monitor.web.common.api.PageResultBase;
import com.monitor.web.common.service.ServiceBase;
import com.monitor.web.utils.DataConvertUtils;
import com.monitor.web.utils.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SubscriberNotifyRecordService extends ServiceBase {

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
        Date startTime = DateUtils.strToDate(request.getParameter("startTime"), "yyyy-MM-dd HH:mm:ss");
        Date endTime = DateUtils.strToDate(request.getParameter("endTime"), "yyyy-MM-dd HH:mm:ss");
        Long alarmRecordId = DataConvertUtils.strToLong(request.getParameter("alarmRecordId"));
        Long subscriberId = DataConvertUtils.strToLong(request.getParameter("subscriberId"));
        Integer state = DataConvertUtils.strToIntegerOrNull(request.getParameter("state"));
        String content = request.getParameter("content");

        // 拼接sql，分页查询
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Map<String, Object> paramMap = new HashMap<>();
        StringBuilder dataSqlBuilder = new StringBuilder("select * from ams_subscriber_notify_record t where 1=1");
        StringBuilder countSqlBuilder = new StringBuilder("select count(t.id) from ams_subscriber_notify_record t where 1=1");
        StringBuilder paramSqlBuilder = new StringBuilder();

        // 报警记录id
        if (alarmRecordId != null) {
            paramSqlBuilder.append(" and t.alarm_record_id = :alarmRecordId");
            paramMap.put("alarmRecordId", alarmRecordId);
        }

        // 报警订阅方id
        if (subscriberId != null) {
            paramSqlBuilder.append(" and t.subscriber_id = :subscriberId");
            paramMap.put("subscriberId", subscriberId);
        }

        // 通知状态，0-失败，1-成功
        if (state != null) {
            paramSqlBuilder.append(" and t.state = :state");
            paramMap.put("state", state);
        }

        // 开始时间、结束时间
        if (startTime != null && endTime != null) {
            paramSqlBuilder.append(" and t.create_time between :startTime and :endTime");
            paramMap.put("startTime", startTime);
            paramMap.put("endTime", endTime);
        } else if (startTime != null) {
            paramSqlBuilder.append(" and t.create_time >= :startTime");
            paramMap.put("startTime", startTime);
        } else if (endTime != null) {
            paramSqlBuilder.append(" and t.create_time <= :endTime");
            paramMap.put("endTime", endTime);
        }

        if (!StringUtils.isEmpty(content)) {
            paramSqlBuilder.append(" and t.content like :content");
            paramMap.put("content", "%" + content + "%");
        }
        dataSqlBuilder.append(paramSqlBuilder).append(" order by t.create_time desc");
        countSqlBuilder.append(paramSqlBuilder);
        Page<SubscriberNotifyRecordEntity> page = this.findPageBySqlAndParam(SubscriberNotifyRecordEntity.class, dataSqlBuilder.toString(), countSqlBuilder.toString(), pageable, paramMap);

        // 返回
        List<SubscriberNotifyRecordVO> voList = page.getContent().stream().map(this::transEntityToVO).collect(Collectors.toList());
        PageResultBase<SubscriberNotifyRecordVO> pageResultBase = new PageResultBase<>();
        pageResultBase.setTotalNum(page.getTotalElements());
        pageResultBase.setTotalPage(page.getTotalPages());
        pageResultBase.setPageNum(pageNum);
        pageResultBase.setPageSize(pageSize);
        pageResultBase.setRecords(voList);
        return pageResultBase;
    }

    /**
     * 根据alarmRecordId获取所有记录
     *
     * @param alarmRecordId alarmRecordId
     * @return List
     */
    public List<SubscriberNotifyRecordEntity> findAllByAlarmRecordId(long alarmRecordId) {
        return subscriberNotifyRecordDAO.findAllByAlarmRecordId(alarmRecordId);
    }

    /**
     * Entity转VO
     *
     * @param subscriberNotifyRecordEntity subscriberNotifyRecordEntity
     * @return SubscriberNotifyRecordVO
     */
    private SubscriberNotifyRecordVO transEntityToVO(SubscriberNotifyRecordEntity subscriberNotifyRecordEntity) {
        SubscriberNotifyRecordVO subscriberNotifyRecordVO = new SubscriberNotifyRecordVO();
        BeanUtils.copyProperties(subscriberNotifyRecordEntity, subscriberNotifyRecordVO);

        return subscriberNotifyRecordVO;
    }
}
