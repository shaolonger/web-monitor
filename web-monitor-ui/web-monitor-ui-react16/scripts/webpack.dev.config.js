const path = require('path');
const HtmlWebPackPlugin = require("html-webpack-plugin");

module.exports = {
    mode: 'development',
    entry: path.resolve(__dirname, '../src/index.js'),
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
                test: /\.html$/,
                use: {
                    loader: "html-loader"
                }
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
        new HtmlWebPackPlugin({
            title: 'web-monitor-ui',
            filename: 'index.html',
            template: path.resolve(__dirname, '../src/index.html')
        })
    ],
    devServer: {
        contentBase: path.resolve(__dirname, '../build'),
        port: 5000,
        hot: true,
        proxy: {
            '/api': {
                target: 'http://localhost:6001',
                pathRewrite: {'^/api' : ''}
            }
        }
    },
    devtool: "source-map"
};