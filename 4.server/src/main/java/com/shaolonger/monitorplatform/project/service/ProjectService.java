package com.shaolonger.monitorplatform.project.service;

import com.shaolonger.monitorplatform.project.dao.ProjectDao;
import com.shaolonger.monitorplatform.project.entity.ProjectEntity;
import com.shaolonger.monitorplatform.utils.ServiceBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProjectService extends ServiceBase {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProjectDao projectDao;

    /**
     * 新增
     *
     * @param projectEntity projectEntity
     * @return Object
     */
    public Object add(ProjectEntity projectEntity) {

        logger.info("--------[ProjectService]保存开始--------");

        // 创建时间
        Date createTime = new Date();
        projectEntity.setCreateTime(createTime);
        projectDao.save(projectEntity);

        logger.info("--------[ProjectService]保存结束--------");

        return projectEntity;
    }
}
