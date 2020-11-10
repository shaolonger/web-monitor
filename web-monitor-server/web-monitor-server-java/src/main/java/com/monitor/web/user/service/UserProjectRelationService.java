package com.monitor.web.user.service;

import com.monitor.web.user.entity.UserProjectRelationEntity;
import com.monitor.web.user.dao.UserProjectRelationDao;
import com.monitor.web.common.service.ServiceBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserProjectRelationService extends ServiceBase {

    @Autowired
    private UserProjectRelationDao userProjectRelationDao;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    /**
     * 新增
     *
     * @param userProjectRelationEntity userProjectRelationEntity
     * @return Object
     */
    public Object add(UserProjectRelationEntity userProjectRelationEntity) {

        logger.info("--------[UserProjectRelationService]保存开始--------");
        
        // 保存实体
        userProjectRelationDao.save(userProjectRelationEntity);

        logger.info("--------[UserProjectRelationService]保存结束--------");

        return userProjectRelationEntity;
    }
}
