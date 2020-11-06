import {
    v4 as uuidv4
} from 'uuid';

const setUuid = () => {
    const uuid = uuidv4();
    window.localStorage.setItem('web-monitor-sdk-client__uuid', uuid);
    return uuid;
};

const getUuid = () => {
    let uuid = window.localStorage.getItem('web-monitor-sdk-client__uuid');
    if (!uuid) {
        uuid = setUuid();
    }
    return uuid;
};

export {
    getUuid
};