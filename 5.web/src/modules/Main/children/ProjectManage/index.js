import React, {useState, useEffect} from 'react';
import {Modal, Input, Select, Table, Button, message, Spin} from 'antd';
import moment from "moment";

// css
import './index.scss';

// service
import userService from 'service/userService';
import projectService from 'service/projectService';

// init const
const defaultProjectDetail = {
    projectName: '',
    projectIdentifier: '',
    description: '',
    userList: [],
};

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

    // 详情页
    const [mode, setMode] = useState('add');
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [modalTitle, setModalTitle] = useState('新增');
    const [userList, setUserList] = useState([]);

    useEffect(() => {
        // 获取用户列表
        userService.get({isNeedPaging: 0})
            .then(res => {
                setSpinning(false);
                console.log('[成功]获取用户列表', res);
                const {success, data} = res;
                if (!success) {
                    message.error({content: '获取用户列表失败' + (res.msg || '')});
                } else {
                    setUserList(data.map(item => ({
                        label: item.username, value: item.id
                    })));
                }
            })
            .catch(err => {
                console.log('[失败]新增', err);
                setSpinning(false);
                message.error({content: '获取用户列表失败' + (err.msg || '')});
            });
    }, []);

    const [projectDetail, setProjectDetail] = useState({...defaultProjectDetail});
    const handleModalOnOk = () => {
        if (mode === 'add') {
            // 新增
            // 校验表单数据
            if (!projectDetail.projectName || !projectDetail.projectName.trim()) {
                return message.error('请输入项目名');
            }
            if (!projectDetail.projectIdentifier || !projectDetail.projectIdentifier.trim()) {
                return message.error('请输入项目标识');
            }

            setSpinning(true);
            message.loading({content: '数据提交中，请稍等', duration: 0, key: 'add'});
            projectService.add({...projectDetail, userList: JSON.stringify(projectDetail.userList)})
                .then(res => {
                    console.log('[成功]新增', res);
                    setSpinning(false);
                    message.success({
                        content: '新增成功',
                        key: 'add',
                        onClose: () => {
                            // 重置表单数据
                            setProjectDetail({...defaultProjectDetail});
                            getTableList(filterForm);
                            setIsModalVisible(false);
                        }
                    });
                })
                .catch(err => {
                    console.log('[失败]新增', err);
                    setSpinning(false);
                    message.error({content: '新增失败' + (err.msg || ''), key: 'add'});
                });
        } else if (mode === 'edit') {
            // 编辑
        }
    };
    const handleModalOnCancel = () => {
        setMode('add');
        setIsModalVisible(false);
    };
    useEffect(() => {
        if (mode === 'add') {
            // 新增
            setModalTitle('新增');
            setProjectDetail({...defaultProjectDetail});
        } else if (mode === 'view') {
            // 查看
            setModalTitle('查看');
        } else if (mode === 'edit') {
            // 编辑
            setModalTitle('编辑');
        }
    }, [mode]);
    const deleteProjectById = id => {
        setSpinning(true);
        message.loading({content: '数据删除中，请稍等', duration: 0, key: 'delete'});
        projectService.delete(id)
            .then(res => {
                const {success, msg} = res;
                if (success) {
                    console.log('[成功]删除', res);
                    setSpinning(false);
                    message.success({
                        content: '删除成功',
                        key: 'delete',
                        onClose: () => {
                            getTableList(filterForm);
                            setIsModalVisible(false);
                        }
                    });
                } else {
                    console.log('[失败]删除', res);
                    setSpinning(false);
                    message.error({content: '删除失败' + (msg || ''), key: 'delete'});
                }
            })
            .catch(err => {
                console.log('[失败]删除', err);
                setSpinning(false);
                message.error({content: '删除失败' + (err.msg || ''), key: 'delete'});
            });
    };

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
            render: row => (<div className='projectManage-table-handleBtn'>
                <Button onClick={() => {
                    const userList = row.userList.length ? row.userList.split(',').map(user => Number(user)) : [];
                    setProjectDetail({
                        projectName: row.projectName,
                        projectIdentifier: row.projectIdentifier,
                        description: row.description,
                        userList
                    });
                    setMode('view');
                    setIsModalVisible(true);
                }} type='primary'>查看</Button>
                <Button onClick={() => {
                    const userList = row.userList.length ? row.userList.split(',').map(user => Number(user)) : [];
                    setProjectDetail({
                        projectName: row.projectName,
                        projectIdentifier: row.projectIdentifier,
                        description: row.description,
                        userList
                    });
                    setMode('edit');
                    setIsModalVisible(true);
                }} type='primary'>编辑</Button>
                <Button onClick={() => {
                    deleteProjectById(row.id);
                }} type="primary" danger>删除</Button>
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
                        handle: item,
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
                    <Button onClick={() => {
                        setMode('add');
                        setIsModalVisible(true);
                    }} type='primary'>新增</Button>
                </div>
                <div className='projectManage-topBanner-input'>
                    <Input
                        placeholder="请输入项目名"
                        onChange={e => e && e.target &&
                            setFilterForm({...filterForm, projectName: e.target.value || ''})
                        }
                        allowClear
                    />
                </div>
            </div>
            <div className='projectManage-table'>
                <Spin spinning={spinning}>
                    <Table dataSource={dataSource} columns={columns} pagination={pagination}/>
                </Spin>
            </div>
            <Modal
                title={modalTitle} visible={isModalVisible} onOk={handleModalOnOk} onCancel={handleModalOnCancel}
            >
                <ul className='projectManage-modal-list' style={{
                    listStyle: 'none',
                    paddingLeft: '0'
                }}>
                    <li className='projectManage-modal-listItem' style={{
                        display: 'flex',
                        alignItems: 'center',
                        marginBottom: '2em',
                    }}>
                        <span className='projectManage-modal-key' style={{
                            marginRight: '1em',
                            minWidth: '4em',
                        }}>项目名</span>
                        <div className='projectManage-modal-value' style={{
                            flex: 1,
                        }}>
                            <Input
                                placeholder="请输入项目名"
                                value={projectDetail.projectName}
                                disabled={mode === 'view'}
                                onChange={e => e && e.target &&
                                    setProjectDetail({...projectDetail, projectName: e.target.value || ''})}
                                allowClear/>
                        </div>
                    </li>
                    <li className='projectManage-modal-listItem' style={{
                        display: 'flex',
                        alignItems: 'center',
                        marginBottom: '2em',
                    }}>
                        <span className='projectManage-modal-key' style={{
                            marginRight: '1em',
                            minWidth: '4em',
                        }}>项目标识</span>
                        <div className='projectManage-modal-value' style={{
                            flex: 1,
                        }}>
                            <Input
                                placeholder="请输入项目名"
                                value={projectDetail.projectIdentifier}
                                disabled={mode === 'view'}
                                onChange={e => e && e.target &&
                                    setProjectDetail({...projectDetail, projectIdentifier: e.target.value || ''})}
                                allowClear/>
                        </div>
                    </li>
                    <li className='projectManage-modal-listItem' style={{
                        display: 'flex',
                        alignItems: 'center',
                        marginBottom: '2em',
                    }}>
                        <span className='projectManage-modal-key' style={{
                            marginRight: '1em',
                            minWidth: '4em',
                        }}>项目描述</span>
                        <div className='projectManage-modal-value' style={{
                            flex: 1,
                        }}>
                            <Input
                                placeholder="请输入项目名"
                                value={projectDetail.description} disabled={mode === 'view'}
                                onChange={e => e && e.target &&
                                    setProjectDetail({...projectDetail, description: e.target.value || ''})}
                                allowClear/>
                        </div>
                    </li>
                    <li className='projectManage-modal-listItem' style={{
                        display: 'flex',
                        alignItems: 'center',
                        marginBottom: '2em',
                    }}>
                        <span className='projectManage-modal-key' style={{
                            marginRight: '1em',
                            minWidth: '4em',
                        }}>关联用户</span>
                        <div className='projectManage-modal-value' style={{
                            flex: 1,
                        }}>
                            <Select
                                mode="multiple"
                                placeholder="请选择用户"
                                value={projectDetail.userList}
                                onChange={value => setProjectDetail({...projectDetail, userList: value})}
                                style={{width: '100%'}}
                                disabled={mode === 'view'}
                            >
                                {userList.map(user => (
                                    <Select.Option key={user.value} value={user.value}>{user.label}</Select.Option>
                                ))}
                            </Select>
                        </div>
                    </li>
                </ul>
            </Modal>
        </div>
    );
};

export default ProjectManage;