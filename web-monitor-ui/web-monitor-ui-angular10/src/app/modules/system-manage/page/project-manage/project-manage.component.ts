import { Component, OnInit } from '@angular/core';
import * as moment from 'moment';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { EnvService } from '@core/service/env.service';
import { UserService } from '@data/service/user.service';
import { ProjectService } from '@data/service/project.service';

import { Project } from '@data/interfaces/project.interface';

@Component({
    selector: 'app-project-manage',
    templateUrl: './project-manage.component.html',
    styleUrls: ['./project-manage.component.scss']
})
export class ProjectManageComponent implements OnInit {

    isLoading = false;
    // 操作模式
    mode = 'add';
    // 显示新增、编辑、查看对话框
    showDetailDialog = false;
    // 对话框标题
    detailDialogTitle = '新增项目';
    // 筛选条件
    filterForm = {
        pageNum: 1,
        pageSize: 10,
        projectName: ''
    };
    // 数据列表
    listData: Array<Object>;
    // 分页控制器
    paginationConfig = {
        total: 0
    };
    // 关联用户选项列表
    userOptionsList = [];
    validateForm!: FormGroup;
    // 标签序列
    detailDialogTabIndex: number = 0;
    // 接入方式
    accessTypeOpitons = [
        { label: '<script>标签引入', value: 'script', disabled: false },
        { label: 'npm', value: 'npm', disabled: true },
    ];
    // 开启功能
    activeFuncsOptions = [
        { label: 'JS异常', value: 'jsError' },
        { label: 'HTTP异常', value: 'httpError' },
        { label: '静态资源异常', value: 'ResourceLoadError' },
        { label: '自定义异常', value: 'customError' },
    ];
    // 打点代码-ngx-codemirror配置
    ngxCodeMirrorOptions = {
        lineNumbers: true,
        theme: 'default',
        mode: 'javascript',
        readOnly: true,
        viewportMargin: Infinity,
        autoFocus: true
    };
    // 打点代码-内容
    codeContent: string = '';

    constructor(
        private envService: EnvService,
        private userService: UserService,
        private projectService: ProjectService,
        private message: NzMessageService,
        private modal: NzModalService,
        private fb: FormBuilder
    ) { }

    ngOnInit(): void {
        this.initFormData();
        this.getUserOptionsList();
        this.getTableList();
        this.onValidateFormChange();
    }

    /**
     * 表单数据初始化
     */
    initFormData(): void {
        if (!this.validateForm) {
            this.validateForm = this.fb.group({
                id: [0],
                projectName: ['', [Validators.required]],
                projectIdentifier: ['', [Validators.required]],
                description: [''],
                userList: [[]],
                accessType: ['script', [Validators.required]],
                activeFuncs: [[], [Validators.required]],
                notifyDtToken: [''],
                notifyEmail: [''],
                isAutoUpload: [false, [Validators.required]],
            });
        } else {
            this.validateForm.patchValue({
                ...this.validateForm.getRawValue(),
                id: 0,
                projectName: '',
                projectIdentifier: '',
                description: '',
                userList: [],
                accessType: 'script',
                activeFuncs: [],
                notifyDtToken: [''],
                notifyEmail: [''],
                isAutoUpload: false
            });
        }
    }

    /**
     * 获取关联用户列表
     */
    getUserOptionsList(): void {
        this.isLoading = true;
        this.userService.getUsers(
            {},
            res => {
                console.log('[成功]获取关联用户列表', res);
                this.isLoading = false;
                let { success, data, msg } = res;
                if (!success) {
                    this.message.error(msg || '获取关联用户列表失败');
                } else {
                    this.userOptionsList = data.map(item => ({
                        label: item.username, value: item.id
                    }));
                }
            },
            err => {
                console.log('[失败]获取关联用户列表', err);
                this.isLoading = false;
            }
        );
    }

    /**
     * 获取项目列表
     */
    getTableList(): void {
        this.isLoading = true;
        this.projectService.getProjects(
            this.filterForm,
            res => {
                console.log('[成功]获取项目列表', res);
                this.isLoading = false;
                const { success, data, msg } = res;
                if (!success) {
                    this.message.error(msg || '获取项目列表失败');
                } else {
                    let { records, totalNum } = data;
                    this.listData = records.map(item => ({
                        ...item,
                        createTime: item.createTime && moment(new Date(item.createTime)).format('YYYY-MM-DD HH:mm:ss'),
                        updateTime: item.updateTime && moment(new Date(item.updateTime)).format('YYYY-MM-DD HH:mm:ss')
                    }));
                    this.paginationConfig.total = totalNum;
                }
            },
            err => {
                console.log('[失败]获取项目列表', err);
                this.isLoading = false;
            }
        );
    }

    /**
     * 对话框点击确认
     */
    handleModalOnOk(): void {
        // 新增
        if (this.mode === 'add') {
            this.addProject();
        }
        // 编辑
        if (this.mode === 'edit') {
            this.updateProject();
        }
    }

    /**
     * 对话框点击取消
     */
    handleModalOnCancel(): void {
        this.showDetailDialog = false;
        this.setMode('add');
    }

    /**
     * 操作按钮点击事件
     * @param mode 
     * @param data 
     */
    handleShowDetailDialog(mode: string, data?: Project): void {
        if (mode === 'delete') {
            // 删除
            this.modal.confirm({
                nzTitle: '提示',
                nzContent: '确定要删除该项目吗？<br>(注意，此操作无法撤回！)',
                nzOkText: '确定',
                nzOkType: 'danger',
                nzOnOk: () => this.deleteProject(data.id),
                nzCancelText: '取消'
            });
        } else if (mode === 'view' || mode === 'edit') {
            // 查看|编辑
            this.setMode(mode);
            this.validateForm.patchValue({
                ...this.validateForm.getRawValue(),
                ...data,
                userList: data.userList.length ? data.userList.split(',').map(user => Number(user)) : [],
                activeFuncs: data.activeFuncs.length ? data.activeFuncs.split(',') : [],
                isAutoUpload: !!data.isAutoUpload
            });
            if (mode === 'view') {
                this.validateForm.disable();
            } else {
                this.validateForm.enable();
            }
            this.showDetailDialog = true;
        } else if (mode === 'add') {
            this.setMode(mode);
            this.validateForm.enable();
            this.showDetailDialog = true;
        }
    }

    /**
     * 动态设置打点代码
     * @param projectIdentifier 
     */
    setCodeContent(projectIdentifier: string): void {
        const jsSdkAPIBasicUrl = this.envService.getJsSdkAPIBasicUrl();
        this.codeContent = `<script id="web-monitor-sdk" type="text/javascript" src="${jsSdkAPIBasicUrl}${projectIdentifier}"></script>`;
    }

    /**
     * 监听表单内容变化
     */
    onValidateFormChange(): void {
        this.validateForm.valueChanges.subscribe(value => {
            const { projectIdentifier } = value;
            this.setCodeContent(projectIdentifier);
        });
    }

    /**
     * 设置操作模式
     * @param mode
     */
    setMode(mode: string): void {
        this.mode = mode;
        this.detailDialogTabIndex = 0;
        if (mode === 'add') {
            // 新增
            this.detailDialogTitle = '新增';
            this.initFormData();
        } else if (mode === 'view') {
            // 查看
            this.detailDialogTitle = '查看';
        } else if (mode === 'edit') {
            // 编辑
            this.detailDialogTitle = '编辑';
        }
    }

    /**
     * 新增项目
     */
    addProject(): void {
        this.isLoading = true;
        let formData = this.validateForm.getRawValue();
        this.projectService.addProject(
            {
                ...formData,
                userList: JSON.stringify(formData.userList),
                activeFuncs: formData.activeFuncs.join(','),
                isAutoUpload: formData.isAutoUpload ? 1 : 0
            },
            res => {
                console.log('[成功]新增项目', res);
                this.isLoading = false;
                const { success, msg } = res;
                if (!success) {
                    this.message.error(msg || '新增项目失败');
                } else {
                    this.showDetailDialog = false;
                    this.getTableList();
                    this.message.success('新增成功');
                }
            },
            err => {
                console.log('[失败]新增项目', err);
                this.isLoading = false;
            }
        );
    }

    /**
     * 编辑项目
     */
    updateProject(): void {
        this.isLoading = true;
        let formData = this.validateForm.getRawValue();
        this.projectService.updateProject(
            {
                ...formData,
                userList: JSON.stringify(formData.userList),
                activeFuncs: formData.activeFuncs.join(','),
                isAutoUpload: formData.isAutoUpload ? 1 : 0
            },
            res => {
                console.log('[成功]编辑项目', res);
                this.isLoading = false;
                const { success, msg } = res;
                if (!success) {
                    this.message.error(msg || '编辑项目失败');
                } else {
                    this.showDetailDialog = false;
                    this.getTableList();
                    this.message.success('编辑成功');
                }
            },
            err => {
                console.log('[失败]编辑项目', err);
                this.isLoading = false;
            }
        );
    }

    /**
     * 删除项目
     */
    deleteProject(id: number): void {
        this.isLoading = true;
        this.projectService.deleteProject(
            id,
            res => {
                const { success, msg } = res;
                this.isLoading = false;
                if (success) {
                    console.log('[成功]删除', res);
                    this.showDetailDialog = false;
                    this.getTableList();
                    this.message.success('删除成功');
                } else {
                    console.log('[失败]删除', res);
                    this.message.error(msg || '删除项目失败');
                }
            },
            err => {
                console.log('[失败]删除项目失败', err);
                this.isLoading = false;
            }
        );
    }
}
