# web-monitor-sdk-core

This is a simple log tool for usage in browser.

Dont like Java or the other language, we lack good log tool in browser environment, which can help us trace errors or problems more efficiently.

So this is what web-monitor-sdk-core try to do.

Welcome to develop together or put an issue.

## Installation

```shell script
npm install web-monitor-sdk-core
```

## Usage
1.create instance
```javascript
// Common JS
var WebMonitorSdkCore = require("web-monitor-sdk-core");
var monitor = new WebMonitorSdkCore();
```

```javascript
// Or in ES Modules
import WebMonitorSdkCore from 'web-monitor-sdk-core';
var monitor = new WebMonitorSdkCore();
```

2.init with your own config
```javascript
var config = {
    projectIdentifier: '',
    captureJsError: true,
    captureResourceError: true,
    captureAjaxError: true,
    captureConsoleError: false,
    isAutoHandle: true, // if true, monitor will call errorHandler automatically
    isEnableBuffer: false, // if true, monitor will create a buffer pool and save the concurrency info
    bufferCapacity: 10, // the capacity of buffer pool
    errorHandler: function (data) {
        // something to do with data
    }
};
monitor.init(config);
```

3.(optional)switch errorHandler automation
```javascript
monitor.setIsAutoHandle(false); // stop
monitor.setIsAutoHandle(true); // restart
```

## License
web-monitor-sdk-core is under the MIT License.