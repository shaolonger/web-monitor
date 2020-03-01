import React, {useState} from 'react';

// 组件
import {Spin, Form, Input, Button, CheckboxSelect, Select, message} from 'antd';
import {UserOutlined, LockOutlined, MailOutlined, MobileOutlined} from '@ant-design/icons';

// 图片资源
import imgBgRegister from 'static/images/Register/bg_register.png';

// css
import './index.scss';

// service
import registerService from 'service/registerService';

const Register = props => {
    const [spinning, setSpinning] = useState(false);

    /**
     * 注册
     * @param values
     */
    const onFinish = values => {
        setSpinning(true);
        message.loading({content: '注册中，请稍等', duration: 0, key: 'register'});
        registerService.register(values)
            .then(res => {
                console.log('[成功]注册', res);
                setSpinning(false);
                message.success({
                    content: '注册成功，即将跳转', key: 'register',
                    onClose: () => {
                        props.history.replace('/login');
                    }
                });
            })
            .catch(err => {
                console.log('[失败]注册', err);
                setSpinning(false);
                message.error({content: '注册失败' + (err.msg || ''), key: 'register'});
            });
    };

    /**
     * 返回
     */
    const onBack = () => {
        props.history.goBack();
    };

    return (
        <div className='register-container'>
            <img className='register-container-bg' src={imgBgRegister}/>
            <Spin spinning={spinning}>
                <Form className='register-container-form' onFinish={onFinish}>
                    <Form.Item name="username" rules={[{required: true, message: '请输入用户名'}]}>
                        <Input prefix={<UserOutlined/>} placeholder="用户名"/>
                    </Form.Item>
                    <Form.Item name="password" rules={[{required: true, message: '请输入密码'}]}>
                        <Input prefix={<LockOutlined/>} type="password" placeholder="密码"/>
                    </Form.Item>
                    <Form.Item name="email" rules={[{required: true, message: '请输入电子邮箱'}]}>
                        <Input prefix={<MailOutlined/>} placeholder="电子邮箱"/>
                    </Form.Item>
                    <Form.Item name="phone">
                        <Input prefix={<MobileOutlined/>} type='number' placeholder="电话号码"/>
                    </Form.Item>
                    <Form.Item name="gender">
                        <Select allowClear placeholder="请选择性别">
                            <Select.Option value="0">未知</Select.Option>
                            <Select.Option value="1">男</Select.Option>
                            <Select.Option value="2">女</Select.Option>
                        </Select>
                    </Form.Item>
                    <Form.Item>
                        <div className='register-container-btnWrapper'>
                            <Button onClick={onBack} className='register-container-btnItem' type="default">返回</Button>
                            <Button className='register-container-btnItem' type="primary" htmlType="submit">确认注册</Button>
                        </div>
                    </Form.Item>
                </Form>
            </Spin>
        </div>
    );
};

export default Register;