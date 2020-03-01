import React from 'react';

// 组件
import {Result} from 'antd';

// css
import './index.scss';

const NotFound = () => {
    return (
        <div className='notFound-container'>
            <Result
                status="404"
                title="404"
                subTitle="抱歉，找不到该页面"
            />
        </div>
    );
};

export default NotFound;