import React, {useState, useEffect} from 'react';
import {useHistory} from 'react-router-dom';

// service
import userService from 'service/userService';

// state
import useUserInfo from "../../state/useUserInfo";

// 图片资源
import imgIconAvatar from 'static/images/Home/icon_avatar.png';
import imgBgSystemManage from 'static/images/Main/systemManage/bg_systemManage.png';

// css
import './index.scss';
import {message, Spin, Table} from "antd";

const Home = () => {

    const history = useHistory();
    const [userInfo, setUserInfo] = useUserInfo();
    const [spinning, setSpinning] = useState(false);
    const [moduleList, setModuleList] = useState([]);

    useEffect(() => {
        // 判断用户是否登录，否则跳转至登录页
        if (!userInfo.hasLogin) {
            history.replace('/login');
            return;
        } else {
            console.log('userInfo', userInfo);
            (async () => {
                let moduleList = [];
                if (userInfo.isAdmin === 1) {
                    // 若为管理员，添加【系统管理】模块
                    moduleList.push({
                        name: '系统管理', pageUrl: '/main/systemManage', children: [
                            {name: '用户注册审核', pageUrl: '/main/systemManage/userRegisterAudit'},
                            {name: '项目管理', pageUrl: '/main/systemManage/projectManage'},
                        ],
                    });
                }
                const data = await getRelatedProjectList();
                if (data && data.length) {
                    // 若有关联的项目，则添加对应的模块
                    data.forEach(v => {
                        moduleList.push({
                            name: v.projectName, pageUrl: '/main/project', children: [
                                {name: '项目总览', pageUrl: '/main/project/overview'},
                                {name: 'JS错误', pageUrl: '/main/project/jsErrorLog'},
                                {name: 'HTTP异常', pageUrl: '/main/project/httpErrorLog'},
                                {name: '静态资源异常', pageUrl: '/main/project/resourceLoadErrorLog'},
                                {name: '自定义异常', pageUrl: '/main/project/customErrorLog'},
                            ],
                        });
                    });
                }
                setModuleList(moduleList);
            })();
        }
    }, []);

    /**
     * 根据用户获取关联的项目
     */
    const getRelatedProjectList = () => {
        setSpinning(true);
        return new Promise((resolve, reject) => {
            userService.getRelatedProjectList()
                .then(res => {
                    setSpinning(false);
                    const {success, data} = res;
                    if (!success) {
                        console.log('[失败]根据用户获取关联的项目', res);
                        message.error({content: '根据用户获取关联的项目失败: ' + (res.msg || '')});
                        reject(false);
                    } else {
                        console.log('[成功]根据用户获取关联的项目', res);
                        resolve(data);
                    }
                })
                .catch(err => {
                    console.log('[失败]根据用户获取关联的项目', err);
                    setSpinning(false);
                    message.error({content: '根据用户获取关联的项目失败: ' + (err.msg || '')});
                    reject(false);
                });
        });
    };

    /**
     * 跳转至模块
     *
     * @param module
     */
    const navigateTo = module => {
        const {pageUrl} = module;
        const location = {
            pathname: pageUrl,
            state: {module}
        };
        history.push(location);
    };

    return (
        <div className='home-container'>
            <Spin spinning={spinning}>
                <img className='home-iconAvatar' src={imgIconAvatar}/>
                <ul className='home-modules-list'>
                    {
                        moduleList.length
                            ? moduleList.map((module, index) => (
                                <li key={index} className='home-modules-listItem' onClick={() => navigateTo(module)}>
                                    <div className='home-modules-iconWrapper'
                                         style={{backgroundImage: `url(${imgBgSystemManage})`}}></div>
                                    <p>{module.name}</p>
                                </li>
                            ))
                            : '您没有配置任何模块'
                    }
                </ul>
            </Spin>
        </div>
    );
};

export default Home;