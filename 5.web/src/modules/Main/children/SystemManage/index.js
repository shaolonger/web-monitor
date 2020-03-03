import React from 'react';
import {DatePicker, Select} from 'antd';

// css
import './index.scss';

const SystemManage = () => {
    return (
        <div className='systemManage-container'>
            <div className='systemManage-topBanner'>
                <div className='systemManage-topBanner-datePicker'>
                    <DatePicker.RangePicker showTime/>
                </div>
                <div className='systemManage-topBanner-radio'>
                    <Select defaultValue="-1" style={{width: 120}}
                            onChange={value => console.log(`selected ${value}`)}>
                        <Select.Option value="-1">不限</Select.Option>
                        <Select.Option value="0">未审核</Select.Option>
                        <Select.Option value="1">审核通过</Select.Option>
                        <Select.Option value="2">审核不通过</Select.Option>
                    </Select>
                </div>
            </div>
        </div>
    );
};

export default SystemManage;