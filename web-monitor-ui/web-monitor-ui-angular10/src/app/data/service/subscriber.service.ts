import { Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { HttpService } from '@core/service/http.service';

@Injectable({
    providedIn: 'root'
})
export class SubscriberService {

    constructor(
        private httpService: HttpService
    ) { }

    /**
     * 获取预警列表
     * @param params
     * @param successCallback
     * @param failCallback
     */
    public getSubscriberNotifyRecord(params: Object, successCallback?: Function, failCallback?: Function): void {
        this.httpService.get('/subscriberNotifyRecord/get', params).subscribe(
            (res: any) => {
                successCallback && successCallback(res);
            },
            (err: HttpErrorResponse) => {
                failCallback && failCallback(err);
            }
        );
    }
}
