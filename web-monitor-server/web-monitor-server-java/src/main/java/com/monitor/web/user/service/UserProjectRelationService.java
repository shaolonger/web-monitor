package com.monitor.web.user.service;

import com.monitor.web.user.entity.UserProjectRelationEntity;
import com.monitor.web.user.dao.UserProjectRelationDAO;
import com.monitor.web.common.service.ServiceBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserProjectRelationService extends ServiceBase {

    @Autowired
    private UserProjectRelationDAO userProjectRelationDao;

    /**
     * 新增
     *
     * @param userProjectRelationEntity userProjectRelationEntity
     * @return Object
     */
    public Object add(UserProjectRelationEntity userProjectRelationEntity) {

        log.info("--------[UserProjectRelationService]保存开始--------");
        
        // 保存实体
        userProjectRelationDao.save(userProjectRelationEntity);

        log.info("--------[UserProjectRelationService]保存结束--------");

        return userProjectRelationEntity;
    }
}
