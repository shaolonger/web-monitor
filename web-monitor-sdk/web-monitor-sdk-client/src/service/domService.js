/**
 * 从script标签中获取项目信息
 * @param {Function} success 
 */
const getParamsFromScript = (success) => {
    const scriptDom = document.getElementById('web-monitor-sdk');
    if (!scriptDom) {
        return console.error('[error]web-monitor-sdk: 无法找到script标签！');
    }
    const url = scriptDom.getAttribute('src');
    const paramsArr = url.split('?');
    if (paramsArr.length < 2) {
        return console.error('[error]web-monitor-sdk: script标签缺少参数！');
    }

    // 获取参数
    const pidItem = paramsArr[1]
        .split('&')
        .map(item => {
            const temp = item.split('=');
            if (temp.length > 1) {
                return {
                    [temp[0]]: temp[1]
                };
            }
        })
        .find(item => item['key']);
    if (!pidItem) {
        return console.error('[error]web-monitor-sdk: script标签参数错误！');
    }

    success && typeof success === 'function' && success(pidItem['key']);
};

export {
    getParamsFromScript
};