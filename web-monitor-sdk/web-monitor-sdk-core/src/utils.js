import {
    CONNECTION_TYPE,
    EFFECTIVE_TYPE
} from './const';

const getDeviceInfo = () => {
    const device = {};
    const ua = navigator.userAgent;
    const android = ua.match(/(Android);?[\s\/]+([\d.]+)?/);
    const iPad = ua.match(/(iPad).*OS\s([\d_]+)/);
    const iPod = ua.match(/(iPod)(.*OS\s([\d_]+))?/);
    const iphone = !iPad && ua.match(/(iPhone\sOS)\s([\d_]+)/);
    const mobileInfo = ua.match(/Android\s[\S\s]+Build\//);
    device.ios = device.android = device.iphone = device.iPad = device.androidChrome = false;
    device.isWeixin = /MicroMessenger/i.test(ua);
    device.os = 'web';
    device.deviceName = 'PC';
    // Android
    if (android) {
        device.os = 'android';
        device.osVersion = android[2];
        device.android = true;
        device.androidChrome = ua.toLowerCase().indexOf('chrome') >= 0;
    }
    if (iPad || iphone || iPod) {
        device.os = 'ios';
        device.ios = true;
    }
    // iOS
    if (iphone && !iPod) {
        device.osVersion = iphone[2].replace(/_/g, '.');
        device.iphone = true;
    }
    if (iPad) {
        device.osVersion = iPad[2].replace(/_/g, '.');
        device.iPad = true;
    }
    if (iPod) {
        device.osVersion = iPod[3] ? iPod[3].replace(/_/g, '.') : null;
        device.iphone = true;
    }
    // iOS 8+ changed UA
    if (device.ios && device.osVersion && ua.indexOf('Version/') >= 0) {
        if (device.osVersion.split('.')[0] === '10') {
            device.osVersion = ua.toLowerCase().split('version/')[1].split(' ')[0];
        }
    }
    // if is ios, then set deviceName as 'iphone', and separate with ratio
    if (device.iphone) {
        device.deviceName = 'iphone';
        const screenWidth = window.screen.width;
        const screenHeight = window.screen.height;
        if (screenWidth === 320 && screenHeight === 480) {
            device.deviceName = 'iphone 4';
        } else if (screenWidth === 320 && screenHeight === 568) {
            device.deviceName = 'iphone 5/SE';
        } else if (screenWidth === 375 && screenHeight === 667) {
            device.deviceName = 'iphone 6/7/8';
        } else if (screenWidth === 414 && screenHeight === 736) {
            device.deviceName = 'iphone 6/7/8 Plus';
        } else if (screenWidth === 375 && screenHeight === 812) {
            device.deviceName = 'iphone X/S/Max';
        }
    } else if (device.iPad) {
        device.deviceName = 'iPad';
    } else if (mobileInfo) {
        const info = mobileInfo[0];
        const deviceName = info.split(';')[1].replace(/Build\//g, '');
        device.deviceName = deviceName.replace(/(^\s*)|(\s*$)/g, '');
    }
    if (ua.indexOf('Mobile') === -1) {
        const agent = navigator.userAgent.toLowerCase();
        const regStr_ie = /msie [\d.]+;/gi;
        const regStr_ff = /firefox\/[\d.]+/gi;
        const regStr_chrome = /chrome\/[\d.]+/gi;
        const regStr_saf = /safari\/[\d.]+/gi;

        device.browserName = 'Unknown';

        // IE
        if (agent.indexOf('msie') > 0) {
            const browserInfo = agent.match(regStr_ie)[0];
            device.browserName = browserInfo.split('/')[0];
            device.browserVersion = browserInfo.split('/')[1];
        }

        // firefox
        if (agent.indexOf('firefox') > 0) {
            const browserInfo = agent.match(regStr_ff)[0];
            device.browserName = browserInfo.split('/')[0];
            device.browserVersion = browserInfo.split('/')[1];
        }

        // Safari
        if (agent.indexOf('safari') > 0 && agent.indexOf('chrome') < 0) {
            const browserInfo = agent.match(regStr_saf)[0];
            device.browserName = browserInfo.split('/')[0];
            device.browserVersion = browserInfo.split('/')[1];
        }

        // Chrome
        if (agent.indexOf('chrome') > 0) {
            const browserInfo = agent.match(regStr_chrome)[0];
            device.browserName = browserInfo.split('/')[0];
            device.browserVersion = browserInfo.split('/')[1];
        }
    }
    device.webView = (iphone || iPad || iPod) && ua.match(/.*AppleWebKit(?!.*Safari)/i);
    return device;
};

const getLocationInfo = () => {
    let pageUrl = '';
    let pageKey = '';
    if (window && window.location) {
        const {
            href,
            hash,
            pathname
        } = window.location;
        pageUrl = href;
        pageKey = hash || pathname;
    }
    return {
        pageUrl,
        pageKey
    };
};

const getCurrentTime = () => {
    const getTwoBit = number => ('00' + number).substr(-2);
    const now = new Date();
    const year = now.getFullYear();
    const month = getTwoBit(now.getMonth() + 1);
    const day = getTwoBit(now.getDate());
    const hour = getTwoBit(now.getHours());
    const minute = getTwoBit(now.getMinutes());
    const second = getTwoBit(now.getSeconds());
    return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
};

const getNetworkType = () => {
    const networkInformation = navigator.connection;
    let netType = '';
    if (networkInformation && networkInformation.type) {
        const type = networkInformation.type;
        if (type === CONNECTION_TYPE.NONE) {
            netType = 'disconnected';
        } else if (type === CONNECTION_TYPE.CELLULAR) {
            netType = networkInformation.effectiveType === EFFECTIVE_TYPE.SLOW2G ? '2g' : networkInformation.effectiveType;
        } else {
            netType = type;
        }
    }
    return netType;
};

const setIpInfo = () => {
    let script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = 'http://pv.sohu.com/cityjson?ie=utf-8'
    script.onload = () => {
        script.parentNode.removeChild(script);
        script = null;
    };
    document.body.appendChild(script);
};

const getIpInfo = () => {
    return window.returnCitySN || {};
};

export {
    getDeviceInfo,
    getLocationInfo,
    getCurrentTime,
    getNetworkType,
    setIpInfo,
    getIpInfo
};