import React, {useState, useEffect} from 'react';
import {useHistory} from 'react-router-dom';

// 图片资源
import imgIconAvatar from 'static/images/Home/icon_avatar.png';
import imgBgSystemManage from 'static/images/Main/systemManage/bg_systemManage.png';

// css
import './index.scss';

const Home = () => {

    const history = useHistory();
    // TODO 这里的模块列表，后期需要改为从角色菜单中获取
    const [moduleList, setModuleList] = useState([]);

    useEffect(() => {
        // 判断用户是否登录，否则跳转至登录页
        const userInfo = window.sessionStorage.getItem('ump-userInfo');
        if (!userInfo) {
            history.replace('/login');
            return;
        }
    }, []);

    useEffect(() => {
        setModuleList([
            {
                name: '系统管理', pageUrl: '/main/systemManage', children: [
                    {name: '用户注册审核', pageUrl: '/main/systemManage/userRegisterAudit'},
                    {name: '项目管理', pageUrl: '/main/systemManage/projectManage'},
                ],
            },
            {
                name: '项目1', pageUrl: '/main/project', children: [
                    {name: '项目总览', pageUrl: '/main/project/overview'},
                    {name: 'JS错误', pageUrl: '/main/project/jsErrorLog'},
                    {name: 'HTTP异常', pageUrl: '/main/project/httpErrorLog'},
                    {name: '静态资源异常', pageUrl: '/main/project/resourceLoadErrorLog'},
                    {name: '自定义异常', pageUrl: '/main/project/customErrorLog'},
                ],
            },
        ]);
    }, []);

    /**
     * 跳转至模块
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
            <img className='home-iconAvatar' src={imgIconAvatar}/>
            <ul className='home-modules-list'>
                {
                    moduleList && moduleList.map((module, index) => (
                        <li key={index} className='home-modules-listItem' onClick={() => navigateTo(module)}>
                            <div className='home-modules-iconWrapper'
                                 style={{backgroundImage: `url(${imgBgSystemManage})`}}></div>
                            <p>{module.name}</p>
                        </li>
                    ))
                }
            </ul>
        </div>
    );
};

export default Home;