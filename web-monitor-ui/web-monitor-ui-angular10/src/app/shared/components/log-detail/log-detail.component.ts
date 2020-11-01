import { Component, Input, Output, OnChanges, SimpleChanges, EventEmitter } from '@angular/core';
import * as moment from 'moment';

import { NzMessageService } from 'ng-zorro-antd/message';
import { UserService } from '@data/service/user.service';
import { LogService } from '@data/service/log.service';

@Component({
    selector: 'app-log-detail',
    templateUrl: './log-detail.component.html',
    styleUrls: ['./log-detail.component.scss']
})
export class LogDetailComponent implements OnChanges {

    @Input()
    visible: boolean = false;
    @Output()
    visibleChange = new EventEmitter();
    @Input()
    logType: string;
    @Input()
    errorMsg: string;

    // 筛选条件
    filterForm = {
        pageNum: 1,
        pageSize: 5,
        projectIdentifier: '',
        logType: 'jsErrorLog',
        conditionList: [{ key: 'error_message', value: '', op: '=' }],
    };
    // 选中的tab索引
    selectedTabIndex = 0;
    // 筛选结果列表
    resultList = [];

    constructor(
        private userService: UserService,
        private logService: LogService,
        private message: NzMessageService
    ) { }

    ngOnChanges(changes: SimpleChanges) {
        if (changes['visible']) {
            const { currentValue } = changes['visible'];
            if (currentValue) {
                this.setProjectSelected();
                this.setConditionList();
                this.getInitData();
            } else {
                this.resetData();
            }
        }
    }

    /**
     * 设置用户选择的项目
     */
    setProjectSelected(): void {
        let projectSelected = this.userService.getProjectSelected();
        this.filterForm.projectIdentifier = projectSelected.projectIdentifier;
    }

    // 设置从父组件传递过来的筛选条件
    setConditionList(): void {
        let conditionList = [];
        switch (this.logType) {
            case 'jsErrorLog':
            case 'customErrorLog':
                conditionList = [{ key: 'error_message', value: '', op: '=' }];
                break;
            case 'httpErrorLog':
                conditionList = [{ key: 'http_url_complete', value: '', op: '=' }];
                break;
            case 'resourceLoadErrorLog':
                conditionList = [{ key: 'resource_url', value: '', op: '=' }];
                break;
            default:
                break;
        }
        conditionList[0].value = this.errorMsg;
        this.filterForm.conditionList = conditionList;
    }

    /**
     * 获取详情数据
     */
    getInitData(): void {
        this.logService.getLogListByConditions(
            {
                ...this.filterForm,
                conditionList: JSON.stringify(this.filterForm.conditionList),
                logType: this.logType
            },
            res => {
                console.log('[成功]获取详情数据', res);
                let { success, data, msg } = res;
                if (!success) {
                    this.message.error(msg || '获取详情数据失败');
                } else {
                    const { records } = data;
                    let resultList = [];
                    if (records instanceof Array && records.length > 0) {
                        records.forEach(record => {
                            record.createTime = moment(new Date(record.createTime)).format('YYYY-MM-DD HH:mm:ss')
                        });
                        resultList = records;
                    }
                    this.resultList = resultList;
                }
            },
            err => {
                console.log('[失败]获取详情数据', err);
            }
        );
    }

    /**
     * 重置数据
     */
    resetData(): void {
        this.resultList = [];
        this.selectedTabIndex = 0;
    }

    /**
     * 关闭对话框
     */
    handleModalOnCancel(): void {
        this.visible = false;
        this.visibleChange.emit(this.visible);
    }

}
