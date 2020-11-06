// rollup.config.js
import resolve from '@rollup/plugin-node-resolve';
import babel from '@rollup/plugin-babel';
import {
    terser
} from 'rollup-plugin-terser';

export default {
    input: 'src/index.js',
    output: [{
        file: 'lib/web-monitor-sdk-core.js',
        format: 'umd',
        name: 'webMonitorSdkCore'
    }, {
        file: 'lib/web-monitor-sdk-core.min.js',
        format: 'umd',
        name: 'webMonitorSdkCore',
        plugins: [terser()]
    }],
    plugins: [
        resolve(),
        babel({
            babelHelpers: 'bundled'
        })
    ]
};