import React, {useState, useEffect} from 'react';
import {DatePicker, Select, Table, Button} from 'antd';

// css
import './index.scss';

const SystemManage = () => {

    const [dataSource, setDataSource] = useState([]);
    const [filterForm, setFilterForm] = useState({
        startTime: '',
        endTime: '',
        auditResult: ''
    });
    const [current, setCurrent] = useState(0);
    const [total, setTotal] = useState(3);

    useEffect(() => {
        console.log('[useCallback]filterForm', filterForm);
    }, [filterForm]);

    // 表格数据
    const columns = [
        {
            title: '序号',
            dataIndex: 'index',
            key: 'index',
        },
        {
            title: '用户名',
            dataIndex: 'username',
            key: 'username',
        },
        {
            title: '电子邮箱',
            dataIndex: 'email',
            key: 'email',
        },
        {
            title: '电话号码',
            dataIndex: 'phone',
            key: 'phone',
        },
        {
            title: '性别',
            dataIndex: 'sex',
            key: 'sex',
        },
        {
            title: '审核结果',
            dataIndex: 'auditResult',
            key: 'auditResult',
        },
        {
            title: '审核时间',
            dataIndex: 'auditTime',
            key: 'auditTime',
        },
        {
            title: '操作',
            dataIndex: 'handle',
            key: 'handle',
            align: 'center',
            render: () => <div className='systemManage-table-handleBtn'>
                <Button type='primary'>通过</Button>
                <Button>不通过</Button>
            </div>
        },
    ];
    const onChange = (page, pageSize) => {
        console.log(page, pageSize);
    };
    const onShowSizeChange = (current, pageSize) => {
        console.log(current, pageSize);
    };
    const pagination = {
        showSizeChanger: true,
        current,
        total,
        onShowSizeChange,
        onChange
    };

    return (
        <div className='systemManage-container'>
            <div className='systemManage-topBanner'>
                <div className='systemManage-topBanner-datePicker'>
                    <DatePicker.RangePicker
                        showTime format='YYYY-MM-DD hh:mm:ss'
                        onChange={
                            (date, dateString) => {
                                setFilterForm({...filterForm, startTime: dateString[0], endTime: dateString[1]})
                            }
                        }
                    />
                </div>
                <div className='systemManage-topBanner-radio'>
                    <Select style={{width: 120}} placeholder='审核状态' allowClear
                            onChange={value => setFilterForm({...filterForm, auditResult: value})}>
                        <Select.Option value="0">未审核</Select.Option>
                        <Select.Option value="1">审核通过</Select.Option>
                        <Select.Option value="2">审核不通过</Select.Option>
                    </Select>
                </div>
            </div>
            <div className='systemManage-table'>
                <Table dataSource={dataSource} columns={columns} pagination={pagination}/>
            </div>
        </div>
    );
};

export default SystemManage;