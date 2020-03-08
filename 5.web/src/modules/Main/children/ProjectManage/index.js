import React, {useState, useEffect} from 'react';
import {Input, Select, Table, Button, message, Spin} from 'antd';
import moment from "moment";

// css
import './index.scss';

// service
import projectService from 'service/projectService';

const ProjectManage = () => {

    const [dataSource, setDataSource] = useState([]);
    const [filterForm, setFilterForm] = useState({
        pageNum: 1,
        pageSize: 10,
        projectName: ''
    });
    const [total, setTotal] = useState(3);
    const [spinning, setSpinning] = useState(false);

    useEffect(() => {
        console.log('[useCallback]filterForm', filterForm);
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
            title: '项目名',
            dataIndex: 'projectName',
            key: 'projectName',
        },
        {
            title: '项目标识',
            dataIndex: 'projectIdentifier',
            key: 'projectIdentifier',
        },
        {
            title: '项目描述',
            dataIndex: 'description',
            key: 'description',
        },
        {
            title: '创建时间',
            dataIndex: 'createTime',
            key: 'createTime',
        },
        {
            title: '修改时间',
            dataIndex: 'updateTime',
            key: 'updateTime',
        },
        {
            title: '操作',
            dataIndex: 'handle',
            key: 'handle',
            align: 'center',
            render: () => (<div className='projectManage-table-handleBtn'>
                <Button onClick={() => {}} type='primary'>查看</Button>
                <Button onClick={() => {}} type='primary'>编辑</Button>
                <Button onClick={() => {}} type="primary" danger>删除</Button>
            </div>)
        },
    ];

    /**
     * 获取列表数据
     * @param form
     */
    const getTableList = form => {
        setSpinning(true);
        projectService.get(form)
            .then(res => {
                setSpinning(false);
                console.log('[成功]获取列表数据', res);
                const {success, data, msg} = res;
                if (!success) {
                    message.error({content: msg || '获取列表数据失败'});
                } else {
                    const {records, totalNum} = data;
                    setTotal(totalNum);
                    const dataSource = records.map((item, index) => ({
                        key: index + 1,
                        index: index + 1,
                        projectName: item.projectName,
                        projectIdentifier: item.projectIdentifier,
                        description: item.description,
                        createTime: item.createTime && moment(new Date(item.createTime)).format('YYYY-MM-DD HH:mm:ss'),
                        updateTime: item.updateTime && moment(new Date(item.updateTime)).format('YYYY-MM-DD HH:mm:ss'),
                    }));
                    setDataSource(dataSource);
                }
            })
            .catch(err => {
                setSpinning(false);
                console.log('[失败]获取列表数据', err);
                message.error({content: err.msg || '获取列表数据失败'});
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
        <div className='projectManage-container'>
            <div className='projectManage-topBanner'>
                <div className='projectManage-topBanner-add'>
                    <Button onClick={() => {}} type='primary'>新增</Button>
                </div>
                <div className='projectManage-topBanner-input'>
                    <Input placeholder="请输入项目名" onChange={e => e && e.target &&
                        setFilterForm({...filterForm, projectName: e.target.value || ''})} allowClear />
                </div>
            </div>
            <div className='projectManage-table'>
                <Spin spinning={spinning}>
                    <Table dataSource={dataSource} columns={columns} pagination={pagination}/>
                </Spin>
            </div>
        </div>
    );
};

export default ProjectManage;