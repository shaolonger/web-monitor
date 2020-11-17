import { Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

import { EventService } from '@core/service/event.service';
import { HttpService } from '@core/service/http.service';

import { UserInfo } from '@data/classes/userInfo.class';
import { Project } from '@data/classes/project.class';
import { EventModel } from '@data/classes/event.class';


// 用户菜单列表
// 注：目前暂时写死
let userMenuList = [
    {
        name: '首页', pageUrl: '/home', icon: 'home', children: []
    },
    {
        name: '项目日志', pageUrl: '/project', icon: 'read', children: [
            // { name: '总览', pageUrl: '/project/overview' },
            { name: 'JS异常', pageUrl: '/project/jsErrorLog' },
            { name: 'HTTP异常', pageUrl: '/project/httpErrorLog' },
            { name: '静态资源异常', pageUrl: '/project/resourceLoadErrorLog' },
            { name: '自定义异常', pageUrl: '/project/customErrorLog' },
        ]
    },
    {
        name: '系统管理', pageUrl: '/systemManage', icon: 'appstore', children: [
            { name: '用户注册审核', pageUrl: '/systemManage/userRegisterAudit' },
            { name: '项目管理', pageUrl: '/systemManage/projectManage' },
        ],
    },
    {
        name: '监控预警', pageUrl: '/alarmManage', icon: 'alert', children: []
    },
    {
        name: '高级查询', pageUrl: '/advancedSearch', icon: 'file-search', children: []
    },
];

@Injectable({
    providedIn: 'root'
})
export class UserService {

    // 用户信息
    private userInfo: UserInfo = new UserInfo();
    // 用户关联项目列表
    private userRelatedProjectList: Project[] = [];
    // 用户选中的项目
    private projectSelected: Project = new Project();

    constructor(
        private httpService: HttpService,
        private eventService: EventService
    ) { }

    /**
     * 获取用户信息
     */
    public getUserInfo(): UserInfo {
        return this.userInfo;
    }

    /**
     * 设置用户信息
     * @param userInfo 用户信息
     */
    public setUserInfo(userInfo: UserInfo): void {
        this.userInfo = userInfo;
    }

    /**
     * 获取用户菜单列表
     */
    public getUserMenuList(): Array<{}> {
        return userMenuList;
    }

    /**
     * 获取用户列表
     * @param params 
     * @param successCallback 
     * @param failCallback 
     */
    public getUsers(params: Object, successCallback?: Function, failCallback?: Function): void {
        this.httpService.get('/user/get', params).subscribe(
            (res: any) => {
                successCallback && successCallback(res);
            },
            (err: HttpErrorResponse) => {
                failCallback && failCallback(err);
            }
        );
    }

    /**
     * 查询用户注册记录
     * @param params 
     * @param successCallback 
     * @param failCallback 
     */
    public getUserRegisterRecord(params: Object, successCallback?: Function, failCallback?: Function): void {
        this.httpService.get('/userRegisterRecord/get', params).subscribe(
            (res: any) => {
                successCallback && successCallback(res);
            },
            (err: HttpErrorResponse) => {
                failCallback && failCallback(err);
            }
        );
    }

    /**
     * 用户注册记录审批
     * @param params 
     * @param successCallback 
     * @param failCallback 
     */
    public audit(params: Object, successCallback?: Function, failCallback?: Function): void {
        this.httpService.post('/userRegisterRecord/audit', params).subscribe(
            (res: any) => {
                successCallback && successCallback(res);
            },
            (err: HttpErrorResponse) => {
                failCallback && failCallback(err);
            }
        );
    }

    /**
     * 设置用户获取关联的项目
     * @param list 列表
     */
    public setUserRelatedProjectList(list: Project[]) {
        this.userRelatedProjectList = list;
    }

    /**
     * 获取用户获取关联的项目
     */
    public getUserRelatedProjectList(): Project[] {
        return this.userRelatedProjectList;
    }

    /**
     * 根据用户获取关联的项目
     * @param params 
     * @param successCallback 
     * @param failCallback 
     */
    getUserRelatedProjectListAjax(params: Object, successCallback?: Function, failCallback?: Function): void {
        this.httpService.get('/user/getRelatedProjectList', params).subscribe(
            (res: any) => {
                successCallback && successCallback(res);
            },
            (err: HttpErrorResponse) => {
                failCallback && failCallback(err);
            }
        );
    }

    /**
     * 设置用户选择的项目
     * @param project 项目
     */
    setProjectSelected(project: Project) {
        this.projectSelected = project;
        let event = new EventModel();
        event.eventName = 'projectSelectedChanged';
        event.eventPayload = this.projectSelected;
        this.eventService.eventEmitter.emit(event);
    }

    /**
     * 获取用户选择的项目
     * @param project 项目
     */
    getProjectSelected(): Project {
        return this.projectSelected;
    }
}
