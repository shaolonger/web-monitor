package com.monitor.web.log.service;

import com.monitor.web.log.dao.ClientUserDAO;
import com.monitor.web.log.dto.ClientUserDTO;
import com.monitor.web.log.entity.ClientUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
public class ClientUserService {

    @Autowired
    ClientUserDAO clientUserDAO;

    public Object add(ClientUserDTO clientUserDTO) {
        ClientUserEntity entity = new ClientUserEntity();

        // cUuid
        entity.setCUuid(clientUserDTO.getCUuid());

        // bUid
        Long bUid = clientUserDTO.getBUid();
        if (bUid != null) {
            entity.setBUid(bUid);
        } else {
            entity.setBUid(0L);
        }

        // bUname
        String bUname = clientUserDTO.getBUname();
        if (!StringUtils.isEmpty(bUname)) {
            entity.setBUname(bUname);
        } else {
            entity.setBUname("");
        }

        // createTime && updateTime
        Date createTime = new Date();
        entity.setCreateTime(createTime);
        entity.setUpdateTime(createTime);

        clientUserDAO.save(entity);

        return true;
    }

    /**
     * 计算总cUuid数
     *
     * @return long
     */
    public int countDistinctCUuid() {
        return clientUserDAO.countDistinctCUuid();
    }
}
