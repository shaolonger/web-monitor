package com.shaolonger.monitorplatform.user.service;

import com.shaolonger.monitorplatform.user.dao.UserDao;
import com.shaolonger.monitorplatform.user.entity.UserEntity;
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
public class UserService extends ServiceBase {

    @Autowired
    private UserDao userDao;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

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

        return userEntityList.get(0);
    }

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
        userEntity.setCreateTime(createTime);
        userDao.save(userEntity);

        logger.info("--------[UserService]保存结束--------");

        return userEntity;
    }
}
