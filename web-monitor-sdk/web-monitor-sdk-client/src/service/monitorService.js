import httpClient, {
    getUrlByQueryParams
} from '../utils/http';

const uploadLog = logData => {
    const url = getUrlByQueryParams('/log/add', logData);
    httpClient.uploadLog(url);
};

export {
    uploadLog
};