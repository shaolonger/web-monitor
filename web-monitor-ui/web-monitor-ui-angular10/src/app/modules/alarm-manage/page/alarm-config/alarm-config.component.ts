import { Component, OnInit, EventEmitter } from '@angular/core';
import * as moment from 'moment';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { NzMessageService } from 'ng-zorro-antd/message';
import { NzModalService } from 'ng-zorro-antd/modal';
import { UserService } from '@data/service/user.service';
import { AlarmService } from '@data/service/alarm.service';
import { SubscriberService } from '@data/service/subscriber.service';

import { Project } from '@data/classes/project.class';
import { Alarm } from '@data/interfaces/alarm.interface';

import { NOTIFY_STATE_MAP } from '@core/constants/alarm-config.const';

@Component({
    selector: 'app-alarm-config',
    templateUrl: './alarm-config.component.html',
    styleUrls: ['./alarm-config.component.scss']
})
export class AlarmConfigComponent implements OnInit {

    isLoading = false;
    projectSelected: Project = null;
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
        { label: '影响用户数', value: 'uvCount', valText: '个', aggList: 'count,avg' },
        { label: '影响用户率', value: 'uvRate', valText: '%', aggList: 'avg' },
        { label: '人均异常次数', value: 'perPV', valText: '个', aggList: 'count,avg' },
        { label: '新增异常数', value: 'newPV', valText: '个', aggList: 'count,avg' },
    ];
    // 监控指标选项列表-全部数据
    ruleOpOptionsFullList = [
        { label: '最近N分钟总和大于', agg: 'count', op: '>', timeSpanSize: 1, interval: 1, timeSpanText: '分钟' },
        { label: '最近N天总和大于', agg: 'count', op: '>', timeSpanSize: 1440, interval: 1440, timeSpanText: '天' },
        { label: '最近N分钟平均值大于', agg: 'avg', op: '>', timeSpanSize: 1, interval: 1, timeSpanText: '分钟' },
        { label: '最近N天平均值大于', agg: 'avg', op: '>', timeSpanSize: 1440, interval: 1440, timeSpanText: '天' },
        { label: '最近N分钟平均值环比上涨大于', agg: 'avg', op: '>', timeSpanSize: 1, interval: 1, timeSpanText: '分钟' },
        { label: '最近N分钟总和环比上涨大于', agg: 'count', op: '>', timeSpanSize: 1, interval: 1, timeSpanText: '分钟' },
        { label: '最近N小时平均值与昨天同比上涨大于', agg: 'avg', op: 'd_up', timeSpanSize: 60, interval: 60, timeSpanText: '小时' },
        { label: '最近N小时平均值与上周同比上涨大于', agg: 'avg', op: 'w_up', timeSpanSize: 60, interval: 60, timeSpanText: '小时' },
    ];
    // 监控指标选项列表-实际显示
    ruleOpOptionsList = [];
    // 监控条件选择结果
    ruleOp = '&&';
    ruleRules = [
        { timeSpan: null, timeSpanSize: 1, ind: "", type: "", agg: "", op: "", val: null, interval: null, timeSpanText: ' ', valText: ' ' }
    ];
    validateForm!: FormGroup;

    constructor(
        private userService: UserService,
        private alarmService: AlarmService,
        private subscriberService: SubscriberService,
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
        this.projectSelected = projectSelected;

        // 设置filterForm
        this.filterForm.projectIdentifier = this.projectSelected.projectIdentifier;
    }

    /**
     * 表单数据初始化
     */
    initFormData(): void {
        if (!this.validateForm) {
            this.validateForm = this.fb.group({
                id: [0],
                name: ['', [Validators.required]],
                projectIdentifier: [this.projectSelected.projectIdentifier],
                level: [0, [Validators.required]],
                category: [0, [Validators.required]],
                startTime: [null],
                endTime: [null],
                silentPeriod: [0, [Validators.required]],
                isActive: [true, [Validators.required]],
                subscriberList: [[], [Validators.required]]
            });
        } else {
            this.validateForm.patchValue({
                ...this.validateForm.getRawValue(),
                id: 0,
                name: '',
                projectIdentifier: this.projectSelected.projectIdentifier,
                level: 0,
                category: 0,
                startTime: null,
                endTime: null,
                silentPeriod: 0,
                isActive: true,
                subscriberList: []
            });
            this.ruleOp = '&&';
            this.ruleRules = [
                { timeSpan: null, timeSpanSize: 1, ind: "", type: "", agg: "", op: "", val: null, interval: null, timeSpanText: ' ', valText: ' ' }
            ];
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
                    this.listData = records.map(item => {
                        const rule = JSON.parse(item.rule);
                        const subscriberList = JSON.parse(item.subscriberList);
                        if ((typeof rule !== 'object') || !(subscriberList instanceof Array)) {
                            return {
                                ...item,
                                categoryText: '',
                                silentPeriodText: '',
                                levelText: '',
                                ruleOperatorText: '',
                                ruleTextList: [],
                            };
                        }
                        return {
                            ...item,
                            categoryText: this.getOptionLabelByVal(item.category, this.categoryOptionsList),
                            startTimeText: item.startTime.substring(0, 5),
                            endTimeText: item.endTime.substring(0, 5),
                            silentPeriodText: this.getOptionLabelByVal(item.silentPeriod, this.silentPeriodOptionsList),
                            levelText: this.getOptionLabelByVal(item.level, this.ruleLevelOptionsList),
                            ruleOperatorText: this.getOptionLabelByVal(rule.op, this.ruleOperatorOptionsList),
                            ruleTextList: this.getRuleTextListByRules(rule.rules),
                            subscriberActiveList: subscriberList.filter(item => item.isActive === 1).map(item => item.category),
                            expand: false,
                            filterForm: {
                                pageNum: 1,
                                pageSize: 10,
                                alarmId: item.id
                            },
                            paginationConfig: {
                                total: 0
                            },
                            children: []
                        };
                    });
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
            this.setViewFormBySubmitForm(data);
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
     * 获取提交的表单数据
     */
    getSubmitFormFromViewForm(): any {

        // 校验表单
        const isRuleNotFinish = this.ruleRules.some(item => {
            return !item.ind || !item.agg || !item.op || !item.timeSpan || !item.val || !item.interval;
        });
        if (isRuleNotFinish) {
            this.message.error('报警条件未填写完整');
            return null;
        }

        const rawValue = this.validateForm.getRawValue();
        const formData = {
            id: rawValue.id,
            name: rawValue.name,
            projectIdentifier: rawValue.projectIdentifier,
            level: rawValue.level,
            rule: {},
            category: rawValue.category,
            startTime: '',
            endTime: '',
            silentPeriod: rawValue.silentPeriod,
            isActive: rawValue.isActive ? 1 : 0,
            subscriberList: ''
        };

        // 设置报警规则
        const rule = {
            op: this.ruleOp,
            rules: this.ruleRules.map(item => ({
                ind: item.ind,
                agg: item.agg,
                op: item.op,
                timeSpan: item.timeSpan * item.timeSpanSize,
                val: item.val,
                interval: item.interval,
            }))
        };
        formData.rule = JSON.stringify(rule);

        // 报警时段-开始时间
        if (rawValue.startTime) {
            formData.startTime = moment(rawValue.startTime).format('HH:mm') + ':00';
        }

        // 报警时段-结束时间
        if (rawValue.endTime) {
            formData.endTime = moment(rawValue.endTime).format('HH:mm') + ':59';
        }

        // 设置通知方式
        const subscriberList = [
            {
                subscriber: this.projectSelected.notifyDtToken, // TODO 这里将来要改成可选择指定的钉钉机器人
                category: 1, // 钉钉机器人
                isActive: rawValue.subscriberList.includes(1) ? 1 : 0
            },
            {
                subscriber: this.projectSelected.notifyEmail, // TODO 这里将来要改成可选择指定的邮箱
                category: 2, // 邮箱
                isActive: rawValue.subscriberList.includes(2) ? 1 : 0
            },
        ];
        formData.subscriberList = JSON.stringify(subscriberList);

        return formData;
    }

    /**
     * 根据详情数据设置页面展示数据
     * @param data 
     */
    setViewFormBySubmitForm(data: Alarm): any {

        // 设置报警规则
        const ruleObj = JSON.parse(data.rule);
        if (typeof ruleObj === 'object') {
            this.ruleOp = ruleObj.op;
            let ruleRules = [];
            if (ruleObj.rules instanceof Array && ruleObj.rules.length > 0) {
                ruleRules = ruleObj.rules.map(rule => {
                    const ruleIndOption = this.ruleIndOptionsList.find(item => item.value === rule.ind);
                    const ruleOpOption = this.ruleOpOptionsFullList.find(item => {
                        return item.agg === rule.agg && item.op === rule.op && item.interval === rule.interval;
                    });
                    return {
                        timeSpan: rule.timeSpan / ruleOpOption.timeSpanSize,
                        timeSpanSize: ruleOpOption.timeSpanSize,
                        ind: rule.ind,
                        type: ruleOpOption.label,
                        agg: rule.agg,
                        op: rule.op,
                        val: rule.val,
                        interval: rule.interval,
                        timeSpanText: ruleOpOption.timeSpanText,
                        valText: ruleIndOption.valText
                    };
                });
            }
            this.ruleRules = ruleRules;
        }

        // 设置表单
        const subscriberList = JSON.parse(data.subscriberList);
        const nowTimeStr = moment(new Date()).format('YYYY-MM-DD ');
        this.validateForm.patchValue({
            ...this.validateForm.getRawValue(),
            ...data,
            startTime: new Date(nowTimeStr + data.startTime),
            endTime: new Date(nowTimeStr + data.endTime),
            subscriberList: subscriberList.filter(item => item.isActive === 1).map(item => item.category)
        });
        this.validateForm.enable();
    }

    /**
     * 新增预警
     */
    addAlarm(): void {
        const formData = this.getSubmitFormFromViewForm();
        if (formData === null) {
            return;
        }
        this.isLoading = true;
        this.alarmService.addAlarm(
            formData,
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
        const formData = this.getSubmitFormFromViewForm();
        if (formData === null) {
            return;
        }
        this.alarmService.updateAlarm(
            formData,
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
                type: '',
                timeSpan: null,
                val: null,
                interval: null,
                timeSpanSize: 1,
                valText: ruleOpItem.valText
            };
            this.ruleRules.splice(index, 1, newRuleItem);
        }
    }

    /**
     * 判断取值方式的选项是否显示
     * @param option 
     * @param rule 
     */
    isOpOptionDisabled(option, rule) {
        if (!rule.ind) {
            return true;
        }
        const ruleOpItem = this.ruleIndOptionsList.find(item => item.value === rule.ind);
        if (ruleOpItem) {
            return ruleOpItem.aggList.indexOf(option.agg) === -1;
        } else {
            return true;
        }
    }

    /**
     * 取值方式选择回调
     * @param event 
     * @param index 
     */
    handleRuleTypeChange(event: string, index: number): void {
        const ruleOpItem = this.ruleOpOptionsFullList.find(item => item.label === event);
        const ruleItem = this.ruleRules[index];
        if (ruleOpItem) {
            const newRuleItem = {
                ...ruleItem,
                agg: ruleOpItem.agg,
                op: ruleOpItem.op,
                interval: ruleOpItem.interval,
                timeSpanText: ruleOpItem.timeSpanText,
                timeSpanSize: ruleOpItem.timeSpanSize,
                timeSpan: null,
                val: null,
            };
            this.ruleRules.splice(index, 1, newRuleItem);
        }
    }

    /**
     * 新增一行预警规则
     */
    handleAddRule(): void {
        const newRuleItem = { timeSpan: null, timeSpanSize: 1, ind: "", type: "", agg: "", op: "", val: null, interval: null, timeSpanText: ' ', valText: ' ' };
        this.ruleRules.push(newRuleItem);
    }

    /**
     * 删除本行预警规则
     */
    handleRemoveRule(event: EventEmitter<any>, index: number): void {
        this.ruleRules.splice(index, 1);
    }

    /**
     * 根据选项值获取文本
     * @param value 
     * @param optionList 
     */
    getOptionLabelByVal(value: string | number, optionList: Array<any>): string {
        const option = optionList.find(item => item.value === value);
        let label = '';
        if (option) {
            label = option.label;
        }
        return label;
    }

    /**
     * 改变预案启动状态
     * @param isActive 
     * @param data 
     */
    handleChangeIsActive(isActive: boolean, data: any): void {
        const { id } = data;
        const newIsActive = isActive ? 1 : 0;
        this.alarmService.updateAlarm(
            {
                id,
                isActive: newIsActive
            },
            res => {
                console.log('[成功]改变预案启动状态', res);
                const { success, msg } = res;
                if (!success) {
                    this.message.error(msg || '操作失败');
                } else {
                    this.message.success('操作成功');
                }
            },
            err => {
                console.log('[失败]改变预案启动状态', err);
                this.message.error(err.msg || '操作失败');
            }
        );
    }

    /**
     * 将后台存放的预警规则字符串，转换为页面显示的列表
     * @param list 
     */
    getRuleTextListByRules(list: Array<any>): Array<string> {
        if (!(list instanceof Array) || list.length === 0) {
            return [];
        }
        return list.map(rule => {
            const ruleIndOption = this.ruleIndOptionsList.find(item => item.value === rule.ind);
            const ruleOpOption = this.ruleOpOptionsFullList.find(item => {
                return item.agg === rule.agg && item.op === rule.op && item.interval === rule.interval;
            });
            let returnStr = '';
            if (ruleIndOption && ruleOpOption) {
                returnStr = `${ruleIndOption.label}-${ruleOpOption.label}${rule.val}${ruleIndOption.valText}`;
                returnStr = returnStr.replace('N', String(rule.timeSpan / ruleOpOption.timeSpanSize));
            }
            return returnStr;
        });
    }

    /**
     * 行展开或折叠
     * @param event 
     * @param row 
     */
    handleAlarmRowExpand(event: boolean, row: any): void {
        if (event) {
            this.getAlarmRecordList(row);
            // 展开本行时，将其他行折叠起来，一次只能有一行展开
            // this.listData.forEach((item: any) => {
            //     if (item !== row) {
            //         item.expand = false;
            //     }
            // });
        }
    }

    /**
     * 获取预警记录列表
     * @param row 
     */
    getAlarmRecordList(row: any): void {
        this.isLoading = true;
        this.alarmService.getAlarmRecord(
            row.filterForm,
            res => {
                console.log('[成功]获取预警记录列表', res);
                this.isLoading = false;
                const { success, data, msg } = res;
                if (!success) {
                    this.message.error(msg || '获取预警记录列表失败');
                } else {
                    let { records, totalNum } = data;
                    row.children = records.map(item => ({
                        ...item,
                        createTimeText: moment(item.createTime).format("YYYY-MM-DD HH:mm:ss"),
                        expand: false,
                        filterForm: {
                            pageNum: 1,
                            pageSize: 10,
                            alarmRecordId: item.id
                        },
                        paginationConfig: {
                            total: 0
                        },
                        children: []
                    }));
                    row.paginationConfig = { total: totalNum };
                }
            },
            err => {
                console.log('[失败]获取预警记录列表', err);
                this.isLoading = false;
            }
        );
    }

    /**
     * 报警记录行展开或折叠
     * @param event 
     * @param row 
     */
    handleNotifyRowExpand(event: boolean, row: any): void {
        if (event) {
            this.getNotifyRecordList(row);
            // 展开本行时，将其他行折叠起来，一次只能有一行展开
            // this.listData.forEach((item: any) => {
            //     if (item !== row) {
            //         item.expand = false;
            //     }
            // });
        }
    }

    /**
     * 获取报警记录列表
     * @param row 
     */
    getNotifyRecordList(row: any): void {
        this.isLoading = true;
        this.subscriberService.getSubscriberNotifyRecord(
            row.filterForm,
            res => {
                console.log('[成功]获取报警记录列表', res);
                this.isLoading = false;
                const { success, data, msg } = res;
                if (!success) {
                    this.message.error(msg || '获取预警记录列表失败');
                } else {
                    let { records, totalNum } = data;
                    row.children = records.map(item => ({
                        ...item,
                        createTimeText: moment(item.createTime).format("YYYY-MM-DD HH:mm:ss"),
                        stateText: NOTIFY_STATE_MAP[item.state]
                    }));
                    row.paginationConfig = { total: totalNum };
                }
            },
            err => {
                console.log('[失败]获取报警记录列表', err);
                this.isLoading = false;
            }
        );
    }
}
