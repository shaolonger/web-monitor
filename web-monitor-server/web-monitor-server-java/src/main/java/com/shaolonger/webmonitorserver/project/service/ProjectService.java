package com.shaolonger.webmonitorserver.project.service;

import com.shaolonger.webmonitorserver.project.dao.ProjectDao;
import com.shaolonger.webmonitorserver.project.entity.ProjectEntity;
import com.shaolonger.webmonitorserver.project.vo.ProjectVO;
import com.shaolonger.webmonitorserver.user.dao.UserProjectRelationDao;
import com.shaolonger.webmonitorserver.user.entity.UserProjectRelationEntity;
import com.shaolonger.webmonitorserver.common.api.PageResultBase;
import com.shaolonger.webmonitorserver.common.service.ServiceBase;
import com.shaolonger.webmonitorserver.utils.DataConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectService extends ServiceBase {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private UserProjectRelationDao userProjectRelationDao;

    /**
     * 新增
     *
     * @param projectEntity projectEntity
     * @return Object
     */
    @Transactional(rollbackOn = {Exception.class})
    public Object add(HttpServletRequest request, ProjectEntity projectEntity) throws Exception {

        logger.info("--------[ProjectService]保存开始--------");

        // 创建时间
        Date createTime = new Date();
        projectEntity.setCreateTime(createTime);
        projectDao.save(projectEntity);

        // 保存关联用户
        String userListStr = request.getParameter("userList");
        List<Integer> userList = DataConvertUtils.jsonStrToObject(userListStr, List.class);
        for (Integer userId : userList) {
            UserProjectRelationEntity userProjectRelationEntity = new UserProjectRelationEntity();
            userProjectRelationEntity.setUserId(userId.longValue());
            userProjectRelationEntity.setProjectId(projectEntity.getId());
            userProjectRelationDao.save(userProjectRelationEntity);
        }

        logger.info("--------[ProjectService]保存结束--------");

        return projectEntity;
    }

    /**
     * 更新
     *
     * @param projectEntity projectEntity
     * @return Object
     */
    @Transactional(rollbackOn = {Exception.class})
    public Object update(HttpServletRequest request, ProjectEntity projectEntity) throws Exception {

        logger.info("--------[ProjectService]更新开始--------");

        // 获取要更新的实体
        Long id = DataConvertUtils.strToLong(request.getParameter("id"));
        String projectName = request.getParameter("projectName");
        String projectIdentifier = request.getParameter("projectIdentifier");
        String description = request.getParameter("description");
        String accessType = request.getParameter("accessType");
        String activeFuncs = request.getParameter("activeFuncs");
        if (id == null || id < 1) throw new Exception("id格式不正确");
        Optional<ProjectEntity> optional = projectDao.findById(id);
        ProjectEntity entity = optional.orElseThrow(() -> new Exception("项目不存在"));

        // 处理关联用户
        String requestUserListStr = request.getParameter("userList");
        List<Integer> requestUserList = DataConvertUtils.jsonStrToObject(requestUserListStr, List.class);
        List<UserProjectRelationEntity> relatedUserList = userProjectRelationDao.findByProjectId(entity.getId());
        List<Long> dbUserList = relatedUserList.stream().map(UserProjectRelationEntity::getUserId).collect(Collectors.toList());
        // 找出差异的关联用户
        List<Long> shouldAddUserList = new ArrayList<>();
        List<Long> shouldDelUserList = new ArrayList<>();
        for (Integer userId : requestUserList) {
            if (!dbUserList.contains(userId.longValue())) {
                shouldAddUserList.add(userId.longValue());
            } else {
                dbUserList.remove(userId.longValue());
            }
        }
        shouldDelUserList = dbUserList;
        for (Long userId : shouldAddUserList) {
            UserProjectRelationEntity userProjectRelationEntity = new UserProjectRelationEntity();
            userProjectRelationEntity.setUserId(userId);
            userProjectRelationEntity.setProjectId(entity.getId());
            userProjectRelationDao.save(userProjectRelationEntity);
        }
        for (Long userId : shouldDelUserList) {
            userProjectRelationDao.deleteByUserIdAndProjectId(userId, entity.getId());
        }

        // 修改时间
        Date updateTime = new Date();

        // 保存实体
        entity.setUpdateTime(updateTime);
        entity.setProjectName(projectName);
        entity.setProjectIdentifier(projectIdentifier);
        entity.setDescription(description);
        entity.setAccessType(accessType);
        entity.setActiveFuncs(activeFuncs);
        projectDao.save(entity);

        logger.info("--------[ProjectService]更新结束--------");

        return entity;
    }

    /**
     * 条件查询
     *
     * @param request request
     * @return Object
     */
    public Object get(HttpServletRequest request) throws Exception {
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
        dataSqlBuilder.append(paramSqlBuilder).append(" group by t.project_name order by t.create_time desc");
        countSqlBuilder.append(paramSqlBuilder);
        Page<ProjectEntity> page = this.findPageBySqlAndParam(ProjectEntity.class, dataSqlBuilder.toString(), countSqlBuilder.toString(), pageable, paramMap);

        // 转换vo
        List<ProjectVO> resultList = this.getProjectVOList(page.getContent());

        // 返回
        PageResultBase<ProjectVO> pageResultBase = new PageResultBase<>();
        pageResultBase.setTotalNum(page.getTotalElements());
        pageResultBase.setTotalPage(page.getTotalPages());
        pageResultBase.setPageNum(pageNum);
        pageResultBase.setPageSize(pageSize);
        pageResultBase.setRecords(resultList);
        return pageResultBase;
    }

    /**
     * 根据ID获取项目
     *
     * @param id 项目ID
     * @return Optional
     */
    public Optional<ProjectEntity> getProjectById(Long id) {
        return projectDao.findById(id);
    }

    /**
     * 根据projectIdentifier获取项目
     *
     * @param request request
     * @return Optional
     */
    public Optional<ProjectEntity> getProjectByProjectIdentifier(HttpServletRequest request) throws Exception {
        // 获取参数
        String projectIdentifier = request.getParameter("projectIdentifier");

        // 检验参数
        if (projectIdentifier == null || projectIdentifier.isEmpty()) throw new Exception("projectIdentifier不存在");
        return projectDao.findByProjectIdentifier(projectIdentifier);
    }

    /**
     * 删除
     *
     * @param id id
     * @return Object
     */
    @Transactional(rollbackOn = {Exception.class})
    public Object delete(Long id) throws Exception {

        logger.info("--------[ProjectService]删除开始--------");

        Optional<ProjectEntity> optional = projectDao.findById(id);
        ProjectEntity entity = optional.orElseThrow(() -> new Exception("找不到要删除的项目"));

        // 删除用户项目关联表中的数据
        userProjectRelationDao.deleteAllByProjectId(id);

        projectDao.delete(entity);

        logger.info("--------[ProjectService]删除结束--------");

        return true;
    }

    /**
     * entity转vo
     *
     * @param list list
     * @return List List
     */
    private List<ProjectVO> getProjectVOList(List<ProjectEntity> list) throws Exception {
        ArrayList<ProjectVO> returnList = new ArrayList<>();
        for (ProjectEntity entity : list) {
            String jsonStr = DataConvertUtils.objectToJsonStr(entity);
            ProjectVO projectVO = DataConvertUtils.jsonStrToObject(jsonStr, ProjectVO.class);
            returnList.add(projectVO);

            // 获取项目关联的用户
            List<UserProjectRelationEntity> relatedUserList = userProjectRelationDao.findByProjectId(projectVO.getId());
            String userList = relatedUserList.stream().map(UserProjectRelationEntity::getUserId)
                    .map(userId -> String.valueOf(userId)).collect(Collectors.joining(","));
            projectVO.setUserList(userList);
        }
        return returnList;
    }
}
