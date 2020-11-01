import React from 'react';
import {HashRouter, Switch, Route} from 'react-router-dom';

// pages
import Home from "../modules/Home";
import Register from "../modules/Register";
import Login from "../modules/Login";
import Main from "../modules/Main";
import NotFound from "../modules/NotFound";

const Router = () => {
    return (
        <div>
            <HashRouter>
                <Switch>
                    <Route path="/" exact component={Home}></Route>
                    <Route path="/register" component={Register}></Route>
                    <Route path="/login" component={Login}></Route>
                    <Route path="/main" component={Main}></Route>
                    <Route component={NotFound}></Route>
                </Switch>
            </HashRouter>
        </div>
    );
};

export default Router;