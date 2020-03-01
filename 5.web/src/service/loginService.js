import axios from './basicConfig';
import qs from 'qs';

const LoginService = {
    // 登录
    login(params) {
        return axios.post('/user/login', qs.stringify(params));
    },
};

export default LoginService;