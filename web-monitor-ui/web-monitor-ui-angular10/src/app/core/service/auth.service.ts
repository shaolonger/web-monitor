import { Injectable } from '@angular/core';
import { HttpService } from './http.service';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class AuthService {

    private token: string;
    private AUTH_IGNORE_LIST: string[] = [
        '/user/login', '/userRegisterRecord/add'
    ];

    constructor(
        private httpService: HttpService
    ) { }

    public getAuthIgnoreList(): string[] {
        return this.AUTH_IGNORE_LIST;
    }

    public getToken(): string {
        return this.token;
    }

    public setToken(token: string): void {
        this.token = token;
    }

    public login(params: Object, successCallback?: Function, failCallback?: Function): void {
        this.httpService.post('/user/login', params).subscribe(
            (res: any) => {
                successCallback && successCallback(res);
            },
            (err: HttpErrorResponse) => {
                failCallback && failCallback(err);
            }
        );
    }

    public register(params: Object, successCallback?: Function, failCallback?: Function): void {
        this.httpService.put('/userRegisterRecord/add', params).subscribe(
            (res: any) => {
                successCallback && successCallback(res);
            },
            (err: HttpErrorResponse) => {
                failCallback && failCallback(err);
            }
        );
    }
}
