import httpClient from '../utils/http';

/**
 * 根据projectIdentifier获取项目信息
 * @param {Function} success 
 */
const getByProjectIdentifier = async (projectIdentifier, successCallback) => {
    try {
        const result = await httpClient.fetch({
            method: 'GET',
            url: '/project/getByProjectIdentifier',
            params: {
                projectIdentifier: projectIdentifier
            }
        });
        // console.log('result', result);
        const {
            success,
            data
        } = result;
        if (success) {
            typeof successCallback === 'function' && successCallback(data);
        } else {
            console.error('[error]web-monitor-sdk: getByProjectIdentifier', result);
        }
    } catch (e) {
        console.error('[error]web-monitor-sdk: getByProjectIdentifier', e);
    }
};

export {
    getByProjectIdentifier
};