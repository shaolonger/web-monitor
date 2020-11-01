import axios from './basicConfig';
import qs from 'qs';

const userService = {
    // 登录
    login(params) {
        return axios.post('/user/login', qs.stringify(params));
    },
    // 获取用户列表
    get(params) {
        return axios.get('/user/get', qs.stringify(params));
    },
    // 根据用户获取关联的项目
    getRelatedProjectList() {
        return axios.get('/user/getRelatedProjectList');
    },
};

export default userService;