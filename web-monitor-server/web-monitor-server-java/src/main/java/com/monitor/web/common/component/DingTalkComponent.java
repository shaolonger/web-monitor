package com.monitor.web.common.component;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class DingTalkComponent {

    private static final String DT_SERVER_URL = "https://oapi.dingtalk.com/robot/send?access_token=";

    private com.dingtalk.api.DingTalkClient client = null;

    public DingTalkComponent(String accessToken) {
        this.client = new DefaultDingTalkClient(DT_SERVER_URL + accessToken);
    }

    /**
     * 发送TEXT
     *
     * @param content   发送的文本内容
     * @param atMobiles 要@的对象列表
     * @return boolean 是否发送成功
     */
    public boolean sendText(String content, List<String> atMobiles) {
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("text");
        OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
        text.setContent(content);
        request.setText(text);
        if (atMobiles != null && atMobiles.size() > 0) {
            OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
            at.setAtMobiles(atMobiles);
            at.setIsAtAll(true);
            request.setAt(at);
        }
        boolean isSendSuccess = false;
        try {
            OapiRobotSendResponse response = client.execute(request);
            isSendSuccess = response.isSuccess();
            if (!isSendSuccess) {
                String errorCode = response.getErrorCode();
                String errMsg = response.getErrmsg();
                log.error("[钉钉机器人webHook调用失败]errorCode：{}，errMsg：{}", errorCode, errMsg);
            }
        } catch (ApiException e) {
            log.error(e.getErrMsg());
        }
        return isSendSuccess;
    }

    /**
     * 发送LINK
     *
     * @param linkUrl 链接地址
     * @param picUrl  图片地址
     * @param title   标题
     * @param text    内容
     * @return boolean
     */
    public boolean sendLink(String linkUrl, String picUrl, String title, String text) {
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("link");
        OapiRobotSendRequest.Link link = new OapiRobotSendRequest.Link();
        link.setMessageUrl(linkUrl);
        link.setPicUrl(picUrl);
        link.setTitle(title);
        link.setText(text);
        request.setLink(link);
        boolean isSendSuccess = false;
        try {
            OapiRobotSendResponse response = client.execute(request);
            log.info(response.toString());
            isSendSuccess = true;
        } catch (ApiException e) {
            log.error(e.getErrMsg());
        }
        return isSendSuccess;
    }

    /**
     * 发送Markdown
     *
     * @param title 标题
     * @param text  内容
     * @return boolean
     */
    public boolean sendMarkdown(String title, String text) {
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("markdown");
        OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
        markdown.setTitle(title);
        markdown.setText(text);
        request.setMarkdown(markdown);
        boolean isSendSuccess = false;
        try {
            OapiRobotSendResponse response = client.execute(request);
            log.info(response.toString());
            isSendSuccess = true;
        } catch (ApiException e) {
            log.error(e.getErrMsg());
        }
        return isSendSuccess;
    }
}
