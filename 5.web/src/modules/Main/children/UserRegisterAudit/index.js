import React, {useState, useEffect} from 'react';
import {DatePicker, Select, Table, Button, message, Spin} from 'antd';
import moment from "moment";

// css
import './index.scss';

// service
import registerService from 'service/registerService';

// const
import {auditResultMap, genderMap} from 'const/SystemManage.js';

const SystemManage = () => {

    const [dataSource, setDataSource] = useState([]);
    const [filterForm, setFilterForm] = useState({
        pageNum: 1,
        pageSize: 10,
        startTime: '',
        endTime: '',
        auditResult: ''
    });
    const [total, setTotal] = useState(3);
    const [spinning, setSpinning] = useState(false);

    useEffect(() => {
        // console.log('[useCallback]filterForm', filterForm);
        getTableList(filterForm);
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
            dataIndex: 'gender',
            key: 'gender',
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
            render: ({auditResult, id}) => auditResult === -1 && <div className='systemManage-table-handleBtn'>
                <Button onClick={() => audit(id, 1)} type='primary'>通过</Button>
                <Button onClick={() => audit(id, 0)}>不通过</Button>
            </div>
        },
    ];

    /**
     * 获取列表数据
     * @param form
     */
    const getTableList = form => {
        setSpinning(true);
        registerService.get(form)
            .then(res => {
                setSpinning(false);
                console.log('[成功]查询用户注册记录', res);
                const {success, data, msg} = res;
                if (!success) {
                    message.error({content: msg || '查询用户注册记录失败'});
                } else {
                    const {records, totalNum} = data;
                    setTotal(totalNum);
                    const dataSource = records.map((item, index) => ({
                        key: index + 1,
                        index: index + 1,
                        username: item.username,
                        email: item.email,
                        phone: item.phone,
                        gender: genderMap[item.gender],
                        auditResult: auditResultMap[item.auditResult],
                        auditTime: moment(new Date(item.updateTime)).format('YYYY-MM-DD HH:mm:ss'),
                        handle: item,
                    }));
                    setDataSource(dataSource);
                }
            })
            .catch(err => {
                setSpinning(false);
                console.log('[失败]查询用户注册记录', err);
                message.error({content: err.msg || '查询用户注册记录失败'});
            });
    };

    /**
     * 审批
     * @param auditId
     * @param auditResult
     */
    const audit = (auditId, auditResult) => {
        setSpinning(true);
        registerService.audit({auditId, auditResult})
            .then(res => {
                setSpinning(false);
                console.log('[成功]审批', res);
                const {success, msg} = res;
                if (!success) {
                    message.error({content: msg || '审批失败'});
                } else {
                    message.success({
                        content: '审批成功',
                        onClose: () => getTableList(filterForm)
                    });
                }
            })
            .catch(err => {
                setSpinning(false);
                console.log('[失败]审批', err);
                message.error({content: err.msg || '审批失败'});
            });
    };

    const onChange = (pageNum, pageSize) => {
        // console.log(pageNum, pageSize);
        setFilterForm({...filterForm, pageNum, pageSize});
    };
    const onShowSizeChange = (pageNum, pageSize) => {
        // console.log(pageNum, pageSize);
        setFilterForm({...filterForm, pageNum, pageSize});
    };
    const pagination = {
        showSizeChanger: true,
        current: filterForm.pageNum,
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
                <Spin spinning={spinning}>
                    <Table dataSource={dataSource} columns={columns} pagination={pagination}/>
                </Spin>
            </div>
        </div>
    );
};

export default SystemManage;