class HttpClient {

    constructor(config) {
        this.config = config;
        this.setXmlHttp();
    }

    setXmlHttp() {
        let xmlHttp = null;
        if (window.XMLHttpRequest) {
            xmlHttp = new XMLHttpRequest;
        } else if (window.ActiveXObject) {
            // IE5 and IE6
            xmlHttp = new ActiveXObject('Microsoft.XMLHTTP');
        }
        if (xmlHttp === null) {
            console.error('[error]web-monitor-client: 客户端不支持xmlHttp');
            return;
        }
        this.xmlHttp = xmlHttp;
    }

    fetch(option) {
        return new Promise((resolve, reject) => {
            const xmlHttp = this.xmlHttp;
            const {
                baseUrl = ''
            } = this.config;
            let {
                method = 'GET', url, params = {}, async = true
            } = option;
            method = method.toUpperCase();
            if (method == 'GET') {
                // 处理GET请求URL
                let getUrl = url;
                getUrl += '?';
                for (let key in params) {
                    getUrl += key + '=' + params[key] + '&';
                }
                getUrl = getUrl.substring(0, getUrl.length - 1);
                xmlHttp.open('GET', baseUrl + getUrl, async);
                xmlHttp.send(null);
            }
            if (method == 'POST') {
                xmlHttp.open('POST', baseUrl + url, async);
                xmlHttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                xmlHttp.send(params);
            }

            xmlHttp.onreadystatechange = () => {
                if (xmlHttp.readyState === 4) {
                    const res = JSON.parse(xmlHttp.responseText);
                    if (xmlHttp.status === 200) {
                        if (res) {
                            resolve(res);
                        } else {
                            reject(res);
                        }
                    } else {
                        reject(res);
                    }
                }
            };
        })
    }

    uploadLog(url) {
        (new Image()).src = url;
    }
}

export default HttpClient;