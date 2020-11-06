/**
 * Error levels
 */
const ERROR_LEVEL = {
    'INFO': 'info',
    'WARN': 'warn',
    'ERROR': 'error'
};

/**
 * Error types
 */
const ERROR_TYPE = {
    'JS_ERROR': 'JS_ERROR',
    'RESOURCE_LOAD_ERROR': 'RESOURCE_LOAD_ERROR',
    'HTTP_ERROR': 'HTTP_ERROR',
    'CUSTOM_ERROR': 'CUSTOM_ERROR'
};

const CONNECTION_TYPE = {
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

const EFFECTIVE_TYPE = {
    '2G': '2g',
    '3G': '3g',
    '4G': '4g',
    'SLOW2G': 'slow-2g'
};

const DEFAULT_BUFFER_CAPACITY = 10;

export {
    ERROR_LEVEL,
    ERROR_TYPE,
    CONNECTION_TYPE,
    EFFECTIVE_TYPE,
    DEFAULT_BUFFER_CAPACITY
};