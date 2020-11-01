import BufferPool from './BufferPool';

/**
 * Get diff key by logType
 * @param {String} logType 
 */
const getDiffKeyByLogType = logType => {
    const logTypeKeyMap = {
        'JS_ERROR': 'errorMessage',
        'RESOURCE_LOAD_ERROR': 'resourceUrl',
        'HTTP_ERROR': 'httpUrlComplete',
        'CUSTOM_ERROR': 'errorMessage'
    };
    return logTypeKeyMap[logType];
}

class LogBufferPool extends BufferPool {
    constructor(capacity, handleMehod) {
        super(capacity);
        this.handleMehod = handleMehod;
    }

    /**
     * Override
     * @param {Object} item 
     */
    push(item) {
        if (this.pool.length < this.capacity) {
            const key = getDiffKeyByLogType(item.logType);
            const oldItem = this.pool.find(oldItem => oldItem[key] === item[key]);
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
    pop(item) {
        typeof this.handleMehod === 'function' && this.handleMehod(item);
    }
}

export default LogBufferPool;