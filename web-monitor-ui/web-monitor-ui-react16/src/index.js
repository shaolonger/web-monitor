import React from 'react';
import ReactDOM from 'react-dom';
import {ConfigProvider} from 'antd';
import zhCN from 'antd/es/locale/zh_CN';

// modules
import Router from './router/index';

// css
import './index.scss';

ReactDOM.render(
    <ConfigProvider locale={zhCN}>
        <Router/>
    </ConfigProvider>,
    document.getElementById('rootNmpWeb')
);