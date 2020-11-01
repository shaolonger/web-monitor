import { Component, OnInit } from '@angular/core';
import * as moment from 'moment';

import { NzMessageService } from 'ng-zorro-antd/message';
import { UserService } from '@data/service/user.service';
import { AUDIT_RESULT_MAP, GENDER_MAP } from '@app/constants/system-manage.const';

@Component({
    selector: 'app-user-register-audit',
    templateUrl: './user-register-audit.component.html',
    styleUrls: ['./user-register-audit.component.scss']
})
export class UserRegisterAuditComponent implements OnInit {

    isLoading = false;
    // 筛选条件
    filterForm = {
        pageNum: 1,
        pageSize: 3,
        startTime: '',
        endTime: '',
        auditResult: ''
    };
    // 数据列表
    listData: Array<Object>;
    // 分页控制器
    paginationConfig = {
        total: 0
    };

    constructor(
        private userService: UserService,
        private message: NzMessageService,
    ) {
    }

    ngOnInit(): void {
        this.getTableList();
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
        this.getTableList();
    }

    /**
     * 查询用户注册记录
     */
    getTableList(): void {
        this.isLoading = true;
        this.userService.getUserRegisterRecord(
            this.filterForm,
            res => {
                console.log('[成功]查询用户注册记录', res);
                this.isLoading = false;
                const { success, data, msg } = res;
                if (!success) {
                    this.message.error(msg || '查询用户注册记录失败');
                } else {
                    let { records, totalNum } = data;
                    this.listData = records.map(item => ({
                        id: item.id,
                        username: item.username,
                        email: item.email,
                        phone: item.phone,
                        gender: GENDER_MAP[item.gender],
                        auditResult: item.auditResult,
                        auditResultText: AUDIT_RESULT_MAP[item.auditResult],
                        auditTime: item.updateTime ? moment(new Date(item.updateTime)).format('YYYY-MM-DD HH:mm:ss') : ''
                    }));
                    this.paginationConfig.total = totalNum;
                }
            },
            err => {
                console.log('[失败]查询用户注册记录', err);
                this.isLoading = false;
            }
        );
    }

    /**
     * 用户注册记录审批
     * @param auditId 
     * @param auditResult 
     */
    audit(auditId: number, auditResult: number): void {
        this.isLoading = true;
        this.userService.audit(
            { auditId, auditResult },
            res => {
                console.log('[成功]审核注册记录', res);
                this.isLoading = false;
                const { success, msg } = res;
                if (success) {
                    this.message.success('审核成功').onClose.subscribe(() => {
                        this.getTableList();
                    });
                } else {
                    this.message.error(msg || '审核失败');
                }
            }
        );
    }

}
