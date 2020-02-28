const path = require('path');
const HtmlWebPackPlugin = require("html-webpack-plugin");

module.exports = {
    mode: 'development',
    entry: path.resolve(__dirname, '../src/index.js'),
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
                test: /\.html$/,
                use: {
                    loader: "html-loader"
                }
            },
        ]
    },
    plugins: [
        new HtmlWebPackPlugin({
            title: 'nuall-monitor-platform',
            filename: 'index.html',
            template: path.resolve(__dirname, '../src/index.html')
        })
    ],
    devServer: {
        contentBase: path.resolve(__dirname, '../dist'),
        port: 5000,
        hot: true
    },
};