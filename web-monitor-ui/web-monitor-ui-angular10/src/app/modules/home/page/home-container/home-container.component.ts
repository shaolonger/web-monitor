import { Component, OnInit } from '@angular/core';
import * as moment from 'moment';
import { EChartOption } from 'echarts';

import { UserService } from '@data/service/user.service';
import { EventService } from '@core/service/event.service';
import { NzMessageService } from 'ng-zorro-antd/message';
import { LogService } from '@data/service/log.service';

import { EventModel } from '@data/classes/event.class';

@Component({
    selector: 'app-home-container',
    templateUrl: './home-container.component.html',
    styleUrls: ['./home-container.component.scss']
})
export class HomeContainerComponent implements OnInit {

    // 加载状态
    isLoading = false;
    // 时间筛选
    timeIntervalList = [
        { label: '近一小时', value: '近一小时' },
        { label: '今日', value: '今日' }
    ];
    timeRange = '近一小时';
    // 筛选条件
    filterForm = {
        projectIdentifier: '',
        logTypeList: 'jsErrorLog,httpErrorLog,resourceLoadErrorLog,customErrorLog',
        indicatorList: 'count',
        startTime: '',
        endTime: '',
        timeInterval: 86400
    };
    // 图表-总计数据
    chartTotalJS = {
        today: 0,
        yesterday: 0,
        rate: '-',
        change: 0
    };
    chartTotalHttp = {
        today: 0,
        yesterday: 0,
        rate: '-',
        change: 0
    };
    chartTotalRes = {
        today: 0,
        yesterday: 0,
        rate: '-',
        change: 0
    };
    chartTotalCus = {
        today: 0,
        yesterday: 0,
        rate: '-',
        change: 0
    };
    // 图表-JS异常
    chartOptionJS: EChartOption = {
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
            top: 10,
            left: '8%',
            right: '8%',
            bottom: '15%'
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
    // 图表-HTTP异常
    chartOptionHttp: EChartOption = {
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
            top: 10,
            left: '8%',
            right: '8%',
            bottom: '15%'
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
                name: 'HTTP异常',
                type: 'line',
                stack: '总量',
                areaStyle: {},
                data: []
            },
        ]
    };
    // 图表-资源加载异常
    chartOptionRes: EChartOption = {
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
            top: 10,
            left: '8%',
            right: '8%',
            bottom: '15%'
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
                name: '资源加载异常',
                type: 'line',
                stack: '总量',
                areaStyle: {},
                data: []
            },
        ]
    };
    // 图表-自定义异常
    chartOptionCus: EChartOption = {
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
            top: 10,
            left: '8%',
            right: '8%',
            bottom: '15%'
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
                name: '自定义异常',
                type: 'line',
                stack: '总量',
                areaStyle: {},
                data: []
            },
        ]
    };

    constructor(
        private userService: UserService,
        private eventService: EventService,
        private logService: LogService,
        private message: NzMessageService
    ) { }

    ngOnInit(): void {
        this.attachEventListener();
        this.setInitData();
        // 初始化时，手动触发一次数据刷新
        this.getPageData();
    }

    /**
     * 绑定事件监听器
     */
    attachEventListener(): void {
        this.eventService.eventEmitter.subscribe((event: EventModel) => {
            let { eventName, eventPayload } = event;
            if (eventName === 'projectSelectedChanged') {
                this.filterForm.projectIdentifier = eventPayload.projectIdentifier;
                this.getPageData();
            }
        });
    }

    // 初始化数据
    setInitData(): void {
        this.setProjectSelected();
    }

    /**
     * 设置用户选择的项目
     */
    setProjectSelected(): void {
        let projectSelected = this.userService.getProjectSelected();
        this.filterForm.projectIdentifier = projectSelected.projectIdentifier;
    }

    /**
     * 获取页面数据
     */
    getPageData(): void {
        let startTime: string;
        let endTime: string;
        let timeInterval: number;
        if (this.timeRange === '近一小时') {
            startTime = moment(new Date()).add(-1, 'hours').format('YYYY-MM-DD HH:mm') + ':00';
            endTime = moment(new Date()).format('YYYY-MM-DD HH:mm') + ':00';
            timeInterval = 60;
        }
        else if (this.timeRange === '今日') {
            startTime = moment(new Date()).format('YYYY-MM-DD') + ' 00:00:00';
            endTime = moment(new Date()).add(1, 'days').format('YYYY-MM-DD') + ' 00:00:00';
            timeInterval = 3600;
        }
        if (!this.filterForm.projectIdentifier || !startTime || !endTime) return;
        this.filterForm = { ...this.filterForm, startTime, endTime, timeInterval };
        this.getDayData(this.filterForm);
        this.getDetailData(this.filterForm);
    }

    /**
     * 获取今天、昨天对比数据
     * @param formData 
     */
    getDayData(formData: Object): void {
        let startTime = moment(new Date()).add(-1, 'day').format('YYYY-MM-DD') + ' 00:00:00';
        let endTime = moment(new Date()).add(1, 'day').format('YYYY-MM-DD') + ' 00:00:00';
        this.getLogCountBetweenDiffDate(
            { ...formData, startTime, endTime, timeInterval: 86400 },
            data => {
                this.setChartTotal(data);
            }
        );
    }

    /**
     * 获取详细数据
     * @param formData 
     */
    getDetailData(formData: Object): void {
        this.getLogCountBetweenDiffDate(
            formData,
            data => {
                this.setChartsOption(data);
            }
        );
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
     * 设置图表标题总计数据
     * @param data 
     */
    setChartTotal(data: any): void {
        let { jsErrorLog, httpErrorLog, resourceLoadErrorLog, customErrorLog } = data;
        let getData = (array: any) => {
            let yesterday = array[0].count;
            let today = array[1].count;
            let change = (today !== yesterday) && (today > yesterday ? 1 : -1);
            let rate = yesterday === 0 ? '-' : `${(((today - yesterday) / yesterday) * 100).toFixed(2)}%`;
            return { yesterday, today, rate, change };
        };
        this.chartTotalJS = getData(jsErrorLog);
        this.chartTotalHttp = getData(httpErrorLog);
        this.chartTotalRes = getData(resourceLoadErrorLog);
        this.chartTotalCus = getData(customErrorLog);
    }

    /**
     * 设置图表
     * @param data 
     */
    setChartsOption(data: any): void {
        let { jsErrorLog, httpErrorLog, resourceLoadErrorLog, customErrorLog } = data;
        // 设置JS异常图表
        this.chartOptionJS.xAxis[0].data = jsErrorLog.map(item => item.key);
        this.chartOptionJS.series[0].data = jsErrorLog.map(item => item.count);
        this.chartOptionJS = { ...this.chartOptionJS };
        // 设置HTTP异常图表
        this.chartOptionHttp.xAxis[0].data = httpErrorLog.map(item => item.key);
        this.chartOptionHttp.series[0].data = httpErrorLog.map(item => item.count);
        this.chartOptionHttp = { ...this.chartOptionHttp };
        // 设置资源加载异常图表
        this.chartOptionRes.xAxis[0].data = resourceLoadErrorLog.map(item => item.key);
        this.chartOptionRes.series[0].data = resourceLoadErrorLog.map(item => item.count);
        this.chartOptionRes = { ...this.chartOptionRes };
        // 设置自定义异常图表
        this.chartOptionCus.xAxis[0].data = customErrorLog.map(item => item.key);
        this.chartOptionCus.series[0].data = customErrorLog.map(item => item.count);
        this.chartOptionCus = { ...this.chartOptionCus };
    }

}
