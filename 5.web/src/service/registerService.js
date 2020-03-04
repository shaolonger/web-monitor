import axios from './basicConfig';
import qs from 'qs';

const RegisterService = {
    // 查询用户注册记录
    get(params) {
        return axios.get('/userRegisterRecord/get?' + qs.stringify(params));
    },
    // 注册
    register(params) {
        return axios.put('/userRegisterRecord/add', qs.stringify(params));
    },
};

export default RegisterService;