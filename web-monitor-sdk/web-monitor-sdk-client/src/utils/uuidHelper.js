import {
    v4 as uuidv4
} from 'uuid';
import {
    addLogClient
} from '../service/monitorService';

const setUuid = () => {
    const uuid = uuidv4();
    window.localStorage.setItem('web-monitor-sdk-client__uuid', uuid);
    return uuid;
};

const getUuid = () => {
    let uuid = window.localStorage.getItem('web-monitor-sdk-client__uuid');
    if (!uuid) {
        uuid = setUuid();

        // 若为首次初始化，则记录日志客户端cUuid
        addLogClient({cUuid: uuid});
    }
    return uuid;
};

export {
    getUuid
};
