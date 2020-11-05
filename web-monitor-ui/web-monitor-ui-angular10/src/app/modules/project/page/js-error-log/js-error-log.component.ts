import { Component, OnInit } from '@angular/core';
import * as moment from 'moment';
import { EChartOption } from 'echarts';

import { NzMessageService } from 'ng-zorro-antd/message';
import { EventService } from '@core/service/event.service';
import { UserService } from '@data/service/user.service';
import { LogService } from '@data/service/log.service';

import { UserRelatedProject } from '@data/classes/project.class';
import { EventModel } from '@data/classes/event.class';
import { LogRecordList } from '@data/interfaces/log.interface';

@Component({
    selector: 'app-js-error-log',
    templateUrl: './js-error-log.component.html',
    styleUrls: ['./js-error-log.component.scss']
})
export class JsErrorLogComponent implements OnInit {

    // 加载状态
    isLoading = false;
    // 项目标识
    projectIdentifier = '';
    // 总计数据
    totalData = {
        count: {
            yesterday: 0,
            today: 0,
            change: 1,
            rate: '-'
        },
        affectUV: {
            yesterday: 0,
            today: 0,
            change: 1,
            rate: '-'
        },
        affectUVPercent: {
            yesterday: '20%',
            today: '10%',
            change: 1,
            rate: '-'
        },
    };
    // 日志时间分布图表筛选条件
    logTimeChartFilterForm = {
        projectIdentifier: '',
        startTime: '',
        endTime: '',
        logTypeList: 'jsErrorLog',
        indicatorList: 'count',
        timeInterval: 60
    };
    chartTimeRangeModel = [];
    // 时间粒度可选列表
    timeIntervalOptionList = [
        { label: '分钟', value: 60 },
        { label: '小时', value: 3600 },
        { label: '天', value: 86400 },
    ];
    // 图表-JS异常
    logTimeChartOptionJS: EChartOption = {
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                label: {
                    backgroundColor: '#6a7985'
                }
            }
        },
        grid: {
            top: 30,
            left: 30,
            right: 30,
            bottom: 30
        },
        xAxis: [
            {
                type: 'category',
                boundaryGap: false,
                axisLabel: {
                    formatter: value => value.substr(-8, 5)
                },
                data: []
            }
        ],
        yAxis: [
            {
                type: 'value',
                minInterval: 1,
                splitLine: {
                    show: true,
                    color: '#eeeeee',
                    lineStyle: {
                        type: 'dashed'
                    }
                }
            }
        ],
        color: ['#409EFF'],
        series: [
            {
                name: 'JS异常',
                type: 'line',
                stack: '总量',
                areaStyle: {},
                data: []
            },
        ]
    };
    // 表格筛选条件
    tableFilterForm = {
        pageNum: 1,
        pageSize: 10,
        projectIdentifier: '',
        startTime: '',
        endTime: ''
    };
    // 分页控制器
    paginationConfig = {
        total: 0
    };
    // 项目列表
    projectList: UserRelatedProject[] = [];
    // 表格时间筛选日历结果
    timeRangePicker: Date[];
    // 表格时间筛选列表
    timeIntervalList = [
        { label: '近1小时', value: '近1小时' },
        { label: '今日', value: '今日' },
        { label: '近7日', value: '近7日' },
    ];
    timeRange = '';
    // 日志记录列表
    logRecordList: LogRecordList[] = [];
    // 分布统计图表筛选条件
    baseDistributionChartFilterForm = {
        projectIdentifier: '',
        startTime: '',
        endTime: '',
        logType: 'jsErrorLog',
        indicator: ''
    };
    // 图表-设备类型
    chartOptionDeviceName: EChartOption = {
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        graphic: {
            type: 'text',
            top: 'center',
            left: 'center',
            style: {
                text: '',
                fill: '#333333',
                font: 'bolder 20px "Microsoft YaHei", sans-serif'
            },
        },
        grid: {
            top: 10,
            left: 10,
            right: 10,
            bottom: 10
        },
        color: ['#668ED6', '#60ACFC', '#23C2DB', '#32D3EB', '#9CDC82', '#D4EC59', '#FFE168'],
        series: [
            {
                name: '设备类型',
                type: 'pie',
                radius: ['60%', '80%'],
                stack: '总量',
                label: {
                    position: 'outer',
                    alignTo: 'none',
                    bleedMargin: 5
                },
                emphasis: {
                    label: {
                        show: true,
                        fontSize: '18',
                        fontWeight: 'bold'
                    }
                },
                areaStyle: {},
                data: []
            },
        ]
    };
    // 图表-操作系统
    chartOptionOs: EChartOption = {
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        graphic: {
            type: 'text',
            top: 'center',
            left: 'center',
            style: {
                text: '',
                fill: '#333333',
                font: 'bolder 20px "Microsoft YaHei", sans-serif'
            },
        },
        grid: {
            top: 10,
            left: 10,
            right: 10,
            bottom: 10
        },
        color: ['#668ED6', '#60ACFC', '#23C2DB', '#32D3EB', '#9CDC82', '#D4EC59', '#FFE168'],
        series: [
            {
                name: '操作系统',
                type: 'pie',
                radius: ['60%', '80%'],
                stack: '总量',
                label: {
                    position: 'outer',
                    alignTo: 'none',
                    bleedMargin: 5
                },
                emphasis: {
                    label: {
                        show: true,
                        fontSize: '18',
                        fontWeight: 'bold'
                    }
                },
                areaStyle: {},
                data: []
            },
        ]
    };
    // 图表-浏览器
    chartOptionBrowserName: EChartOption = {
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        graphic: {
            type: 'text',
            top: 'center',
            left: 'center',
            style: {
                text: '',
                fill: '#333333',
                font: 'bolder 20px "Microsoft YaHei", sans-serif'
            },
        },
        grid: {
            top: 10,
            left: 10,
            right: 10,
            bottom: 10
        },
        color: ['#668ED6', '#60ACFC', '#23C2DB', '#32D3EB', '#9CDC82', '#D4EC59', '#FFE168'],
        series: [
            {
                name: '浏览器',
                type: 'pie',
                radius: ['60%', '80%'],
                stack: '总量',
                label: {
                    position: 'outer',
                    alignTo: 'none',
                    bleedMargin: 5
                },
                emphasis: {
                    label: {
                        show: true,
                        fontSize: '18',
                        fontWeight: 'bold'
                    }
                },
                areaStyle: {},
                data: []
            },
        ]
    };
    // 图表-网络类型
    chartOptionNetType: EChartOption = {
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        graphic: {
            type: 'text',
            top: 'center',
            left: 'center',
            style: {
                text: '',
                fill: '#333333',
                font: 'bolder 20px "Microsoft YaHei", sans-serif'
            },
        },
        grid: {
            top: 10,
            left: 10,
            right: 10,
            bottom: 10
        },
        color: ['#668ED6', '#60ACFC', '#23C2DB', '#32D3EB', '#9CDC82', '#D4EC59', '#FFE168'],
        series: [
            {
                name: '网络类型',
                type: 'pie',
                radius: ['60%', '80%'],
                stack: '总量',
                label: {
                    position: 'outer',
                    alignTo: 'none',
                    bleedMargin: 5
                },
                emphasis: {
                    label: {
                        show: true,
                        fontSize: '18',
                        fontWeight: 'bold'
                    }
                },
                areaStyle: {},
                data: []
            },
        ]
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
        this.attachEventListener();
        this.setInitData();
        this.getTotalData();
        this.getLogTimeChartData(this.logTimeChartFilterForm);
    }

    /**
     * 绑定事件监听器
     */
    attachEventListener(): void {
        this.eventService.eventEmitter.subscribe((event: EventModel) => {
            let { eventName, eventPayload } = event;
            if (eventName === 'projectSelectedChanged') {
                this.projectIdentifier = eventPayload.projectIdentifier;
                this.setInitData();
                this.getTotalData();
                this.getLogTimeChartData(this.logTimeChartFilterForm);
                this.getTableList();
                this.getDistributionChartData();
            }
        });
    }

    // 初始化数据
    setInitData(): void {
        this.setProjectSelected();
        this.setChartInitData();
        this.setTableInitData();
    }

    /**
     * 设置用户选择的项目
     */
    setProjectSelected(): void {
        let projectSelected = this.userService.getProjectSelected();
        this.projectIdentifier = projectSelected.projectIdentifier;
    }

    /**
     * 初始化图表筛选条件
     */
    setChartInitData(): void {
        this.chartTimeRangeModel = [new Date(), new Date()];
        let startTime = moment(new Date()).format('YYYY-MM-DD') + ' 00:00:00';
        let endTime = moment(new Date()).add(1, 'day').format('YYYY-MM-DD') + ' 00:00:00';
        this.logTimeChartFilterForm = {
            ...this.logTimeChartFilterForm,
            projectIdentifier: this.projectIdentifier,
            startTime,
            endTime
        };
    }

    /**
     * 设置表格筛选条件
     */
    setTableInitData(): void {
        // 初始化表格数据时间范围
        this.initTableTimeRange();
        // 初始化列表筛选条件
        this.tableFilterForm = {
            ...this.tableFilterForm,
            projectIdentifier: this.projectIdentifier,
        };
    }

    /**
     * 获取今天、昨天对比数据
     */
    getTotalData(): void {
        let startTime = moment(new Date()).add(-1, 'day').format('YYYY-MM-DD') + ' 00:00:00';
        let endTime = moment(new Date()).add(1, 'day').format('YYYY-MM-DD') + ' 00:00:00';
        let formData = {
            projectIdentifier: this.projectIdentifier,
            startTime, endTime, logTypeList: 'jsErrorLog', indicatorList: 'count,uv', timeInterval: 86400
        };
        this.getLogCountBetweenDiffDate(
            formData,
            data => {
                this.setChartTotal(data);
            }
        );
    }

    /**
     * 设置图表标题总计数据
     * @param data 
     */
    setChartTotal(data: any): void {
        let { jsErrorLog } = data;
        let yesterday = jsErrorLog[0];
        let today = jsErrorLog[1];
        let affectUVPercentYesterday = yesterday.count === 0 ? '-' : `${((yesterday.uv / yesterday.count) * 100).toFixed(2)}%`;
        let affectUVPercentToday = today.count === 0 ? '-' : `${((today.uv / today.count) * 100).toFixed(2)}%`;
        let affectUVPercentChange = 0;
        let affectUVPercentRate = '-';
        if (affectUVPercentYesterday !== '-' && affectUVPercentToday !== '-') {
            let affectUVPercentYesterdayRate = yesterday.uv / yesterday.count;
            let affectUVPercentTodayRate = today.uv / today.count;
            affectUVPercentChange = (affectUVPercentTodayRate !== affectUVPercentYesterdayRate) && (affectUVPercentTodayRate > affectUVPercentYesterdayRate ? 1 : -1);
            affectUVPercentRate = affectUVPercentYesterdayRate === 0 ? '-' : `${(((affectUVPercentTodayRate - affectUVPercentYesterdayRate) / affectUVPercentYesterdayRate) * 100).toFixed(2)}%`;
        }
        let totalData = {
            count: {
                yesterday: yesterday.count,
                today: today.count,
                change: (today.count !== yesterday.count) && (today.count > yesterday.count ? 1 : -1),
                rate: yesterday.count === 0 ? '-' : `${(((today.count - yesterday.count) / yesterday.count) * 100).toFixed(2)}%`
            },
            affectUV: {
                yesterday: yesterday.uv,
                today: today.uv,
                change: (today.uv !== yesterday.uv) && (today.uv > yesterday.uv ? 1 : -1),
                rate: yesterday.uv === 0 ? '-' : `${(((today.uv - yesterday.uv) / yesterday.uv) * 100).toFixed(2)}%`
            },
            affectUVPercent: {
                yesterday: affectUVPercentYesterday,
                today: affectUVPercentToday,
                change: affectUVPercentChange,
                rate: affectUVPercentRate
            },
        };
        this.totalData = totalData;
    }

    /**
     * 获取详细数据
     * @param formData 
     */
    getLogTimeChartData(formData: Object): void {
        this.getLogCountBetweenDiffDate(
            formData,
            data => {
                this.setLogTimeChartsOption(data);
            }
        );
    }

    /**
     * 设置图表
     * @param data 
     */
    setLogTimeChartsOption(data: any): void {
        let { jsErrorLog } = data;
        // 设置JS异常图表
        this.logTimeChartOptionJS.xAxis[0].data = jsErrorLog.map(item => item.key);
        this.logTimeChartOptionJS.series[0].data = jsErrorLog.map(item => item.count);
        this.logTimeChartOptionJS = { ...this.logTimeChartOptionJS };
    }

    /**
     * 获取分布统计图表
     */
    getDistributionChartData(): void {
        const indicatorList = ['device_name', 'os', 'browser_name', 'net_type'];
        indicatorList.forEach(indicator => {
            const { projectIdentifier, startTime, endTime } = this.tableFilterForm;
            this.logService.getLogDistributionBetweenDiffDate(
                {
                    ...this.baseDistributionChartFilterForm,
                    projectIdentifier,
                    startTime,
                    endTime,
                    indicator
                },
                res => {
                    console.log('[成功]获取分布统计图表', res);
                    let { success, data, msg } = res;
                    if (!success) {
                        this.message.error(msg || '获取分布统计图表失败');
                        return;
                    }
                    this.setDistributionChartsOption(indicator, data);
                },
                err => {
                    console.log('[失败]获取分布统计图表', err);
                }
            );
        });
    }

    /**
     * 设置图表
     * @param data 
     */
    setDistributionChartsOption(indicator: string, data: any): void {
        switch (indicator) {
            case 'device_name':
                this.chartOptionDeviceName.series[0].data = data.map(item => ({
                    name: item.key,
                    value: item.count
                }));
                this.chartOptionDeviceName.graphic.style.text = data.reduce((max, cur) => max + cur.count, 0);
                this.chartOptionDeviceName = { ...this.chartOptionDeviceName };
                break;
            case 'os':
                this.chartOptionOs.series[0].data = data.map(item => ({
                    name: item.key,
                    value: item.count
                }));
                this.chartOptionOs.graphic.style.text = data.reduce((max, cur) => max + cur.count, 0);
                this.chartOptionOs = { ...this.chartOptionOs };
                break;
            case 'browser_name':
                this.chartOptionBrowserName.series[0].data = data.map(item => ({
                    name: item.key,
                    value: item.count
                }));
                this.chartOptionBrowserName.graphic.style.text = data.reduce((max, cur) => max + cur.count, 0);
                this.chartOptionBrowserName = { ...this.chartOptionBrowserName };
                break;
            case 'net_type':
                this.chartOptionNetType.series[0].data = data.map(item => ({
                    name: item.key,
                    value: item.count
                }));
                this.chartOptionNetType.graphic.style.text = data.reduce((max, cur) => max + cur.count, 0);
                this.chartOptionNetType = { ...this.chartOptionNetType };
                break;
            default:
                break;
        }
    }

    /**
     * 获取两个日期之间的对比数据
     * @param formData 
     */
    getLogCountBetweenDiffDate(formData: Object, successCallback: Function): void {
        this.isLoading = true;
        this.logService.getLogCountBetweenDiffDate(
            formData,
            res => {
                console.log('[成功]获取两个日期之间的对比数据', res);
                this.isLoading = false;
                let { success, data, msg } = res;
                if (!success) {
                    this.message.error(msg || '获取两个日期之间的对比数据失败');
                } else {
                    successCallback && successCallback(data);
                }
            },
            err => {
                console.log('[失败]获取两个日期之间的对比数据', err);
                this.isLoading = false;
            }
        );
    }

    /**
     * 图表-选择日期查询范围
     * @param result 日期范围
     */
    onChartSelectTimeRange(result: Date[]): void {
        this.logTimeChartFilterForm = {
            ...this.logTimeChartFilterForm,
            projectIdentifier: this.projectIdentifier,
            startTime: result[0] ? `${moment(result[0]).format('YYYY-MM-DD')} 00:00:00` : '',
            endTime: result[1] ? `${moment(result[1]).format('YYYY-MM-DD')} 00:00:00` : '',
        };
        this.getLogTimeChartData(this.logTimeChartFilterForm);
    }

    /**
     * 图表-选择时间粒度
     */
    onChartTimeIntervalChange(): void {
        this.getLogTimeChartData({ ...this.logTimeChartFilterForm, projectIdentifier: this.projectIdentifier });
    }

    /**
     * 表格数据-初始化时间范围
     */
    initTableTimeRange() {
        this.timeRange = '近1小时';
        this.onTableTimeRangeChange();
    }

    /**
     * 表格数据-设置时间范围
     * @param startTimeStr 开始时间
     * @param endTimeStr 结束时间
     */
    setTimeRangePicker(startTimeStr: string, endTimeStr: string) {
        let startTime = new Date(startTimeStr);
        let endTime = new Date(endTimeStr);
        this.timeRangePicker = [startTime, endTime];
    }

    /**
     * 表格数据-选择日期查询范围
     * @param result 日期范围
     */
    onTableSelectTimeRange(result: Date[]): void {
        this.tableFilterForm = {
            ...this.tableFilterForm,
            projectIdentifier: this.projectIdentifier,
            startTime: result[0] ? moment(result[0]).format('YYYY-MM-DD HH:mm:ss') : '',
            endTime: result[1] ? moment(result[1]).format('YYYY-MM-DD HH:mm:ss') : '',
        };
        this.timeRange = '';
        this.getTableList();
        this.getDistributionChartData();
    }

    /**
     * 表格数据-选择日期查询范围
     */
    onTableTimeRangeChange() {
        let startTime: string;
        let endTime: string;
        if (this.timeRange === '近1小时') {
            startTime = moment(new Date()).add(-1, 'hours').format('YYYY-MM-DD HH:mm:ss');
            endTime = moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
        }
        else if (this.timeRange === '今日') {
            startTime = moment(new Date()).format('YYYY-MM-DD') + ' 00:00:00';
            endTime = moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
        }
        else if (this.timeRange === '近7日') {
            startTime = moment(new Date()).add(-7, 'day').format('YYYY-MM-DD HH:mm:ss');
            endTime = moment(new Date()).format('YYYY-MM-DD HH:mm:ss');
        }
        this.tableFilterForm = {
            ...this.tableFilterForm,
            projectIdentifier: this.projectIdentifier,
            startTime,
            endTime
        };
        this.setTimeRangePicker(startTime, endTime);
        this.getTableList();
        this.getDistributionChartData();
    }

    /**
     * js异常日志-聚合查询
     */
    getTableList(): void {
        let { projectIdentifier, startTime, endTime } = this.tableFilterForm;
        if (!projectIdentifier) {
            // this.message.warning('请选择项目');
            return;
        }
        if (!startTime || !endTime) {
            this.message.warning('请选择起止时间');
            return;
        }
        this.isLoading = true;
        this.logService.getJsErrorLogByGroup(
            this.tableFilterForm,
            res => {
                console.log('[成功]获取js异常日志', res);
                this.isLoading = false;
                let { success, data, msg } = res;
                if (!success) {
                    this.message.error(msg || '获取js异常日志失败');
                } else {
                    let { records, totalNum } = data;
                    this.logRecordList = records.map(item => ({
                        ...item,
                        latestRecordTime: item.latestRecordTime ? moment(new Date(item.latestRecordTime)).format('YYYY-MM-DD HH:mm:ss') : '',
                    }));
                    this.paginationConfig.total = totalNum;
                }
            },
            err => {
                console.log('[失败]获取js异常日志', err);
                this.isLoading = false;
            }
        );
    }

    /**
     * 查看日志
     * @param errorMessage 异常内容
     */
    handleShowDetail(errorMessage: string): void {
        // console.log('errorMessage', errorMessage);
        this.errorMsg = errorMessage;
        this.isShowDetail = true;
    }
}
