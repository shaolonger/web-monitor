import { Component, OnInit } from '@angular/core';
import * as moment from 'moment';

import { NzMessageService } from 'ng-zorro-antd/message';
import { UserService } from '@data/service/user.service';
import { AlarmService } from '@data/service/alarm.service';

import { Project } from '@data/classes/project.class';

@Component({
    selector: 'app-alarm-record',
    templateUrl: './alarm-record.component.html',
    styleUrls: ['./alarm-record.component.scss']
})
export class AlarmRecordComponent implements OnInit {

    isLoading = false;
    projectSelected: Project = null;
    // 筛选条件
    filterForm = {
        pageNum: 1,
        pageSize: 10,
        alarmId: null,
        alarmData: '',
        startTime: '',
        endTime: '',
    };
    // 数据列表
    tableData: Array<Object>;
    // 分页控制器
    paginationConfig = {
        total: 0
    };
    // 表格时间筛选日历结果
    timeRangePicker: Date[];
    timeRange = '';
    // 表格时间筛选列表
    timeIntervalList = [
        { label: '近1小时', value: '近1小时' },
        { label: '今日', value: '今日' },
        { label: '近7日', value: '近7日' },
    ];

    constructor(
        private userService: UserService,
        private alarmService: AlarmService,
        private message: NzMessageService,
    ) { }

    ngOnInit(): void {
        this.setProjectSelected();
        this.getAlarmRecord();
    }

    /**
     * 设置用户选择的项目
     */
    setProjectSelected(): void {
        let projectSelected = this.userService.getProjectSelected();
        this.projectSelected = projectSelected;

        // TODO 通过projectIdentifier获取所有的alarmId，放入filterForm内作为查询条件
    }

    /**
     * 获取预警记录列表
     */
    getAlarmRecord(): void {
        this.isLoading = true;
        this.alarmService.getAlarmRecord(
            this.filterForm,
            res => {
                console.log('[成功]获取预警记录列表', res);
                this.isLoading = false;
                const { success, data, msg } = res;
                if (!success) {
                    this.message.error(msg || '获取预警记录列表失败');
                } else {
                    let { records, totalNum } = data;
                    this.tableData = records.map(item => ({
                        ...item,
                        createTimeText: moment(item.createTime).format('YYYY-MM-DD HH:mm:ss')
                    }));
                    this.paginationConfig.total = totalNum;
                }
            },
            err => {
                console.log('[失败]获取预警记录列表', err);
                this.isLoading = false;
            }
        );
    }

    /**
     * 表格数据-选择日期查询范围
     * @param result 日期范围
     */
    onTableSelectTimeRange(result: Date[]): void {
        this.filterForm = {
            ...this.filterForm,
            startTime: result[0] ? moment(result[0]).format('YYYY-MM-DD HH:mm:ss') : '',
            endTime: result[1] ? moment(result[1]).format('YYYY-MM-DD HH:mm:ss') : '',
        };
        this.timeRange = '';
        this.getAlarmRecord();
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
        this.filterForm = {
            ...this.filterForm,
            startTime,
            endTime
        };
        this.setTimeRangePicker(startTime, endTime);
        this.getAlarmRecord();
    }
}
