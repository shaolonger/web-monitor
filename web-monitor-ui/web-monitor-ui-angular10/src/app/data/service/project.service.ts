import { Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

import { HttpService } from '@core/service/http.service';

@Injectable({
    providedIn: 'root'
})
export class ProjectService {

    constructor(
        private httpService: HttpService
    ) { }

    /**
     * 新增项目
     * @param params 
     * @param successCallback 
     * @param failCallback 
     */
    public addProject(params: Object, successCallback?: Function, failCallback?: Function): void {
        this.httpService.put('/project/add', params).subscribe(
            (res: any) => {
                successCallback && successCallback(res);
            },
            (err: HttpErrorResponse) => {
                failCallback && failCallback(err);
            }
        );
    }

    /**
     * 获取项目列表
     * @param params 
     * @param successCallback 
     * @param failCallback 
     */
    public getProjects(params: Object, successCallback?: Function, failCallback?: Function): void {
        this.httpService.get('/project/get', params).subscribe(
            (res: any) => {
                successCallback && successCallback(res);
            },
            (err: HttpErrorResponse) => {
                failCallback && failCallback(err);
            }
        );
    }

    /**
     * 更新项目内容
     * @param params 
     * @param successCallback 
     * @param failCallback 
     */
    public updateProject(params: Object, successCallback?: Function, failCallback?: Function): void {
        this.httpService.post('/project/update', params).subscribe(
            (res: any) => {
                successCallback && successCallback(res);
            },
            (err: HttpErrorResponse) => {
                failCallback && failCallback(err);
            }
        );
    }

    /**
     * 删除项目记录
     * @param id 
     * @param successCallback 
     * @param failCallback 
     */
    public deleteProject(id: number, successCallback?: Function, failCallback?: Function): void {
        this.httpService.delete('/project/delete/' + id).subscribe(
            (res: any) => {
                successCallback && successCallback(res);
            },
            (err: HttpErrorResponse) => {
                failCallback && failCallback(err);
            }
        );
    }
}
