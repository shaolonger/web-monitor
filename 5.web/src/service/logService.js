import axios from './basicConfig';
import qs from 'qs';

const LogService = {
    // 获取总览页信息
    getOverallByTimeRange(params) {
        return axios.get('/statistic/getOverallByTimeRange?' + qs.stringify(params));
    },
    // 按小时间隔，获取各小时内的日志数量
    getLogCountByHours(params) {
        return axios.get('/statistic/getLogCountByHours?' + qs.stringify(params));
    },
    // 按天间隔，获取各日期内的日志数量
    getLogCountByDays(params) {
        return axios.get('/statistic/getLogCountByDays?' + qs.stringify(params));
    },
    // js异常日志-条件查询
    getJsErrorLog(params) {
        return axios.get('/jsErrorLog/get?' + qs.stringify(params));
    },
    // http异常日志-条件查询
    getHttpErrorLog(params) {
        return axios.get('/httpErrorLog/get?' + qs.stringify(params));
    },
    // http异常日志-按status分类获取日志数量
    getHttpErrorLogCountByStatus(params) {
        return axios.get('/httpErrorLog/getLogCountByState?' + qs.stringify(params));
    },
    // 自定义异常日志-条件查询
    getCustomErrorLog(params) {
        return axios.get('/customErrorLog/get?' + qs.stringify(params));
    },
};

export default LogService;