(function (global, factory) {
  typeof exports === 'object' && typeof module !== 'undefined' ? module.exports = factory() :
  typeof define === 'function' && define.amd ? define(factory) :
  (global = typeof globalThis !== 'undefined' ? globalThis : global || self, global.webMonitorSdkCore = factory());
}(this, (function () { 'use strict';

  function _typeof(obj) {
    "@babel/helpers - typeof";

    if (typeof Symbol === "function" && typeof Symbol.iterator === "symbol") {
      _typeof = function (obj) {
        return typeof obj;
      };
    } else {
      _typeof = function (obj) {
        return obj && typeof Symbol === "function" && obj.constructor === Symbol && obj !== Symbol.prototype ? "symbol" : typeof obj;
      };
    }

    return _typeof(obj);
  }

  function _classCallCheck(instance, Constructor) {
    if (!(instance instanceof Constructor)) {
      throw new TypeError("Cannot call a class as a function");
    }
  }

  function _defineProperties(target, props) {
    for (var i = 0; i < props.length; i++) {
      var descriptor = props[i];
      descriptor.enumerable = descriptor.enumerable || false;
      descriptor.configurable = true;
      if ("value" in descriptor) descriptor.writable = true;
      Object.defineProperty(target, descriptor.key, descriptor);
    }
  }

  function _createClass(Constructor, protoProps, staticProps) {
    if (protoProps) _defineProperties(Constructor.prototype, protoProps);
    if (staticProps) _defineProperties(Constructor, staticProps);
    return Constructor;
  }

  function _defineProperty(obj, key, value) {
    if (key in obj) {
      Object.defineProperty(obj, key, {
        value: value,
        enumerable: true,
        configurable: true,
        writable: true
      });
    } else {
      obj[key] = value;
    }

    return obj;
  }

  function ownKeys(object, enumerableOnly) {
    var keys = Object.keys(object);

    if (Object.getOwnPropertySymbols) {
      var symbols = Object.getOwnPropertySymbols(object);
      if (enumerableOnly) symbols = symbols.filter(function (sym) {
        return Object.getOwnPropertyDescriptor(object, sym).enumerable;
      });
      keys.push.apply(keys, symbols);
    }

    return keys;
  }

  function _objectSpread2(target) {
    for (var i = 1; i < arguments.length; i++) {
      var source = arguments[i] != null ? arguments[i] : {};

      if (i % 2) {
        ownKeys(Object(source), true).forEach(function (key) {
          _defineProperty(target, key, source[key]);
        });
      } else if (Object.getOwnPropertyDescriptors) {
        Object.defineProperties(target, Object.getOwnPropertyDescriptors(source));
      } else {
        ownKeys(Object(source)).forEach(function (key) {
          Object.defineProperty(target, key, Object.getOwnPropertyDescriptor(source, key));
        });
      }
    }

    return target;
  }

  function _inherits(subClass, superClass) {
    if (typeof superClass !== "function" && superClass !== null) {
      throw new TypeError("Super expression must either be null or a function");
    }

    subClass.prototype = Object.create(superClass && superClass.prototype, {
      constructor: {
        value: subClass,
        writable: true,
        configurable: true
      }
    });
    if (superClass) _setPrototypeOf(subClass, superClass);
  }

  function _getPrototypeOf(o) {
    _getPrototypeOf = Object.setPrototypeOf ? Object.getPrototypeOf : function _getPrototypeOf(o) {
      return o.__proto__ || Object.getPrototypeOf(o);
    };
    return _getPrototypeOf(o);
  }

  function _setPrototypeOf(o, p) {
    _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) {
      o.__proto__ = p;
      return o;
    };

    return _setPrototypeOf(o, p);
  }

  function _isNativeReflectConstruct() {
    if (typeof Reflect === "undefined" || !Reflect.construct) return false;
    if (Reflect.construct.sham) return false;
    if (typeof Proxy === "function") return true;

    try {
      Date.prototype.toString.call(Reflect.construct(Date, [], function () {}));
      return true;
    } catch (e) {
      return false;
    }
  }

  function _assertThisInitialized(self) {
    if (self === void 0) {
      throw new ReferenceError("this hasn't been initialised - super() hasn't been called");
    }

    return self;
  }

  function _possibleConstructorReturn(self, call) {
    if (call && (typeof call === "object" || typeof call === "function")) {
      return call;
    }

    return _assertThisInitialized(self);
  }

  function _createSuper(Derived) {
    var hasNativeReflectConstruct = _isNativeReflectConstruct();

    return function _createSuperInternal() {
      var Super = _getPrototypeOf(Derived),
          result;

      if (hasNativeReflectConstruct) {
        var NewTarget = _getPrototypeOf(this).constructor;

        result = Reflect.construct(Super, arguments, NewTarget);
      } else {
        result = Super.apply(this, arguments);
      }

      return _possibleConstructorReturn(this, result);
    };
  }

  /**
   * Error levels
   */
  var ERROR_LEVEL = {
    'INFO': 'info',
    'WARN': 'warn',
    'ERROR': 'error'
  };
  /**
   * Error types
   */

  var ERROR_TYPE = {
    'JS_ERROR': 'JS_ERROR',
    'RESOURCE_LOAD_ERROR': 'RESOURCE_LOAD_ERROR',
    'HTTP_ERROR': 'HTTP_ERROR',
    'CUSTOM_ERROR': 'CUSTOM_ERROR'
  };
  var CONNECTION_TYPE = {
    NONE: 'none',
    BLUETOOTH: 'bluetooth',
    CELLULAR: 'cellular',
    ETHERNET: 'ethernet',
    MIXED: 'mixed',
    UNKNOWN: 'unknown',
    WIFI: 'wifi',
    WIMAX: 'wimax',
    OTHER: 'other'
  };
  var EFFECTIVE_TYPE = {
    '2G': '2g',
    '3G': '3g',
    '4G': '4g',
    'SLOW2G': 'slow-2g'
  };
  var DEFAULT_BUFFER_CAPACITY = 10;

  var getDeviceInfo = function getDeviceInfo() {
    var device = {};
    var ua = navigator.userAgent;
    var android = ua.match(/(Android);?[\s\/]+([\d.]+)?/);
    var iPad = ua.match(/(iPad).*OS\s([\d_]+)/);
    var iPod = ua.match(/(iPod)(.*OS\s([\d_]+))?/);
    var iphone = !iPad && ua.match(/(iPhone\sOS)\s([\d_]+)/);
    var mobileInfo = ua.match(/Android\s[\S\s]+Build\//);
    device.ios = device.android = device.iphone = device.iPad = device.androidChrome = false;
    device.isWeixin = /MicroMessenger/i.test(ua);
    device.os = 'web';
    device.deviceName = 'PC'; // Android

    if (android) {
      device.os = 'android';
      device.osVersion = android[2];
      device.android = true;
      device.androidChrome = ua.toLowerCase().indexOf('chrome') >= 0;
    }

    if (iPad || iphone || iPod) {
      device.os = 'ios';
      device.ios = true;
    } // iOS


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
    } // iOS 8+ changed UA


    if (device.ios && device.osVersion && ua.indexOf('Version/') >= 0) {
      if (device.osVersion.split('.')[0] === '10') {
        device.osVersion = ua.toLowerCase().split('version/')[1].split(' ')[0];
      }
    } // if is ios, then set deviceName as 'iphone', and separate with ratio


    if (device.iphone) {
      device.deviceName = 'iphone';
      var screenWidth = window.screen.width;
      var screenHeight = window.screen.height;

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
      var info = mobileInfo[0];
      var deviceName = info.split(';')[1].replace(/Build\//g, '');
      device.deviceName = deviceName.replace(/(^\s*)|(\s*$)/g, '');
    }

    if (ua.indexOf('Mobile') === -1) {
      var agent = navigator.userAgent.toLowerCase();
      var regStr_ie = /msie [\d.]+;/gi;
      var regStr_ff = /firefox\/[\d.]+/gi;
      var regStr_chrome = /chrome\/[\d.]+/gi;
      var regStr_saf = /safari\/[\d.]+/gi;
      device.browserName = 'Unknown'; // IE

      if (agent.indexOf('msie') > 0) {
        var browserInfo = agent.match(regStr_ie)[0];
        device.browserName = browserInfo.split('/')[0];
        device.browserVersion = browserInfo.split('/')[1];
      } // firefox


      if (agent.indexOf('firefox') > 0) {
        var _browserInfo = agent.match(regStr_ff)[0];
        device.browserName = _browserInfo.split('/')[0];
        device.browserVersion = _browserInfo.split('/')[1];
      } // Safari


      if (agent.indexOf('safari') > 0 && agent.indexOf('chrome') < 0) {
        var _browserInfo2 = agent.match(regStr_saf)[0];
        device.browserName = _browserInfo2.split('/')[0];
        device.browserVersion = _browserInfo2.split('/')[1];
      } // Chrome


      if (agent.indexOf('chrome') > 0) {
        var _browserInfo3 = agent.match(regStr_chrome)[0];
        device.browserName = _browserInfo3.split('/')[0];
        device.browserVersion = _browserInfo3.split('/')[1];
      }
    }

    device.webView = (iphone || iPad || iPod) && ua.match(/.*AppleWebKit(?!.*Safari)/i);
    return device;
  };

  var getLocationInfo = function getLocationInfo() {
    var pageUrl = '';
    var pageKey = '';

    if (window && window.location) {
      var _window$location = window.location,
          href = _window$location.href,
          hash = _window$location.hash,
          pathname = _window$location.pathname;
      pageUrl = href;
      pageKey = hash || pathname;
    }

    return {
      pageUrl: pageUrl,
      pageKey: pageKey
    };
  };

  var getCurrentTime = function getCurrentTime() {
    var getTwoBit = function getTwoBit(number) {
      return ('00' + number).substr(-2);
    };

    var now = new Date();
    var year = now.getFullYear();
    var month = getTwoBit(now.getMonth() + 1);
    var day = getTwoBit(now.getDate());
    var hour = getTwoBit(now.getHours());
    var minute = getTwoBit(now.getMinutes());
    var second = getTwoBit(now.getSeconds());
    return "".concat(year, "-").concat(month, "-").concat(day, " ").concat(hour, ":").concat(minute, ":").concat(second);
  };

  var getNetworkType = function getNetworkType() {
    var networkInformation = navigator.connection;
    var netType = '';

    if (networkInformation && networkInformation.type) {
      var type = networkInformation.type;

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

  var setIpInfo = function setIpInfo() {
    var script = document.createElement('script');
    script.type = 'text/javascript';
    script.src = 'http://pv.sohu.com/cityjson?ie=utf-8';

    script.onload = function () {
      script.parentNode.removeChild(script);
      script = null;
    };

    document.body.append(script);
  };

  var getIpInfo = function getIpInfo() {
    return window.returnCitySN || {};
  };

  /**
   * The default config
   */
  var DEFAULT_CONFIG = {
    projectIdentifier: '',
    captureJsError: true,
    captureResourceError: true,
    captureAjaxError: true,
    captureConsoleError: false,
    isAutoUpload: true // If true, WebMonitorSdkCore will automatically call callback function

  };

  var BufferPool = /*#__PURE__*/function () {
    function BufferPool(capacity) {
      _classCallCheck(this, BufferPool);

      if (typeof capacity !== 'number') {
        throw new Error('[error]WebMonitorSdkCore->BufferPool: capacity is not legal!');
      }

      if (capacity < 1) {
        throw new Error('[error]WebMonitorSdkCore->BufferPool: capacity should not less than 0!');
      }

      this.pool = [];
      this.capacity = capacity;
    }
    /**
     * Push item to pool
     * [Attention]Subclasses should override this method
     * @param {Object} item 
     */


    _createClass(BufferPool, [{
      key: "push",
      value: function push(item) {}
      /**
       * Do something with item in pool
       * [Attention]Subclasses should override this method
       * @param {Object} item 
       */

    }, {
      key: "pop",
      value: function pop(item) {}
      /**
       * Flush the pool
       */

    }, {
      key: "flush",
      value: function flush() {
        var _this = this;

        this.pool.forEach(function (item) {
          _this.pop(item);
        });
        this.clear();
      }
      /**
       * Clear the pool
       */

    }, {
      key: "clear",
      value: function clear() {
        this.pool = [];
      }
    }]);

    return BufferPool;
  }();

  /**
   * Get diff key by logType
   * @param {String} logType 
   */

  var getDiffKeyByLogType = function getDiffKeyByLogType(logType) {
    var logTypeKeyMap = {
      'JS_ERROR': 'errorMessage',
      'RESOURCE_LOAD_ERROR': 'resourceUrl',
      'HTTP_ERROR': 'httpUrlComplete',
      'CUSTOM_ERROR': 'errorMessage'
    };
    return logTypeKeyMap[logType];
  };

  var LogBufferPool = /*#__PURE__*/function (_BufferPool) {
    _inherits(LogBufferPool, _BufferPool);

    var _super = _createSuper(LogBufferPool);

    function LogBufferPool(capacity, handleMehod) {
      var _this;

      _classCallCheck(this, LogBufferPool);

      _this = _super.call(this, capacity);
      _this.handleMehod = handleMehod;
      return _this;
    }
    /**
     * Override
     * @param {Object} item 
     */


    _createClass(LogBufferPool, [{
      key: "push",
      value: function push(item) {
        if (this.pool.length < this.capacity) {
          var key = getDiffKeyByLogType(item.logType);
          var oldItem = this.pool.find(function (oldItem) {
            return oldItem[key] === item[key];
          });

          if (oldItem) {
            oldItem.concurrency++;
          } else {
            item.concurrency = 1;
            this.pool.push(item);
          }
        } else {
          this.flush();
        }
      }
      /**
       * Override
       * @param {Object} item 
       */

    }, {
      key: "pop",
      value: function pop(item) {
        typeof this.handleMehod === 'function' && this.handleMehod(item);
      }
    }]);

    return LogBufferPool;
  }(BufferPool);

  var DEVICE_INFO = getDeviceInfo();
  /**
   * Get basic info of Log
   */

  var getLogBasicInfo = function getLogBasicInfo() {
    var ipInfo = getIpInfo();
    var basicInfo = {
      happenTime: getCurrentTime(),
      deviceName: DEVICE_INFO.deviceName,
      os: DEVICE_INFO.os,
      osVersion: DEVICE_INFO.osVersion,
      browserName: DEVICE_INFO.browserName,
      browserVersion: DEVICE_INFO.browserVersion,
      netType: getNetworkType(),
      ipAddress: ipInfo.cip,
      address: ipInfo.cname
    };
    var locationInfo = getLocationInfo();
    return _objectSpread2(_objectSpread2({}, basicInfo), locationInfo);
  };
  /**
   * Get error message and stack
   * @param {String} projectIdentifier 
   * @param {String} errorType 
   * @param {String} errorMessage 
   * @param {String} errorStack 
   */


  var getErrorMessageAndStack = function getErrorMessageAndStack(projectIdentifier, errorType, errorMessage, errorStack) {
    return _objectSpread2(_objectSpread2({}, getLogBasicInfo()), {}, {
      projectIdentifier: projectIdentifier,
      logType: ERROR_TYPE.JS_ERROR,
      errorType: errorType,
      errorMessage: errorMessage,
      errorStack: errorStack,
      level: ERROR_LEVEL.ERROR
    });
  };

  var WebMonitorSdkCore = /*#__PURE__*/function () {
    function WebMonitorSdkCore() {
      _classCallCheck(this, WebMonitorSdkCore);

      setIpInfo();
    }
    /**
     * The init method
     * @param {Object} config 
     */


    _createClass(WebMonitorSdkCore, [{
      key: "init",
      value: function init(config) {
        this.config = _objectSpread2(_objectSpread2({}, DEFAULT_CONFIG), config);

        if (this.config.isEnableBuffer) {
          var capacity = this.config.bufferCapacity || DEFAULT_BUFFER_CAPACITY;
          this.bufferPool = new LogBufferPool(capacity, this.config.errorHandler);
        }

        if (this.config.captureJsError) {
          this.handleJsError(this.config);
          this.handlePromiseRejectError(this.config);
        }

        if (this.config.captureResourceError) {
          this.handleResourceError(this.config);
        }

        if (this.config.captureAjaxError) {
          this.handleAjaxError(this.config);
        }

        if (this.config.captureConsoleError) {
          this.handleConsoleError(this.config);
        }
      }
      /**
       * Handle JS errors
       * @param {Object} config 
       */

    }, {
      key: "handleJsError",
      value: function handleJsError(config) {
        var __this = this;

        window.onerror = function (message, source, lineno, colno, error) {
          var errorType = '';
          var errorMessage = '';
          var errorStack = '';

          if (error && error instanceof Error) {
            errorType = error.name || '';
            errorMessage = error.message || message || '';
            errorStack = error.stack || '';
          } else {
            errorType = 'Others';
            errorMessage = message || '';
            errorStack = '';
          }

          var errorInfo = getErrorMessageAndStack(config.projectIdentifier, errorType, errorMessage, errorStack);

          if (config.isEnableBuffer) {
            __this.bufferPool.push(errorInfo);
          } else {
            config.isAutoUpload && config.errorHandler(errorInfo);
          }
        };
      }
      /**
       * Handle Promise errors
       * @param {Object} config 
       */

    }, {
      key: "handlePromiseRejectError",
      value: function handlePromiseRejectError(config) {
        var __this = this;

        window.onunhandledrejection = function (event) {
          var errorType = 'UncaughtInPromiseError';
          var errorMessage = '';
          var errorStack = '';

          if (_typeof(event.reason) === 'object') {
            errorMessage = event.reason.message;
            errorStack = event.reason.stack;
          } else {
            errorMessage = event.reason;
            errorStack = '';
          }

          var errorInfo = getErrorMessageAndStack(config.projectIdentifier, errorType, errorMessage, errorStack);

          if (config.isEnableBuffer) {
            __this.bufferPool.push(errorInfo);
          } else {
            config.isAutoUpload && config.errorHandler(errorInfo);
          }
        };
      }
      /**
       * Handle Resource errors
       * @param {Object} config 
       */

    }, {
      key: "handleResourceError",
      value: function handleResourceError(config) {
        var __this = this;

        window.addEventListener('error', function (event) {
          var target = event.target || event.srcElement;
          var isElementTarget = target instanceof HTMLScriptElement || target instanceof HTMLLinkElement || target instanceof HTMLImageElement;
          if (!isElementTarget) return; // JS errors has been captured by handleJsError method

          var typeName = event.target.localName;
          var resourceUrl = '';

          if (typeName === 'link') {
            resourceUrl = event.target.href;
          } else if (typeName === 'script') {
            resourceUrl = event.target.src;
          } else if (typeName === 'img') {
            resourceUrl = event.target.src;
          }

          var errorInfo = _objectSpread2(_objectSpread2({}, getLogBasicInfo()), {}, {
            projectIdentifier: config.projectIdentifier,
            logType: ERROR_TYPE.RESOURCE_LOAD_ERROR,
            resourceUrl: resourceUrl,
            resourceType: typeName,
            status: '0',
            level: ERROR_LEVEL.ERROR
          });

          if (config.isEnableBuffer) {
            __this.bufferPool.push(errorInfo);
          } else {
            config.isAutoUpload && config.errorHandler(errorInfo);
          }
        }, true);
      }
      /**
       * Handle Ajax errors
       * @param {Object} config 
       */

    }, {
      key: "handleAjaxError",
      value: function handleAjaxError(config) {
        var __this = this; // fetch


        if (window.fetch) {
          var oldFetch = window.fetch;

          window.fetch = function () {
            var _arguments = arguments;
            return oldFetch.apply(this, arguments).then(function (res) {
              if (!res.ok) {
                var errorInfo = _objectSpread2(_objectSpread2({}, getLogBasicInfo()), {}, {
                  projectIdentifier: config.projectIdentifier,
                  logType: ERROR_TYPE.HTTP_ERROR,
                  httpUrlComplete: _arguments[0],
                  httpUrlShort: _arguments[0],
                  status: res,
                  statusText: res,
                  level: ERROR_LEVEL.ERROR
                });

                if (config.isEnableBuffer) {
                  __this.bufferPool.push(errorInfo);
                } else {
                  config.isAutoUpload && config.errorHandler(errorInfo);
                }
              }

              return res;
            })["catch"](function (error) {
              var errorInfo = _objectSpread2(_objectSpread2({}, getLogBasicInfo()), {}, {
                projectIdentifier: config.projectIdentifier,
                logType: ERROR_TYPE.HTTP_ERROR,
                httpUrlComplete: _arguments[0],
                httpUrlShort: _arguments[0],
                status: error.message,
                statusText: error.stack,
                level: ERROR_LEVEL.ERROR
              });

              if (config.isEnableBuffer) {
                __this.bufferPool.push(errorInfo);
              } else {
                config.isAutoUpload && config.errorHandler(errorInfo);
              }
            });
          };
        } // XMLHttpRequest


        if (window.XMLHttpRequest) {
          var xmlhttp = window.XMLHttpRequest;
          var oldOpen = xmlhttp.prototype.open;
          var oldSend = xmlhttp.prototype.send;

          var handleEvent = function handleEvent(event) {
            if (event && event.currentTarget && event.currentTarget.status !== 200) {
              var errorInfo = _objectSpread2(_objectSpread2({}, getLogBasicInfo()), {}, {
                projectIdentifier: config.projectIdentifier,
                logType: ERROR_TYPE.HTTP_ERROR,
                httpUrlComplete: event.target.responseURL || this._url,
                httpUrlShort: event.target.response || this._url,
                status: event.target.status,
                statusText: event.target.statusText,
                level: ERROR_LEVEL.ERROR
              });

              if (config.isEnableBuffer) {
                __this.bufferPool.push(errorInfo);
              } else {
                config.isAutoUpload && config.errorHandler(errorInfo);
              }
            }
          };

          xmlhttp.prototype.open = function (mothod, url) {
            this._url = url;

            for (var _len = arguments.length, args = new Array(_len > 2 ? _len - 2 : 0), _key = 2; _key < _len; _key++) {
              args[_key - 2] = arguments[_key];
            }

            return oldOpen.apply(this, [mothod, url].concat(args));
          };

          xmlhttp.prototype.send = function () {
            if (this['addEventListener']) {
              this['addEventListener']('error', handleEvent);
              this['addEventListener']('load', handleEvent);
              this['addEventListener']('abort', handleEvent);
            } else {
              var oldStateChange = this['onreadystatechange'];

              this['onreadystatechange'] = function (event) {
                if (this.readyState === 4) {
                  handleEvent(event);
                }

                oldStateChange && oldStateChange.apply(this, arguments);
              };
            }

            return oldSend.apply(this, arguments);
          };
        }
      }
      /**
       * Handle console errors
       * @param {Object} config 
       */

    }, {
      key: "handleConsoleError",
      value: function handleConsoleError(config) {
        if (!window.console || !window.console.error) return;
        var oldConsoleError = window.console.error;

        var __this = this;

        window.console.error = function (otherErrorMsg) {
          var errorMessage = arguments[0] && arguments[0].message || otherErrorMsg;
          var errorStack = arguments[0] && arguments[0].stack;

          var errorInfo = _objectSpread2(_objectSpread2({}, getLogBasicInfo()), {}, {
            projectIdentifier: config.projectIdentifier,
            logType: ERROR_TYPE.CUSTOM_ERROR,
            errorType: ERROR_TYPE.CUSTOM_ERROR,
            errorMessage: errorMessage,
            errorStack: errorStack
          });

          if (config.isEnableBuffer) {
            __this.bufferPool.push(errorInfo);
          } else {
            config.isAutoUpload && config.errorHandler(errorInfo);
          }

          return oldConsoleError.apply(window.console, arguments);
        };
      }
      /**
       * Switch isAutoUpload
       * @param {Boolean} isAutoUpload 
       */

    }, {
      key: "setIsAutoUpload",
      value: function setIsAutoUpload(isAutoUpload) {
        this.config.isAutoUpload = !!isAutoUpload;
      }
    }]);

    return WebMonitorSdkCore;
  }();

  return WebMonitorSdkCore;

})));
