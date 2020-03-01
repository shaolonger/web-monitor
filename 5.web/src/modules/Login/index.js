import React, {useState} from 'react';

// 组件
import {Spin, Form, Input, Button, Checkbox, message} from 'antd';
import {UserOutlined, LockOutlined} from '@ant-design/icons';

// 图片资源
import imgBgMonitor from 'static/images/Login/bg_monitor.png';

// css
import './index.scss';

// service
import loginService from 'service/loginService';

const Login = props => {
    const [spinning, setSpinning] = useState(false);

    /**
     * 登录
     * @param values
     */
    const onFinish = values => {
        const {username, password, remember} = values;
        setSpinning(true);
        message.loading({content: '登录中，请稍等', duration: 0, key: 'login'});
        loginService.login({username, password})
            .then(res => {
                console.log('[成功]登录', res);
                setSpinning(false);
                message.success({content: '登录成功，即将跳转', key: 'login'});
            })
            .catch(err => {
                console.log('[失败]登录', err);
                setSpinning(false);
                message.error({content: '登录失败', key: 'login'});
            });
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
            <Spin spinning={spinning}>
                <Form className='login-container-form' initialValues={{remember: true}} onFinish={onFinish}>
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
            </Spin>
        </div>
    );
};

export default Login;