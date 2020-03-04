package com.shaolonger.monitorplatform.user.service;

import com.shaolonger.monitorplatform.user.dao.UserRegisterRecordDao;
import com.shaolonger.monitorplatform.user.entity.UserRegisterRecordEntity;
import com.shaolonger.monitorplatform.utils.ServiceBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserRegisterRecordService extends ServiceBase {

    @Autowired
    private UserRegisterRecordDao userRegisterRecordDao;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

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
}
