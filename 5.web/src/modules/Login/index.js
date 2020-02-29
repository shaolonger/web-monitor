import React from 'react';

// 组件
import {Form, Input, Button, Checkbox} from 'antd';
import {UserOutlined, LockOutlined} from '@ant-design/icons';

// 图片资源
import imgBgMonitor from 'static/images/Login/bg_monitor.png';

// css
import './index.scss';

const Login = props => {
    /**
     * 登录
     * @param values
     */
    const onFinish = values => {
        console.log('values', values);
    };

    /**
     * 注册
     */
    const onRegister = () => {
        props.history.push('/register');
    };

    return (
        <div className='login-container'>
            <img className='login-container-bg' src={imgBgMonitor}/>
            <Form className='login-container-form' name="normal_login" initialValues={{remember: true}}
                  onFinish={onFinish}>
                <Form.Item name="username" rules={[{required: true, message: '请输入用户名'}]}>
                    <Input prefix={<UserOutlined className="site-form-item-icon"/>} placeholder="用户名"/>
                </Form.Item>
                <Form.Item name="password" rules={[{required: true, message: '请输入密码'}]}>
                    <Input prefix={<LockOutlined/>} type="password" placeholder="密码"/>
                </Form.Item>
                <Form.Item name="remember" valuePropName="checked">
                    <Checkbox>记住密码</Checkbox>
                </Form.Item>
                <Form.Item>
                    <div className='login-container-btnWrapper'>
                        <Button className='login-container-btnItem' type="primary" htmlType="submit">登录</Button>
                        <Button className='login-container-btnItem' type="default" onClick={onRegister}>注册</Button>
                    </div>
                </Form.Item>
            </Form>
        </div>
    );
};

export default Login;