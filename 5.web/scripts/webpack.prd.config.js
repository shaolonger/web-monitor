const pkg = require('../package.json');
const path = require('path');
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {
    mode: 'production',
    entry: {
        app: path.resolve(__dirname, '../src/index.js'),
        vendor: Object.keys(pkg.dependencies) // 将第三方依赖单独打包
    },
    output: {
        path: path.resolve(__dirname, '../dist/'),
        filename: '[name].[chunkhash:8].js'
    },
    resolve: {
        alias: {
            static: path.resolve(__dirname, '../src/static/'),
            service: path.resolve(__dirname, '../src/service/'),
            const: path.resolve(__dirname, '../src/const/'),
        }
    },
    module: {
        rules: [
            {
                test: /\.(js|jsx)$/,
                exclude: /node_modules/,
                use: {
                    loader: "babel-loader"
                }
            },
            {
                test: /\.s[ac]ss$/i,
                use: ['style-loader', 'css-loader', 'sass-loader'],
            },
            {
                test: /\.css$/i,
                use: ['style-loader', 'css-loader'],
            },
            {
                test: /\.(png|jpg|gif)$/,
                use: {
                    loader: 'url-loader',
                    options: {
                        limit: 8192
                    }
                }
            },
        ]
    },
    plugins: [
        // html模板插件
        new HtmlWebpackPlugin({
            template: path.resolve(__dirname, '../src/index.html')
        }),
    ],
    node: {
        fs: 'empty' // 避免打包过程中出现Can't resolve 'fs' when bundle with webpack的报错
    },
    optimization: {
        splitChunks: {
            chunks: 'all'
        }
    }
};