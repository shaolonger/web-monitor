import httpClient, {
    getUrlByQueryParams
} from '../utils/http';

const addLogClient = formData => {
    httpClient.fetch({
        method: 'GET',
        url: '/log/client/add',
        params: formData
    });
};

const uploadLog = logData => {
    const url = getUrlByQueryParams('/log/add', logData);
    httpClient.uploadLog(url);
};

export {
    addLogClient,
    uploadLog
};
