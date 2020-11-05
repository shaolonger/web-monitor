import { Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

import { HttpService } from '@core/service/http.service';

import { BasicFilters } from '@data/interfaces/log.interface';

@Injectable({
    providedIn: 'root'
})
export class LogService {

    constructor(
        private httpService: HttpService
    ) { }

    /**
     * 获取总览页信息
     * @param params 
     * @param successCallback 
     * @param failCallback 
     */
    public getOverallByTimeRange(params: Object, successCallback?: Function, failCallback?: Function): void {
        this.httpService.get('/statistic/getOverallByTimeRange', params).subscribe(
            (res: any) => {
                successCallback && successCallback(res);
            },
            (err: HttpErrorResponse) => {
                failCallback && failCallback(err);
            }
        );
    }

    /**
     * 获取日志统计数据
     * @param logTypeList 
     * @param params 
     * @param successCallback 
     * @param failCallback 
     */
    public getLogCountByHours(logTypeList: string[], params: BasicFilters, successCallback?: Function, failCallback?: Function): void {
        let batchRequestParams = logTypeList.map(logType => {
            return {
                method: 'get',
                params: {
                    url: '/statistic/getLogCountByHours',
                    params: {
                        logType,
                        projectIdentifier: params.projectIdentifier,
                        startTime: params.startTime,
                        endTime: params.endTime
                    }
                }
            };
        });
        this.httpService.batchRequest(batchRequestParams).subscribe(
            (res: any) => {
                successCallback && successCallback(res);
            },
            (err: HttpErrorResponse) => {
                failCallback && failCallback(err);
            }
        );
    }

    /**
     * js异常日志-聚合查询
     * @param params 
     * @param successCallback 
     * @param failCallback 
     */
    public getJsErrorLogByGroup(params: Object, successCallback?: Function, failCallback?: Function): void {
        this.httpService.get('/jsErrorLog/getByGroup', params).subscribe(
            (res: any) => {
                successCallback && successCallback(res);
            },
            (err: HttpErrorResponse) => {
                failCallback && failCallback(err);
            }
        );
    }

    /**
     * http异常日志-聚合查询
     * @param params 
     * @param successCallback 
     * @param failCallback 
     */
    public getHttpErrorLogByGroup(params: Object, successCallback?: Function, failCallback?: Function): void {
        this.httpService.get('/httpErrorLog/getByGroup', params).subscribe(
            (res: any) => {
                successCallback && successCallback(res);
            },
            (err: HttpErrorResponse) => {
                failCallback && failCallback(err);
            }
        );
    }

    /**
     * resource加载异常日志-聚合查询
     * @param params 
     * @param successCallback 
     * @param failCallback 
     */
    public getResourceLoadErrorLogByGroup(params: Object, successCallback?: Function, failCallback?: Function): void {
        this.httpService.get('/resourceLoadErrorLog/getByGroup', params).subscribe(
            (res: any) => {
                successCallback && successCallback(res);
            },
            (err: HttpErrorResponse) => {
                failCallback && failCallback(err);
            }
        );
    }

    /**
     * custom异常日志-聚合查询
     * @param params 
     * @param successCallback 
     * @param failCallback 
     */
    public getCustomErrorLogByGroup(params: Object, successCallback?: Function, failCallback?: Function): void {
        this.httpService.get('/customErrorLog/getByGroup', params).subscribe(
            (res: any) => {
                successCallback && successCallback(res);
            },
            (err: HttpErrorResponse) => {
                failCallback && failCallback(err);
            }
        );
    }

    /**
     * 统计-获取两个日期之间的对比数据
     * @param params 
     * @param successCallback 
     * @param failCallback 
     */
    public getLogCountBetweenDiffDate(params: Object, successCallback?: Function, failCallback?: Function): void {
        this.httpService.get('/statistic/getLogCountBetweenDiffDate', params).subscribe(
            (res: any) => {
                successCallback && successCallback(res);
            },
            (err: HttpErrorResponse) => {
                failCallback && failCallback(err);
            }
        );
    }

    /**
     * 统计-获取两个日期之间的设备、操作系统、浏览器、网络类型的统计数据
     * @param params 
     * @param successCallback 
     * @param failCallback 
     */
    public getLogDistributionBetweenDiffDate(params: Object, successCallback?: Function, failCallback?: Function): void {
        this.httpService.get('/statistic/getLogDistributionBetweenDiffDate', params).subscribe(
            (res: any) => {
                successCallback && successCallback(res);
            },
            (err: HttpErrorResponse) => {
                failCallback && failCallback(err);
            }
        );
    }

    /**
     * 日志记录-高级查询-多条件
     * @param params 
     * @param successCallback 
     * @param failCallback 
     */
    public getLogListByConditions(params: Object, successCallback?: Function, failCallback?: Function): void {
        this.httpService.post('/log/list', params).subscribe(
            (res: any) => {
                successCallback && successCallback(res);
            },
            (err: HttpErrorResponse) => {
                failCallback && failCallback(err);
            }
        );
    }
}
