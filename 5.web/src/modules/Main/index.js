import React, {useState, useEffect} from 'react';
import {Switch, Route, Link, useHistory, useLocation} from "react-router-dom";
import {Menu} from "antd";
import {LeftOutlined} from '@ant-design/icons';

// children
import UserRegisterAudit from "./children/UserRegisterAudit";
import ProjectManage from "./children/ProjectManage";

// css
import './index.scss';

const Main = () => {

    const history = useHistory();
    const location = useLocation();
    const [moduleName, setModuleName] = useState('');
    const [navList, setNavList] = useState([]);

    useEffect(() => {
        const {state} = location;
        if (state) {
            const {module} = state;
            console.log('module', module);
            const {name, children} = module;
            if (name) setModuleName(name);
            if (children && children.length) setNavList(children);
        }
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
        <div className='main-container'>
            <div className='main-topBanner'>
                <Link to='/'>
                    <div className='main-topBanner-back'>
                        <LeftOutlined/>
                        <span>返回首页</span>
                    </div>
                </Link>
                <div className='main-topBanner-title'>{moduleName}</div>
            </div>
            <div className='main-body'>
                <div className='main-leftNavList'>
                    <Menu>
                        {
                            navList.map(nav =>
                                <Menu.Item key={nav.name} onClick={() => navigateTo(nav)}>{nav.name}</Menu.Item>
                            )
                        }
                    </Menu>
                </div>
                <div className='main-router'>
                    <Switch>
                        <Route path='/main/systemManage/userRegisterAudit' component={UserRegisterAudit} />
                        <Route path='/main/systemManage/projectManage' component={ProjectManage} />
                    </Switch>
                </div>
            </div>
        </div>
    );
};

export default Main;