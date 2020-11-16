import { Component, OnInit, EventEmitter } from '@angular/core';
import * as moment from 'moment';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { UserService } from '@data/service/user.service';
import { AlarmService } from '@data/service/alarm.service';

import { Alarm } from '@data/interfaces/alarm.interface';

@Component({
    selector: 'app-alarm-manage',
    templateUrl: './alarm-manage.component.html',
    styleUrls: ['./alarm-manage.component.scss']
})
export class AlarmManageComponent implements OnInit {

    isLoading = false;
    projectIdentifier = '';
    // 操作模式
    mode = 'add';
    // 显示新增、编辑对话框
    showDetailDialog = false;
    // 对话框标题
    detailDialogTitle = '新增预警';
    // 筛选条件
    filterForm = {
        pageNum: 1,
        pageSize: 10,
        projectIdentifier: '',
        name: ''
    };
    // 数据列表
    listData: Array<Object>;
    // 分页控制器
    paginationConfig = {
        total: 0
    };
    // 过滤条件选项列表
    categoryOptionsList = [
        { label: '全部', value: 0 },
        { label: 'JS异常', value: 1 },
        { label: 'HTTP异常', value: 2 },
        { label: '静态资源异常', value: 3 },
        { label: '自定义异常', value: 4 },
    ];
    // 静默期选项列表
    silentPeriodOptionsList = [
        { label: '不静默', value: 0 },
        { label: '5分钟', value: 1 },
        { label: '10分钟', value: 2 },
        { label: '15分钟', value: 3 },
        { label: '30分钟', value: 4 },
        { label: '1小时', value: 5 },
        { label: '3小时', value: 6 },
        { label: '12小时', value: 7 },
        { label: '24小时', value: 8 },
        { label: '当天', value: 9 },
    ];
    // 通知方式选项列表
    subscriberListOptionsList = [
        { label: '钉钉机器人', value: 1 },
        { label: '邮箱', value: 2 },
    ];
    // 报警规则选项列表
    ruleOperatorOptionsList = [
        { label: '满足下列所有规则', value: '&&' },
        { label: '满足下列任一规则', value: '||' },
    ];
    // 报警等级选项列表
    ruleLevelOptionsList = [
        { label: 'P1-紧急', value: 2 },
        { label: 'P2-高', value: 1 },
        { label: 'P3-中', value: 0 },
        { label: 'P4-低', value: -1 },
    ];
    // 监控指标选项列表
    ruleIndOptionsList = [
        { label: '影响用户数', value: 'uvCount', valText: '个' },
        { label: '影响用户率', value: 'uvRate', valText: '%' },
        { label: '人均异常次数', value: 'perPV', valText: '个' },
        { label: '新增异常数', value: 'newPV', valText: '个' },
    ];
    // 监控指标选项列表
    ruleOpOptionsList = [
        { label: '最近N分钟总和大于', agg: 'count', op: '>', timeSpanSize: 1, interval: 1, timeSpanText: '分钟' },
        { label: '最近N天总和大于', agg: 'count', op: '>', timeSpanSize: 1440, interval: 1, timeSpanText: '天' },
        { label: '最近N分钟平均值大于', agg: 'avg', op: '>', timeSpanSize: 1, interval: 1, timeSpanText: '分钟' },
        { label: '最近N天平均值大于', agg: 'avg', op: '>', timeSpanSize: 1440, interval: 1440, timeSpanText: '天' },
        { label: '最近N分钟平均值环比上涨大于', agg: 'avg', op: '>', timeSpanSize: 1, interval: 1, timeSpanText: '分钟' },
        { label: '最近N分钟总和环比上涨大于', agg: 'count', op: '>', timeSpanSize: 1, interval: 1, timeSpanText: '分钟' },
        { label: '最近N小时平均值与昨天同比上涨大于', agg: 'avg', op: 'd_up', timeSpanSize: 60, interval: 60, timeSpanText: '小时' },
        { label: '最近N小时平均值与上周同比上涨大于', agg: 'avg', op: 'w_up', timeSpanSize: 60, interval: 60, timeSpanText: '小时' },
    ];
    // 监控条件选择结果
    ruleOp = '&&';
    ruleRules = [
        { timeSpan: null, ind: "", type: "", agg: "", op: "", val: null, interval: null, timeSpanText: ' ', valText: ' ' }
    ];
    validateForm!: FormGroup;

    constructor(
        private userService: UserService,
        private alarmService: AlarmService,
        private message: NzMessageService,
        private modal: NzModalService,
        private fb: FormBuilder
    ) { }

    ngOnInit(): void {
        this.setProjectSelected();
        this.initFormData();
        this.getTableList();
        this.onValidateFormChange();
    }

    /**
     * 设置用户选择的项目
     */
    setProjectSelected(): void {
        let projectSelected = this.userService.getProjectSelected();
        const { projectIdentifier, notifyDtToken, notifyEmail } = projectSelected;
        this.projectIdentifier = projectIdentifier;

        // 设置filterForm
        this.filterForm.projectIdentifier = this.projectIdentifier;
    }

    /**
     * 表单数据初始化
     */
    initFormData(): void {
        if (!this.validateForm) {
            this.validateForm = this.fb.group({
                id: [0],
                name: ['', [Validators.required]],
                projectIdentifier: [this.projectIdentifier],
                level: [0, [Validators.required]],
                category: [0, [Validators.required]],
                startTime: [''],
                endTime: [''],
                silentPeriod: [0, [Validators.required]],
                isActive: [true, [Validators.required]],
                subscriberList: [[]]
            });
        } else {
            this.validateForm.patchValue({
                ...this.validateForm.getRawValue(),
                id: 0,
                name: '',
                projectIdentifier: this.projectIdentifier,
                level: 0,
                category: 0,
                startTime: '',
                endTime: '',
                silentPeriod: 0,
                isActive: true,
                subscriberList: []
            });
        }
    }

    /**
     * 获取预警列表
     */
    getTableList(): void {
        this.isLoading = true;
        this.alarmService.getAlarms(
            this.filterForm,
            res => {
                console.log('[成功]获取预警列表', res);
                this.isLoading = false;
                const { success, data, msg } = res;
                if (!success) {
                    this.message.error(msg || '获取预警列表失败');
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
                console.log('[失败]获取预警列表', err);
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
            this.addAlarm();
        }
        // 编辑
        if (this.mode === 'edit') {
            this.updateAlarm();
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
    handleShowDetailDialog(mode: string, data?: Alarm): void {
        if (mode === 'delete') {
            // 删除
            this.modal.confirm({
                nzTitle: '提示',
                nzContent: '确定要删除该预警吗？<br>(注意，此操作无法撤回！)',
                nzOkText: '确定',
                nzOkType: 'danger',
                nzOnOk: () => this.deleteAlarm(data.id),
                nzCancelText: '取消'
            });
        } else if (mode === 'edit') {
            // 编辑
            this.setMode(mode);
            this.validateForm.patchValue({
                ...this.validateForm.getRawValue(),
                ...data,
            });
            this.validateForm.enable();
            this.showDetailDialog = true;
        } else if (mode === 'add') {
            this.setMode(mode);
            this.validateForm.enable();
            this.showDetailDialog = true;
        }
    }

    /**
     * 监听表单内容变化
     */
    onValidateFormChange(): void {
        this.validateForm.valueChanges.subscribe(value => {
            // const { projectIdentifier } = value;
        });
    }

    /**
     * 设置操作模式
     * @param mode
     */
    setMode(mode: string): void {
        this.mode = mode;
        if (mode === 'add') {
            // 新增
            this.detailDialogTitle = '新增';
            this.initFormData();
        } else if (mode === 'edit') {
            // 编辑
            this.detailDialogTitle = '编辑';
        }
    }

    /**
     * 新增预警
     */
    addAlarm(): void {
        this.isLoading = true;
        let formData = this.validateForm.getRawValue();
        this.alarmService.addAlarm(
            {
                ...formData,
                userList: JSON.stringify(formData.userList),
                activeFuncs: formData.activeFuncs.join(','),
                isAutoUpload: formData.isAutoUpload ? 1 : 0
            },
            res => {
                console.log('[成功]新增预警', res);
                this.isLoading = false;
                const { success, msg } = res;
                if (!success) {
                    this.message.error(msg || '新增预警失败');
                } else {
                    this.showDetailDialog = false;
                    this.getTableList();
                    this.message.success('新增成功');
                }
            },
            err => {
                console.log('[失败]新增预警', err);
                this.isLoading = false;
            }
        );
    }

    /**
     * 编辑预警
     */
    updateAlarm(): void {
        this.isLoading = true;
        let formData = this.validateForm.getRawValue();
        this.alarmService.updateAlarm(
            {
                ...formData,
                userList: JSON.stringify(formData.userList),
                activeFuncs: formData.activeFuncs.join(','),
                isAutoUpload: formData.isAutoUpload ? 1 : 0
            },
            res => {
                console.log('[成功]编辑预警', res);
                this.isLoading = false;
                const { success, msg } = res;
                if (!success) {
                    this.message.error(msg || '编辑预警失败');
                } else {
                    this.showDetailDialog = false;
                    this.getTableList();
                    this.message.success('编辑成功');
                }
            },
            err => {
                console.log('[失败]编辑预警', err);
                this.isLoading = false;
            }
        );
    }

    /**
     * 删除预警
     * @param id 
     */
    deleteAlarm(id: number): void {
        this.isLoading = true;
        this.alarmService.deleteAlarm(
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
                    this.message.error(msg || '删除预警失败');
                }
            },
            err => {
                console.log('[失败]删除预警失败', err);
                this.isLoading = false;
            }
        );
    }

    /**
     * 监控指标选择回调
     * @param event 
     * @param index 
     */
    handleRuleIndChange(event: string, index: number): void {
        const ruleOpItem = this.ruleIndOptionsList.find(item => item.value === event);
        const ruleItem = this.ruleRules[index];
        if (ruleOpItem) {
            const newRuleItem = {
                ...ruleItem,
                valText: ruleOpItem.valText
            };
            this.ruleRules.splice(index, 1, newRuleItem);
        }
    }

    /**
     * 取值方式选择回调
     * @param event 
     * @param index 
     */
    handleRuleTypeChange(event: string, index: number): void {
        const ruleOpItem = this.ruleOpOptionsList.find(item => item.label === event);
        const ruleItem = this.ruleRules[index];
        if (ruleOpItem) {
            const newRuleItem = {
                ...ruleItem,
                agg: ruleOpItem.agg,
                op: ruleOpItem.op,
                timeSpanText: ruleOpItem.timeSpanText,
            };
            this.ruleRules.splice(index, 1, newRuleItem);
        }
    }

    /**
     * 新增一行预警规则
     */
    handleAddRule(): void {
        const newRuleItem = { timeSpan: null, ind: "", type: "", agg: "", op: "", val: null, interval: null, timeSpanText: ' ', valText: ' ' };
        this.ruleRules.push(newRuleItem);
    }

    /**
     * 删除本行预警规则
     */
    handleRemoveRule(event: EventEmitter<any>, index: number): void {
        this.ruleRules.splice(index, 1);
    }
}
