package com.monitor.web.user.service;

import com.monitor.web.user.dao.UserProjectRelationDAO;
import com.monitor.web.user.entity.UserEntity;
import com.monitor.web.user.entity.UserProjectRelationEntity;
import com.monitor.web.auth.dto.LoginUserDTO;
import com.monitor.web.auth.service.TokenService;
import com.monitor.web.project.entity.ProjectEntity;
import com.monitor.web.project.service.ProjectService;
import com.monitor.web.user.dao.UserDAO;
import com.monitor.web.common.api.PageResultBase;
import com.monitor.web.common.service.ServiceBase;
import com.monitor.web.utils.TokenUtils;
import com.monitor.web.utils.DataConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService extends ServiceBase {

    @Autowired
    private UserDAO userDao;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserProjectRelationDAO userProjectRelationDao;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 新增
     *
     * @param userEntity userEntity
     * @return Object
     */
    public Object add(UserEntity userEntity) {

        logger.info("--------[UserService]保存开始--------");

        // 创建时间
        Date createTime = new Date();

        // 保存实体
        userEntity.setIsAdmin(0);
        userEntity.setCreateTime(createTime);
        userDao.save(userEntity);

        logger.info("--------[UserService]保存结束--------");

        return userEntity;
    }

    /**
     * 多条件分页查询
     *
     * @param request request
     * @return Object
     */
    public Object get(HttpServletRequest request) throws Exception {
        int isNeedPaging = DataConvertUtils.strToInt(request.getParameter("isNeedPaging"));
        if (isNeedPaging == 0) {
            // 不需要分页
            return userDao.findAll();
        } else if (isNeedPaging == 1) {
            // 需要分页
            // 获取请求参数
            int pageNum = DataConvertUtils.strToInt(request.getParameter("pageNum"));
            int pageSize = DataConvertUtils.strToInt(request.getParameter("pageSize"));
            String username = request.getParameter("username");
            String phone = request.getParameter("phone");
            Integer gender = DataConvertUtils.strToIntegerOrNull(request.getParameter("gender"));

            // 拼接sql，分页查询
            Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
            Map<String, Object> paramMap = new HashMap<>();
            StringBuilder dataSqlBuilder = new StringBuilder("select * from ums_user t where 1=1");
            StringBuilder countSqlBuilder = new StringBuilder("select count(t.id) from ums_user t where 1=1");
            StringBuilder paramSqlBuilder = new StringBuilder();

            // 用户名
            if (username != null && !username.isEmpty()) {
                paramSqlBuilder.append(" and t.username like :username");
                paramMap.put("username", "%" + username + "%");
            }

            // 电话
            if (phone != null && !phone.isEmpty()) {
                paramSqlBuilder.append(" and t.phone like :phone");
                paramMap.put("phone", "%" + phone + "%");
            }

            // 性别
            if (gender != null) {
                paramSqlBuilder.append(" and t.gender = :gender");
                paramMap.put("gender", gender);
            }
            dataSqlBuilder.append(paramSqlBuilder).append(" order by t.create_time desc");
            countSqlBuilder.append(paramSqlBuilder);
            Page<UserEntity> page = this.findPageBySqlAndParam(UserEntity.class, dataSqlBuilder.toString(), countSqlBuilder.toString(), pageable, paramMap);

            // 返回
            PageResultBase<UserEntity> pageResultBase = new PageResultBase<>();
            pageResultBase.setTotalNum(page.getTotalElements());
            pageResultBase.setTotalPage(page.getTotalPages());
            pageResultBase.setPageNum(pageNum);
            pageResultBase.setPageSize(pageSize);
            pageResultBase.setRecords(page.getContent());
            return pageResultBase;
        } else {
            throw new Exception("isNeedPaging参数不正确");
        }
    }

    /**
     * 登录
     *
     * @param request request
     * @return Object
     * @throws Exception Exception
     */
    public Object login(HttpServletRequest request) throws Exception {
        String username = DataConvertUtils.getStrOrEmpty(request.getParameter("username"));
        String password = DataConvertUtils.getStrOrEmpty(request.getParameter("password"));

        // 参数判断
        if (username.length() == 0 || password.length() == 0) {
            throw new Exception("用户名或密码不能为空");
        }

        // 查询
        List<UserEntity> userEntityList = userDao.findByUsernameAndPassword(username, password);
        if (userEntityList.size() == 0) {
            throw new Exception("用户名或密码不正确");
        }

        // 获取token并存入redis
        UserEntity userEntity = userEntityList.get(0);
        String token = TokenUtils.getToken();
        return tokenService.addOrUpdateToken(token, userEntity.getId(), userEntity.getUsername(), userEntity.getIsAdmin());
    }

    /**
     * 根据用户获取关联的项目
     *
     * @param request request
     * @return Object
     */
    public Object getRelatedProjectList(HttpServletRequest request) {

        // 通过获取userId
        String token = request.getHeader("token");
        LoginUserDTO user = tokenService.getUserByToken(token);

        // 获取关联的项目列表
        List<UserProjectRelationEntity> relatedProjectList = userProjectRelationDao.findByUserId(user.getId());
        List<Long> projectIdList = relatedProjectList.stream().map(UserProjectRelationEntity::getProjectId).collect(Collectors.toList());
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Long projectId : projectIdList) {
            Optional<ProjectEntity> optional = projectService.getProjectById(projectId);
            ProjectEntity projectEntity = optional.orElse(null);
            if (projectEntity != null) {
                Map<String, Object> project = new HashMap<>();
                project.put("projectId", projectEntity.getId());
                project.put("projectName", projectEntity.getProjectName());
                project.put("projectIdentifier", projectEntity.getProjectIdentifier());
                resultList.add(project);
            }
        }

        return resultList;
    }
}
