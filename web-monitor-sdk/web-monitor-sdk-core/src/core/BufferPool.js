class BufferPool {
    constructor(capacity) {
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
    push(item) {}

    /**
     * Do something with item in pool
     * [Attention]Subclasses should override this method
     * @param {Object} item 
     */
    pop(item) {}

    /**
     * Flush the pool
     */
    flush() {
        this.pool.forEach(item => {
            this.pop(item);
        });
        this.clear();
    }

    /**
     * Clear the pool
     */
    clear() {
        this.pool = [];
    }
}

export default BufferPool;