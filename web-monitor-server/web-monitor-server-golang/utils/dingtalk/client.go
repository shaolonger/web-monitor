package dingtalk

// 这里的部分代码参考了https://github.com/fastwego/dingding

import (
	"encoding/json"
	"errors"
	"io/ioutil"
	"net/http"
	"strings"
)

const (
	ServerUrl         = "https://oapi.dingtalk.com"
	RobotReqSuffixUrl = "/robot/send"
	ContentType       = "application/json"
)

type client struct {
	Config     Config
	HttpClient *http.Client
}

// Send 发起请求
func (c *client) Send(req *http.Request) (err error, resp []byte) {

	// header添加默认的content-type
	req.Header.Set("Content-Type", ContentType)

	// 请求参数添加access_token
	q := req.URL.Query()
	q.Set("access_token", c.Config.AccessToken)
	req.URL.RawQuery = q.Encode()

	// 发送请求
	response, err := c.HttpClient.Do(req)
	if err != nil {
		return
	}
	defer response.Body.Close()

	err, resp = handleResponse(response)

	return
}

// SendText 钉钉webhook机器人，发送text类型
func (c *client) SendText(params *textParams) (err error) {
	url := ServerUrl + RobotReqSuffixUrl
	paramsJson, err := json.Marshal(params)
	if err != nil {
		return
	}
	req, err := http.NewRequest(http.MethodPost, url, strings.NewReader(string(paramsJson)))
	if err != nil {
		return
	}
	err, _ = c.Send(req)
	return
}

// SendLink 钉钉webhook机器人，发送link类型
func (c *client) SendLink(params *linkParams) (err error) {
	url := ServerUrl + RobotReqSuffixUrl
	paramsJson, err := json.Marshal(params)
	if err != nil {
		return
	}
	req, err := http.NewRequest(http.MethodPost, url, strings.NewReader(string(paramsJson)))
	if err != nil {
		return
	}
	err, _ = c.Send(req)
	return
}

// SendMarkdown 钉钉webhook机器人，发送markdown类型
func (c *client) SendMarkdown(params *markdownParams) (err error) {
	url := ServerUrl + RobotReqSuffixUrl
	paramsJson, err := json.Marshal(params)
	if err != nil {
		return
	}
	req, err := http.NewRequest(http.MethodPost, url, strings.NewReader(string(paramsJson)))
	if err != nil {
		return
	}
	err, _ = c.Send(req)
	return
}

// SendActionCard 钉钉webhook机器人，发送整体跳转ActionCard类型
func (c *client) SendActionCard(params *actionCardParams) (err error) {
	url := ServerUrl + RobotReqSuffixUrl
	paramsJson, err := json.Marshal(params)
	if err != nil {
		return
	}
	req, err := http.NewRequest(http.MethodPost, url, strings.NewReader(string(paramsJson)))
	if err != nil {
		return
	}
	err, _ = c.Send(req)
	return
}

// SendCusActionCard 钉钉webhook机器人，发送独立跳转ActionCard类型
func (c *client) SendCusActionCard(params *cusActionCardParams) (err error) {
	url := ServerUrl + RobotReqSuffixUrl
	paramsJson, err := json.Marshal(params)
	if err != nil {
		return
	}
	req, err := http.NewRequest(http.MethodPost, url, strings.NewReader(string(paramsJson)))
	if err != nil {
		return
	}
	err, _ = c.Send(req)
	return
}

// SendFeedCard 钉钉webhook机器人，发送feedCard类型
func (c *client) SendFeedCard(params *feedCardParams) (err error) {
	url := ServerUrl + RobotReqSuffixUrl
	paramsJson, err := json.Marshal(params)
	if err != nil {
		return
	}
	req, err := http.NewRequest(http.MethodPost, url, strings.NewReader(string(paramsJson)))
	if err != nil {
		return
	}
	err, _ = c.Send(req)
	return
}

// handleResponse 处理钉钉的接口响应
func handleResponse(response *http.Response) (err error, resp []byte) {

	if response.StatusCode != http.StatusOK {
		err = errors.New("钉钉服务器响应异常: " + response.Status)
		return
	}

	resp, err = ioutil.ReadAll(response.Body)
	if err != nil {
		return
	}

	if response.Header.Get("Content-Type") == ContentType {
		errorRes := struct {
			Errcode int64  `json:"errcode"`
			Errmsg  string `json:"errmsg"`
		}{}
		err = json.Unmarshal(resp, &errorRes)
		if err != nil {
			return
		}

		if errorRes.Errcode == -1 {
			err = errors.New("钉钉服务器繁忙，请稍后再试")
			return
		}

		if errorRes.Errcode != 0 {
			err = errors.New(string(resp))
			return
		}
	}
	return
}

// NewClient 创建客户端
func NewClient(config *Config) (err error, c *client) {
	if config.AccessToken == "" || config.KeyWord == "" {
		err = errors.New("配置错误，AccessToken或Keyword不能为空")
		return
	}
	c = &client{
		Config:     *config,
		HttpClient: http.DefaultClient,
	}
	return
}
