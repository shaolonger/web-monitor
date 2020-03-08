package com.shaolonger.monitorplatform.project.service;

import com.shaolonger.monitorplatform.project.dao.ProjectDao;
import com.shaolonger.monitorplatform.project.entity.ProjectEntity;
import com.shaolonger.monitorplatform.utils.PageResultBase;
import com.shaolonger.monitorplatform.utils.ServiceBase;
import com.shaolonger.monitorplatform.utils.convert.DataConvertUtils;
import jdk.nashorn.internal.runtime.options.Option;
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
        String projectName = request.getParameter("projectName");

        // 拼接sql，分页查询
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Map<String, Object> paramMap = new HashMap<>();
        StringBuilder dataSqlBuilder = new StringBuilder("select * from pms_project t where 1=1");
        StringBuilder countSqlBuilder = new StringBuilder("select count(t.id) from pms_project t where 1=1");
        StringBuilder paramSqlBuilder = new StringBuilder();

        // 项目名称
        if (projectName != null && !projectName.isEmpty()) {
            paramSqlBuilder.append(" and t.project_name like :projectName");
            paramMap.put("projectName", "%" + projectName + "%");
        }
        dataSqlBuilder.append(paramSqlBuilder).append(" order by t.create_time desc");
        countSqlBuilder.append(paramSqlBuilder);
        Page<ProjectEntity> page = this.findPageBySqlAndParam(ProjectEntity.class, dataSqlBuilder.toString(), countSqlBuilder.toString(), pageable, paramMap);

        // 返回
        PageResultBase<ProjectEntity> pageResultBase = new PageResultBase<>();
        pageResultBase.setTotalNum(page.getTotalElements());
        pageResultBase.setTotalPage(page.getTotalPages());
        pageResultBase.setPageNum(pageNum);
        pageResultBase.setPageSize(pageSize);
        pageResultBase.setRecords(page.getContent());
        return pageResultBase;
    }

    /**
     * 删除
     *
     * @param id id
     * @return Object
     */
    public Object delete(Long id) throws Exception {

        logger.info("--------[ProjectService]删除开始--------");

        Optional<ProjectEntity> optional = projectDao.findById(id);
        ProjectEntity entity = optional.orElseThrow(() -> new Exception("找不到要删除的项目"));
        projectDao.delete(entity);

        logger.info("--------[ProjectService]删除结束--------");

        return true;
    }
}
