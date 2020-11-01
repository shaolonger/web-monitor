import axios from 'axios';
import config from "../config";
import {Modal} from "antd";
import {TOKEN_EXPIRE_TEXT} from 'const/BasicConfigConst';
import useUserInfo from "../state/useUserInfo";
import {createHashHistory} from 'history';

const [userInfo] = useUserInfo();

// 请求的基本域名
let baseURL;
if (process.env.NODE_ENV === 'development') {
    baseURL = config.devBasicUrl;
} else {
    baseURL = config.prodBasicUrl;
}

// 免token校验的接口
const authIgnoreList = [
    '/user/login', '/userRegisterRecord/add'
];

/**
 * 统一的错误回调
 * @param status
 * @param message
 */
const errorHandler = (status, message) => {
    // 状态码判断
    switch (status) {
        // 401
        case 401:
            break;
        // 403 无权访问
        case 403:
            break;
        // 404 请求不存在
        case 404:
            break;
        default:
            console.log(message);
    }
};

// 基本配置
axios.defaults.baseURL = baseURL;
axios.defaults.timeout = 10000;

// 请求截器
axios.interceptors.request.use(
    config => {
        if (userInfo.hasLogin && userInfo.token) {
            config.headers.token = userInfo.token;
            return config;
        } else if (authIgnoreList.indexOf(config.url) > -1) {
            return config;
        } else {
            const history = createHashHistory();
            Modal.warning({
                title: '提示',
                content: '登录已失效，请重新登录',
                okText: '确定',
                onOk: () => {
                    history.push('/login');
                }
            });
            return Promise.reject(config);
        }
    },
    error => Promise.reject(error)
);

// 响应拦截器
axios.interceptors.response.use(
    res => {
        if (res.status === 200) {
            if (res.data.msg === TOKEN_EXPIRE_TEXT) {
                const history = createHashHistory();
                Modal.warning({
                    title: '提示',
                    content: '登录已失效，请重新登录',
                    okText: '确定',
                    onOk: () => {
                        history.push('/login');
                    }
                });
                return Promise.reject(res.data);
            } else {
                return Promise.resolve(res.data);
            }
        } else {
            return Promise.reject(res.data);
        }
    },
    error => {
        const {response} = error;
        if (response) {
            // 请求已发出，但是不在2xx的范围
            errorHandler(response.status, response.data.message);
            return Promise.reject(response.data);
        } else {
            // 处理断网的情况
            return Promise.reject(error);
        }
    }
);

export default axios;