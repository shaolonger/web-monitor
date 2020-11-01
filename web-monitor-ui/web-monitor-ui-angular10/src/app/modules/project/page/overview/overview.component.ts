import { Component, OnInit } from '@angular/core';
import * as moment from 'moment';
import { EChartOption } from 'echarts';

import { NzMessageService } from 'ng-zorro-antd/message';
import { UserService } from '@data/service/user.service';
import { LogService } from '@data/service/log.service';

import { UserRelatedProject } from '@data/classes/project.class';

@Component({
    selector: 'app-overview',
    templateUrl: './overview.component.html',
    styleUrls: ['./overview.component.scss']
})
export class OverviewComponent implements OnInit {

    // 加载状态
    isLoading = false;
    // 筛选条件
    filterForm = {
        projectIdentifier: '',
        startTime: '',
        endTime: ''
    };
    // 项目列表
    projectList: UserRelatedProject[] = [];
    // 异常数据总览
    overviewStatistic = {
        jsErrorLogCount: 0,
        httpErrorLogCount: 0,
        resourceLoadErrorLogCount: 0,
        customErrorLogCount: 0
    };
    // echarts
    logTypeList = [
        {name: 'JS异常', value: 'jsErrorLog'},
        {name: 'HTTP请求异常', value: 'httpErrorLog'},
        {name: '资源加载异常', value: 'resourceLoadErrorLog'},
        {name: '自定义异常', value: 'customErrorLog'}
    ];
    chartOption: EChartOption = {
        title: {
            text: '异常统计总览'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                label: {
                    backgroundColor: '#6a7985'
                }
            }
        },
        legend: {
            data: this.logTypeList.map(item => item.name)
        },
        xAxis: [
            {
                type: 'category',
                boundaryGap: false,
                data: []
            }
        ],
        yAxis: [
            {
                type: 'value',
                minInterval: 1
            }
        ],
        color: ['#64B5F6', '#FF7043', '#FFE082', '#58d45c'],
        series: [
            {
                name: 'jsErrorLog',
                type: 'line',
                stack: '总量',
                areaStyle: {},
                data: []
            },
            {
                name: 'httpErrorLog',
                type: 'line',
                stack: '总量',
                areaStyle: {},
                data: []
            },
            {
                name: 'resourceLoadErrorLog',
                type: 'line',
                stack: '总量',
                areaStyle: {},
                data: []
            },
            {
                name: 'customErrorLog',
                type: 'line',
                stack: '总量',
                areaStyle: {},
                data: []
            },
        ]
    };

    constructor(
        private userService: UserService,
        private logService: LogService,
        private message: NzMessageService
    ) { }

    ngOnInit(): void {
        this.getUserRelatedProjectList();
    }

    /**
     * 根据用户获取关联的项目
     */
    getUserRelatedProjectList(): void {
        this.projectList = this.userService.getUserRelatedProjectList();
    }

    /**
     * 选择日期查询范围
     * @param result 日期范围
     */
    onSelectTimeRange(result: Date[]): void {
        this.filterForm = {
            ...this.filterForm,
            startTime: result[0] ? moment(result[0]).format('YYYY-MM-DD HH:mm:ss') : '',
            endTime: result[1] ? moment(result[1]).format('YYYY-MM-DD HH:mm:ss') : '',
        };
        this.getPageData();
    }

    /**
     * 获取页面数据
     */
    getPageData(): void {
        this.getOverallByTimeRange();
        this.getLogCountByHours();
    }

    /**
     * 获取总览页信息
     */
    getOverallByTimeRange(): void {
        let { projectIdentifier, startTime, endTime } = this.filterForm;
        if (!projectIdentifier) {
            this.message.warning('请选择项目');
            return;
        }
        if (!startTime || !endTime) {
            this.message.warning('请选择起止时间');
            return;
        }
        this.isLoading = true;
        this.logService.getOverallByTimeRange(
            this.filterForm,
            res => {
                console.log('[成功]获取总览页信息', res);
                this.isLoading = false;
                const { success, data, msg } = res;
                if (!success) {
                    this.message.error(msg || '获取总览页信息失败');
                } else {
                    this.overviewStatistic = data;
                }
            },
            err => {
                console.log('[失败]获取总览页信息', err);
                this.isLoading = false;
            }
        );
    }

    /**
     * 获取日志统计数据
     */
    getLogCountByHours(): void {
        let { projectIdentifier, startTime, endTime } = this.filterForm;
        if (!projectIdentifier || !startTime || !endTime) {
            return;
        }
        this.isLoading = true;
        this.logService.getLogCountByHours(
            this.logTypeList.map(item => item.value),
            this.filterForm,
            res => {
                console.log('[成功]获取日志统计数据', res);
                this.isLoading = false;
                let chartData = [];
                this.logTypeList.map(item => item.name).forEach((name, index) => {
                    let response = res[index];
                    if (!response.success) {
                        this.message.warning(`${name}统计数据获取失败`);
                        chartData.push({
                            name: name,
                            data: []
                        });
                        return;
                    }
                    chartData.push({
                        name: name,
                        data: response.data.now
                    });
                });
                let option = {
                    ...this.chartOption,
                    legend: {
                        data: this.logTypeList.map(item => item.name)
                    },
                    xAxis: [
                        {
                            type: 'category',
                            boundaryGap: false,
                            data: Object.keys(chartData[0].data)
                        }
                    ],
                    series: chartData.map(item => ({
                        name: item.name,
                        type: 'line',
                        stack: '总量',
                        areaStyle: {},
                        data: Object.values(item.data)
                    }))
                };
                this.chartOption = option;
            },
            err => {
                console.log('[失败]获取日志统计数据', err);
                this.isLoading = false;
            }
        );
    }
}
