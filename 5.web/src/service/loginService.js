import axios from './basicConfig';
import qs from 'qs';

const LoginService = {
    // 注册
    register(params) {
        return axios.post('/user/register', qs.stringify(params));
    },

    // 登录
    login(params) {
        return axios.post('/user/login', qs.stringify(params));
    },
};

export default LoginService;