package com.shaolonger.monitorplatform.user.service;

import com.shaolonger.monitorplatform.user.dao.UserRegisterRecordDao;
import com.shaolonger.monitorplatform.user.entity.UserEntity;
import com.shaolonger.monitorplatform.user.entity.UserRegisterRecordEntity;
import com.shaolonger.monitorplatform.utils.DateUtils;
import com.shaolonger.monitorplatform.common.api.PageResultBase;
import com.shaolonger.monitorplatform.common.service.ServiceBase;
import com.shaolonger.monitorplatform.utils.DataConvertUtils;
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
import java.util.Optional;

@Service
public class UserRegisterRecordService extends ServiceBase {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRegisterRecordDao userRegisterRecordDao;

    @Autowired
    private UserService userService;

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
     * 审批
     *
     * @param request request
     * @return Object
     */
    public Object audit(HttpServletRequest request) throws Exception {

        Long auditId = DataConvertUtils.strToLong(request.getParameter("auditId"));
        Integer auditResult = DataConvertUtils.strToIntegerOrNull(request.getParameter("auditResult"));

        // 找到要审批的对象
        Optional<UserRegisterRecordEntity> optional = userRegisterRecordDao.findById(auditId);
        UserRegisterRecordEntity entity = optional.orElseThrow(() -> new Exception("找不到要审批的记录"));

        // 若已审批
        if (entity.getAuditResult() == 0 || entity.getAuditResult() == 1) {
            throw new Exception("该记录已审批");
        }

        // 校验审批结果参数
        if (auditResult == null) {
            throw new Exception("auditResult参数不能为空");
        }
        if (!auditResult.equals(0) && !auditResult.equals(1)) {
            throw new Exception("auditResult参数不正确");
        }

        // 保存用户记录审批表
        Date updateTime = new Date();
        entity.setAuditResult(auditResult);
        entity.setUpdateTime(updateTime);
        userRegisterRecordDao.save(entity);

        // 若为审批通过，则需在用户表中新增用户
        if (auditResult == 1) {
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(entity.getUsername());
            userEntity.setPassword(entity.getPassword());
            userEntity.setPhone(entity.getPhone());
            userEntity.setIcon(entity.getIcon());
            userEntity.setGender(entity.getGender());
            userEntity.setEmail(entity.getEmail());
            userService.add(userEntity);
        }

        return entity;
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
        Integer auditResult = DataConvertUtils.strToIntegerOrNull(request.getParameter("auditResult"));
        Date startTime = DateUtils.strToDate(request.getParameter("startTime"), "yyyy-MM-dd HH:mm:ss");
        Date endTime = DateUtils.strToDate(request.getParameter("endTime"), "yyyy-MM-dd HH:mm:ss");

        // 拼接sql，分页查询
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Map<String, Object> paramMap = new HashMap<>();
        StringBuilder dataSqlBuilder = new StringBuilder("select * from ums_user_register_record t where 1=1");
        StringBuilder countSqlBuilder = new StringBuilder("select count(t.id) from ums_user_register_record t where 1=1");
        StringBuilder paramSqlBuilder = new StringBuilder();

        // 审核结果
        if (auditResult != null) {
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
