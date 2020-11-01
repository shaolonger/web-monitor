import { Component, OnInit } from '@angular/core';
import * as moment from 'moment';

import { NzMessageService } from 'ng-zorro-antd/message';
import { EventService } from '@app/service/event.service';
import { UserService } from '@data/service/user.service';
import { LogService } from '@data/service/log.service';

import { EventModel } from '@data/classes/event.class';

@Component({
    selector: 'app-advanced-search-container',
    templateUrl: './advanced-search-container.component.html',
    styleUrls: ['./advanced-search-container.component.scss']
})
export class AdvancedSearchContainerComponent implements OnInit {

    // 加载状态
    isLoading = false;
    // 查询条件
    filterForm = {
        pageNum: 1,
        pageSize: 10,
        logType: 'jsErrorLog',
        projectIdentifier: '',
        startTime: '',
        endTime: '',
        conditionList: []
    };
    logTypeList = [
        { label: 'JS异常', value: 'jsErrorLog' },
        { label: 'HTTP异常', value: 'httpErrorLog' },
        { label: '静态资源异常', value: 'resourceLoadErrorLog' },
        { label: '自定义异常', value: 'customErrorLog' },
    ];
    timeRangeOptions = {};
    timeRangePicker: Date[];
    conditionList = [];
    conditionKeyList = [];
    conditionKeyListBasic = [
        { label: '用户名', value: 'user_name' },
        { label: '页面Url', value: 'page_url' },
        { label: '设备名称', value: 'device_name' },
        { label: '操作系统', value: 'os' },
        { label: '操作系统版本', value: 'os_version' },
        { label: '客户端名称', value: 'browser_name' },
        { label: '客户端版本', value: 'browser_version' },
        { label: 'IP地址', value: 'ip_address' },
        { label: '地址', value: 'address' },
        { label: '网络类型', value: 'net_type' },
    ];
    conditionKeyListJsErrorLog = [
        { label: '异常信息', value: 'error_message' },
        { label: '异常堆栈', value: 'error_stack' },
    ];
    conditionKeyListHttpErrorLog = [
        { label: '请求地址', value: 'http_url_complete' },
        { label: '请求方法', value: 'http_method' },
        { label: '状态码', value: 'status' },
    ];
    conditionKeyListResourceLoadErrorLog = [
        { label: '资源地址', value: 'resource_url' },
        { label: '资源类型', value: 'resource_type' },
    ];
    conditionKeyListCustomErrorLog = [
        { label: '异常信息', value: 'error_message' },
        { label: '异常堆栈', value: 'error_stack' },
    ];
    conditionOpList = [
        { label: '等于', value: '=' },
        { label: '大于', value: '>' },
        { label: '大于等于', value: '>=' },
        { label: '小于', value: '<' },
        { label: '小于等于', value: '<=' },
        { label: '不等于', value: '!=' },
        { label: '包含', value: 'wildcard' },
    ];
    // 日志记录列表
    logRecordList = [];
    // 分页控制器
    paginationConfig = {
        total: 0
    };
    // 是否显示详情页
    isShowDetail = false;
    // 传递给详情页的查询信息
    errorMsg: string;

    constructor(
        private userService: UserService,
        private eventService: EventService,
        private logService: LogService,
        private message: NzMessageService
    ) { }

    ngOnInit(): void {
        this.init();
    }

    /**
     * 绑定事件监听器
     */
    attachEventListener(): void {
        this.eventService.eventEmitter.subscribe((event: EventModel) => {
            let { eventName, eventPayload } = event;
            if (eventName === 'projectSelectedChanged') {
                this.filterForm.projectIdentifier = eventPayload.projectIdentifier;
                this.setProjectSelected();
            }
        });
    }

    /**
     * 设置用户选择的项目
     */
    setProjectSelected(): void {
        let projectSelected = this.userService.getProjectSelected();
        this.filterForm.projectIdentifier = projectSelected.projectIdentifier;
    }

    /**
     * 初始化
     */
    init(): void {
        this.setProjectSelected();
        this.initTimeRange();
        this.setTimeRangeOptions();
        this.conditionKeyList = [
            ...this.conditionKeyListBasic,
            ...this.conditionKeyListJsErrorLog
        ];
    }

    /**
     * 设置时间选择器预设范围
     */
    setTimeRangeOptions(): void {
        const nowTime = new Date();
        this.timeRangeOptions = {
            '近5分钟': [moment(nowTime).add(-5, 'minutes').toDate(), nowTime],
            '近10分钟': [moment(nowTime).add(-10, 'minutes').toDate(), nowTime],
            '近30分钟': [moment(nowTime).add(-30, 'minutes').toDate(), nowTime],
            '近1小时': [moment(nowTime).add(-1, 'hours').toDate(), nowTime],
            '近1天': [moment(nowTime).add(-1, 'days').toDate(), nowTime],
            '近3天': [moment(nowTime).add(-3, 'days').toDate(), nowTime],
            '近7天': [moment(nowTime).add(-7, 'days').toDate(), nowTime],
        };
    }

    /**
     * 表格数据-初始化时间范围
     */
    initTimeRange() {
        let startTime = new Date(moment(new Date()).format('YYYY-MM-DD') + ' 00:00:00');
        let endTime = new Date();
        this.timeRangePicker = [startTime, endTime];
        this.onSelectTimeRange([startTime, endTime]);
    }

    /**
     * 切换监控指标
     */
    handleSwitchLogType(): void {
        this.conditionList = [];
        let conditionKeyTargetList = [];
        switch (this.filterForm.logType) {
            case 'jsErrorLog':
                conditionKeyTargetList = this.conditionKeyListJsErrorLog;
                break;
            case 'httpErrorLog':
                conditionKeyTargetList = this.conditionKeyListHttpErrorLog;
                break;
            case 'resourceLoadErrorLog':
                conditionKeyTargetList = this.conditionKeyListResourceLoadErrorLog;
                break;
            case 'customErrorLog':
                conditionKeyTargetList = this.conditionKeyListCustomErrorLog;
                break;
            default:
                break;
        }
        this.conditionKeyList = [
            ...this.conditionKeyListBasic,
            ...conditionKeyTargetList
        ];
    }

    /**
     * 增加查询参数
     */
    addCondition(): void {
        this.conditionList.push({
            key: '', value: '', op: ''
        });
    }

    /**
     * 删除查询参数
     * @param i 索引
     */
    delCondition(i: number): void {
        this.conditionList.splice(i, 1);
    }

    /**
     * 表格数据-选择日期查询范围
     * @param result 日期范围
     */
    onSelectTimeRange(result: Date[]): void {
        this.filterForm = {
            ...this.filterForm,
            startTime: result[0] ? moment(result[0]).format('YYYY-MM-DD HH:mm:ss') : '',
            endTime: result[1] ? moment(result[1]).format('YYYY-MM-DD HH:mm:ss') : '',
        };
    }

    /**
     * 多条件高级查询
     */
    getTableList(): void {
        this.isLoading = true;
        this.logService.getLogListByConditions(
            {
                ...this.filterForm,
                conditionList: JSON.stringify(this.conditionList)
            },
            res => {
                console.log('[成功]多条件高级查询', res);
                this.isLoading = false;
                let { success, data, msg } = res;
                if (!success) {
                    this.message.error(msg || '多条件高级查询失败');
                } else {
                    let { records, totalNum } = data;
                    const keyMap = {
                        'jsErrorLog': 'errorMessage',
                        'httpErrorLog': 'httpUrlComplete',
                        'resourceLoadErrorLog': 'resourceUrl',
                        'customErrorLog': 'errorMessage',
                    };
                    const key = keyMap[this.filterForm.logType];
                    this.logRecordList = records.map(item => ({
                        ...item,
                        content: item[key],
                        createTimeStr: item.createTime ? moment(new Date(item.createTime)).format('YYYY-MM-DD HH:mm:ss') : '',
                    }));
                    this.paginationConfig.total = totalNum;
                }
            },
            err => {
                console.log('[失败]多条件高级查询', err);
                this.isLoading = false;
            }
        );
    }

    /**
     * 查看日志
     * @param errorMessage 异常内容
     */
    handleShowDetail(errorMessage: string): void {
        this.errorMsg = errorMessage;
        this.isShowDetail = true;
    }

}
