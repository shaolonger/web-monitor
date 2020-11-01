# log4browser

This is a simple log tool for usage in browser.

Dont like Java or the other language, we lack good log tool in browser environment, which can help us trace errors or problems more efficiently.

So this is what log4browser try to do.

Welcome to develop together or put an issue.

## Installation

```shell script
npm install log4browser
```

## Usage
1.create instance
```javascript
// Common JS
var log4browser = require("log4browser");
var logger = new log4browser();
```

```javascript
// Or in ES Modules
import log4browser from 'log4browser';
var logger = new log4browser();
```

2.init with your own config
```javascript
var config = {
    projectIdentifier: '',
    captureJsError: true,
    captureResourceError: true,
    captureAjaxError: true,
    captureConsoleError: false,
    isAutoHandle: true, // if true, logger will call errorHandler automatically
    isEnableBuffer: false, // if true, logger will create a buffer pool and save the concurrency info
    bufferCapacity: 10, // the capacity of buffer pool
    errorHandler: function (logData) {
        // something to do with logData
    }
};
logger.init(config);
```

3.(optional)switch errorHandler automation
```javascript
logger.setIsAutoHandle(false); // stop
logger.setIsAutoHandle(true); // restart
```

## License
Log4browser is under the MIT License.