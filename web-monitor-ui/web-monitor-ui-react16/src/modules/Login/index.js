import React, {useState, useEffect} from 'react';
import {useHistory} from 'react-router-dom';
import {Spin, Form, Input, Button, Checkbox, message} from 'antd';
import {UserOutlined, LockOutlined} from '@ant-design/icons';

// state
import useUserInfo from "../../state/useUserInfo";

// 图片资源
import imgBgMonitor from 'static/images/Login/bg_monitor.png';

// css
import './index.scss';

// service
import userService from 'service/userService';

const Login = props => {

    const history = useHistory();
    const [spinning, setSpinning] = useState(false);
    const [form] = Form.useForm();
    const [userInfo, setUserinfo] = useUserInfo();

    useEffect(() => {
        // console.log('[Login]useEffect', props);
        // 判断本地是否有登录信息，有则还原
        const loginInfo = window.localStorage.getItem('ump-loginInfo');
        if (loginInfo) {
            const {username, password, remember} = JSON.parse(loginInfo);
            form.setFieldsValue({username, password, remember});
        }
    }, []);

    /**
     * 登录
     * @param values
     */
    const onFinish = values => {
        const {username, password, remember} = values;
        setSpinning(true);
        message.loading({content: '登录中，请稍等', duration: 0, key: 'login'});
        userService.login({username, password})
            .then(res => {
                console.log('[成功]登录', res);
                setSpinning(false);
                const {success, data, msg} = res;
                if (success) {
                    // 将用户信息存入state及本地
                    setUserinfo({...data, hasLogin: true});
                    window.sessionStorage.setItem('ump-userInfo', JSON.stringify(data));
                    // 若选择了记住密码，则将用户名、密码存入本地
                    if (remember) {
                        window.localStorage.setItem('ump-loginInfo', JSON.stringify({
                            username, password, remember
                        }));
                    } else {
                        window.localStorage.removeItem('ump-loginInfo');
                    }
                    message.success({
                        content: '登录成功，即将跳转', key: 'login',
                        onClose: () => {
                            history.replace('/');
                        }
                    });
                } else {
                    message.error({content: msg || '登录失败', key: 'login'});
                }
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
        history.push('/register');
    };

    return (
        <div className='login-container'>
            <img className='login-container-bg' src={imgBgMonitor}/>
            <Spin spinning={spinning}>
                <Form form={form} className='login-container-form' onFinish={onFinish}>
                    <Form.Item name="username" rules={[{required: true, message: '请输入用户名'}]}>
                        <Input prefix={<UserOutlined/>} placeholder="用户名" allowClear/>
                    </Form.Item>
                    <Form.Item name="password" rules={[{required: true, message: '请输入密码'}]}>
                        <Input prefix={<LockOutlined/>} type="password" placeholder="密码" allowClear/>
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