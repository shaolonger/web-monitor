import axios from './basicConfig';
import qs from 'qs';

const ProjectService = {
    // 新增
    add(params) {
        return axios.put('/project/add', qs.stringify(params));
    },
    // 条件查询
    get(params) {
        return axios.get('/project/get?' + qs.stringify(params));
    },
    // 删除
    delete(params) {
        return axios.delete('/project/delete/' + params);
    },
    // 更新
    update(params) {
        return axios.post('/project/update', qs.stringify(params));
    },
};

export default ProjectService;