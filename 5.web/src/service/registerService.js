import axios from './basicConfig';
import qs from 'qs';

const RegisterService = {
    // 注册
    register(params) {
        return axios.post('/user/register', qs.stringify(params));
    },
    // 注册
    register(params) {
        return axios.put('/userRegisterRecord/add', qs.stringify(params));
    },
};

export default RegisterService;