import React from 'react';

// 组件
import {Form, Input, Button, Checkbox} from 'antd';
import {UserOutlined, LockOutlined, MailOutlined} from '@ant-design/icons';

// 图片资源
import imgBgRegister from 'static/images/Register/bg_register.png';

// css
import './index.scss';

const Register = props => {
    /**
     * 注册
     * @param values
     */
    const onFinish = values => {
        console.log('values', values);
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
                <Form.Item>
                    <div className='register-container-btnWrapper'>
                        <Button onClick={onBack} className='register-container-btnItem' type="default">返回</Button>
                        <Button className='register-container-btnItem' type="primary" htmlType="submit">确认注册</Button>
                    </div>
                </Form.Item>
            </Form>
        </div>
    );
};

export default Register;