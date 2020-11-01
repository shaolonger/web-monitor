// rollup.config.js
import resolve from '@rollup/plugin-node-resolve';
import babel from '@rollup/plugin-babel';
import {
    terser
} from 'rollup-plugin-terser';

export default {
    input: 'src/index.js',
    output: [{
        file: 'lib/log4browser.js',
        format: 'umd',
        name: 'Log4Browser'
    }, {
        file: 'lib/log4browser.min.js',
        format: 'umd',
        name: 'Log4Browser',
        plugins: [terser()]
    }],
    plugins: [
        resolve(),
        babel({
            babelHelpers: 'bundled'
        })
    ]
};