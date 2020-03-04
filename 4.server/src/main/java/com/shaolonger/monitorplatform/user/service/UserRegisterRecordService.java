package com.shaolonger.monitorplatform.user.service;

import com.shaolonger.monitorplatform.user.dao.UserRegisterRecordDao;
import com.shaolonger.monitorplatform.user.entity.UserRegisterRecordEntity;
import com.shaolonger.monitorplatform.utils.PageResultBase;
import com.shaolonger.monitorplatform.utils.ServiceBase;
import com.shaolonger.monitorplatform.utils.convert.DataConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserRegisterRecordService extends ServiceBase {

    @Autowired
    private UserRegisterRecordDao userRegisterRecordDao;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 新增
     *
     * @param userRegisterRecordEntity userRegisterRecordEntity
     * @return Object
     */
    public Object add(UserRegisterRecordEntity userRegisterRecordEntity) {

        logger.info("--------[UserRegisterRecordService]保存开始--------");

        // 创建时间
        Date nowTime = new Date();

        // 保存实体
        userRegisterRecordEntity.setAuditResult(-1);
        userRegisterRecordEntity.setCreateTime(nowTime);
        userRegisterRecordDao.save(userRegisterRecordEntity);

        logger.info("--------[UserRegisterRecordService]保存结束--------");

        return userRegisterRecordEntity;
    }

    /**
     * 条件查询
     *
     * @param request request
     * @return Object
     */
    public Object get(HttpServletRequest request) {
        // 获取请求参数
        int pageNum = DataConvertUtils.strToInt(request.getParameter("pageNum"));
        int pageSize = DataConvertUtils.strToInt(request.getParameter("pageSize"));
        String auditResult = request.getParameter("auditResult");
        Date startTime = DataConvertUtils.strToDate(request.getParameter("startTime"), "yyyy-MM-dd HH:mm:ss");
        Date endTime = DataConvertUtils.strToDate(request.getParameter("endTime"), "yyyy-MM-dd HH:mm:ss");

        // 拼接sql，分页查询
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Map<String, Object> paramMap = new HashMap<>();
        StringBuilder dataSqlBuilder = new StringBuilder("select * from ums_user_register_record t where 1=1");
        StringBuilder countSqlBuilder = new StringBuilder("select count(t.id) from ums_user_register_record t where 1=1");
        StringBuilder paramSqlBuilder = new StringBuilder();

        // 审核结果
        if (auditResult != null && !auditResult.isEmpty()) {
            paramSqlBuilder.append(" and t.audit_result = :auditResult");
            paramMap.put("auditResult", auditResult);
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
        dataSqlBuilder.append(paramSqlBuilder).append(" order by t.create_time desc");
        countSqlBuilder.append(paramSqlBuilder);
        Page<UserRegisterRecordEntity> page = this.findPageBySqlAndParam(UserRegisterRecordEntity.class, dataSqlBuilder.toString(), countSqlBuilder.toString(), pageable, paramMap);

        // 返回
        PageResultBase<UserRegisterRecordEntity> pageResultBase = new PageResultBase<>();
        pageResultBase.setTotalNum(page.getTotalElements());
        pageResultBase.setTotalPage(page.getTotalPages());
        pageResultBase.setPageNum(pageNum);
        pageResultBase.setPageSize(pageSize);
        pageResultBase.setRecords(page.getContent());
        return pageResultBase;
    }
}
