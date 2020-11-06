import HttpClient from '../lib/httpClient';
import {
    baseUrl
} from '../config/index';

const httpClient = new HttpClient({
    baseUrl
});

const getUrlByQueryParams = (url, params) => {
    let query = baseUrl + url + '?';
    for (let key in params) {
        query += `${key}=${params[key]}&`;
    }
    return query.substring(0, query.length - 1);
};

export default httpClient;
export {
    getUrlByQueryParams
};