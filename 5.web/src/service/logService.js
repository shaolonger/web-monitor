import axios from './basicConfig';
import qs from 'qs';

const LogService = {
    // 获取总览页信息
    getOverallByTimeRange(params) {
        return axios.get('/statistic/getOverallByTimeRange?' + qs.stringify(params));
    },
    // 获取日志统计数据
    getLogCountByHours(params) {
        return axios.get('/statistic/getLogCountByHours?' + qs.stringify(params));
    },
};

export default LogService;