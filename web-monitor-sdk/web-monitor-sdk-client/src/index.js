import WebMonitorSdkCore from '../../web-monitor-sdk-core/lib/web-monitor-sdk-core.min';
import {
    getParamsFromScript
} from './service/domService';
import {
    getByProjectIdentifier
} from './service/projectService';

// 从script标签中获取项目信息
getParamsFromScript(projectIdentifier => {

    getByProjectIdentifier(projectIdentifier, res => {

        const {
            projectIdentifier,
            activeFuncs
        } = res;
        const funcs = activeFuncs.length ? activeFuncs.split(',') : [];
        const checkEnabled = funcName => funcs.indexOf(funcName) > -1;

        const monitor = new WebMonitorSdkCore();
        const config = {
            projectIdentifier: projectIdentifier,
            captureJsError: checkEnabled('jsError'),
            captureResourceError: checkEnabled('ResourceLoadError'),
            captureAjaxError: checkEnabled('httpError'),
            captureConsoleError: checkEnabled('customError'),
            isAutoUpload: true, // if true, monitor will call errorHandler automatically
            isEnableBuffer: false, // if true, monitor will create a buffer pool and save the concurrency info
            bufferCapacity: 10, // the capacity of buffer pool
            errorHandler: (data) => {
                // something to do with data
                console.log('[log]web-monitor-sdk', data);
            }
        };
        monitor.init(config);
        console.log('[log]web-monitor-sdk', '开启成功');
    });
});