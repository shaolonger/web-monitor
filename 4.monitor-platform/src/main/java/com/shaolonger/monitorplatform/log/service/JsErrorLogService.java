package com.shaolonger.monitorplatform.log.service;

import com.shaolonger.monitorplatform.log.dao.JsErrorLogDao;
import com.shaolonger.monitorplatform.log.entity.JsErrorLog;
import com.shaolonger.monitorplatform.utils.PageResultBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class JsErrorLogService {
    @Autowired
    private JsErrorLogDao jsErrorLogDao;

    public Object findByQueries(HttpServletRequest request) throws Exception {
        // 获取请求参数
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int pageNum = Integer.parseInt(request.getParameter("pageNum"));
        int pageSize = Integer.parseInt(request.getParameter("pageSize"));
        String logType = request.getParameter("logType");
        Date startTime = sdf.parse(request.getParameter("startTime"));
        Date endTime = sdf.parse(request.getParameter("endTime"));
        String userName = request.getParameter("userName");
        String pageUrl = request.getParameter("pageUrl");
        String errorType = request.getParameter("errorType");
        String errorMessage = request.getParameter("errorMessage");

        // 分页查询
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<JsErrorLog> page = jsErrorLogDao.findByQueries(logType, startTime, endTime, userName, pageUrl, errorType, errorMessage, pageable);
        List<JsErrorLog> resultList = page.getContent();

        // 返回
        PageResultBase<JsErrorLog> pageResultBase = new PageResultBase<>();
        pageResultBase.setTotalNum(page.getTotalElements());
        pageResultBase.setTotalPage(page.getTotalPages());
        pageResultBase.setPageNum(pageNum);
        pageResultBase.setPageSize(pageSize);
        return pageResultBase;
    }
}
