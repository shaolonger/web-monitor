package com.shaolonger.monitorplatform.user.service;

import com.shaolonger.monitorplatform.user.dao.UserDao;
import com.shaolonger.monitorplatform.user.dao.UserProjectRelationDao;
import com.shaolonger.monitorplatform.user.entity.UserProjectRelationEntity;
import com.shaolonger.monitorplatform.utils.ServiceBase;
import com.shaolonger.monitorplatform.utils.convert.DataConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
public class UserProjectRelationService extends ServiceBase {

    @Autowired
    UserProjectRelationDao userProjectRelationDao;

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
