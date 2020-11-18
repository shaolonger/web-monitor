import { Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { HttpService } from '@core/service/http.service';

@Injectable({
    providedIn: 'root'
})
export class AlarmService {

    constructor(
        private httpService: HttpService
    ) { }

    /**
     * 新增预警
     * @param params
     * @param successCallback
     * @param failCallback
     */
    public addAlarm(params: Object, successCallback?: Function, failCallback?: Function): void {
        this.httpService.put('/alarm/add', params).subscribe(
            (res: any) => {
                successCallback && successCallback(res);
            },
            (err: HttpErrorResponse) => {
                failCallback && failCallback(err);
            }
        );
    }

    /**
     * 获取预警列表
     * @param params
     * @param successCallback
     * @param failCallback
     */
    public getAlarms(params: Object, successCallback?: Function, failCallback?: Function): void {
        this.httpService.get('/alarm/get', params).subscribe(
            (res: any) => {
                successCallback && successCallback(res);
            },
            (err: HttpErrorResponse) => {
                failCallback && failCallback(err);
            }
        );
    }

    /**
     * 更新预警内容
     * @param params
     * @param successCallback
     * @param failCallback
     */
    public updateAlarm(params: Object, successCallback?: Function, failCallback?: Function): void {
        this.httpService.post('/alarm/update', params).subscribe(
            (res: any) => {
                successCallback && successCallback(res);
            },
            (err: HttpErrorResponse) => {
                failCallback && failCallback(err);
            }
        );
    }

    /**
     * 删除预警记录
     * @param id
     * @param successCallback
     * @param failCallback
     */
    public deleteAlarm(id: number, successCallback?: Function, failCallback?: Function): void {
        this.httpService.delete('/alarm/delete/' + id).subscribe(
            (res: any) => {
                successCallback && successCallback(res);
            },
            (err: HttpErrorResponse) => {
                failCallback && failCallback(err);
            }
        );
    }

    /**
     * 获取预警记录列表
     * @param params
     * @param successCallback
     * @param failCallback
     */
    public getAlarmRecord(params: Object, successCallback?: Function, failCallback?: Function): void {
        this.httpService.get('/alarmRecrod/get', params).subscribe(
            (res: any) => {
                successCallback && successCallback(res);
            },
            (err: HttpErrorResponse) => {
                failCallback && failCallback(err);
            }
        );
    }
}
