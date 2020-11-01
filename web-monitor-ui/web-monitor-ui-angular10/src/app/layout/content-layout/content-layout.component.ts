import { Component, OnInit } from '@angular/core';

import { NzMessageService } from 'ng-zorro-antd/message';
import { UserService } from '@data/service/user.service';

import { UserInfo } from '@data/classes/userInfo.class';
import { UserRelatedProject } from '@data/classes/project.class';

@Component({
    selector: 'app-content-layout',
    templateUrl: './content-layout.component.html',
    styleUrls: ['./content-layout.component.scss']
})
export class ContentLayoutComponent implements OnInit {

    // 加载状态
    isLoading = false;
    // 用户信息
    userInfo = new UserInfo();
    // 用户关联的项目列表
    userRelatedProjectList: UserRelatedProject[] = [];
    // 用户选中的项目
    projectSelected: UserRelatedProject = new UserRelatedProject();
    // 左侧菜单栏折叠状态
    isCollapsed = false;
    // 左侧菜单列表
    menuList: Array<Object>;

    constructor(
        private userService: UserService,
        private message: NzMessageService
    ) { }

    ngOnInit(): void {
        this.getUserInfo();
        this.setUserMenuList();
        this.setUserRelatedProjectList();
    }

    /**
     * 获取用户信息
     */
    getUserInfo(): void {
        this.userInfo = this.userService.getUserInfo();
    }

    /**
     * 设置用户菜单
     */
    setUserMenuList(): void {
        this.menuList = this.userService.getUserMenuList();
    }

    /**
     * 根据用户获取关联的项目
     */
    setUserRelatedProjectList(): void {
        this.isLoading = true;
        this.userService.getUserRelatedProjectListAjax(
            {},
            res => {
                console.log('[成功]根据用户获取关联的项目', res);
                this.isLoading = false;
                const { success, data, msg } = res;
                if (!success) {
                    this.message.error(msg || '根据用户获取关联的项目失败');
                } else if (data && data.length) {
                    this.userService.setUserRelatedProjectList(data);
                    this.userRelatedProjectList = this.userService.getUserRelatedProjectList();
                    this.handleSelectProject(this.userRelatedProjectList[0]);
                }
            },
            err => {
                console.log('[失败]查询用户注册记录', err);
                this.isLoading = false;
            }
        );
    }

    /**
     * 选中项目
     * @param project 选中的项目
     */
    handleSelectProject(project: UserRelatedProject) {
        this.projectSelected = project;
        this.userService.setProjectSelected(project);
    }

    /**
     * 切换左侧菜单栏折叠状态
     */
    toggleCollapsed(): void {
        this.isCollapsed = !this.isCollapsed;
    }

}
