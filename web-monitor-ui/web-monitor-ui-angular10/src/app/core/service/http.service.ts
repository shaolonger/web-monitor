import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable, forkJoin } from 'rxjs';

import { BatchRequestParam } from '@data/interfaces/http.interface';
import { EnvService } from './env.service';

@Injectable({
    providedIn: 'root'
})
export class HttpService {

    private apiBasicUrl: string;

    constructor(
        private httpClient: HttpClient,
        private envService: EnvService
    ) {
        this.apiBasicUrl = this.envService.getApiBasicUrl();
    }

    public get(url: string, params?: Object, options?: HttpHeaders): Observable<{}> {
        let httpParams = new HttpParams();
        let fullUrl = this.apiBasicUrl + url;
        if (params) {
            for (let key in params) {
                if (params[key] !== undefined) {
                    httpParams = httpParams.set(key, params[key]);
                }
            }
        }
        return this.httpClient.get(fullUrl, { headers: options, params: httpParams });
    }

    public post(url: string, body: any = null, options?: Object): Observable<{}> {
        let httpParams = new HttpParams();
        let fullUrl = this.apiBasicUrl + url;
        if (body) {
            for (let key in body) {
                if (body[key] !== undefined) {
                    httpParams = httpParams.set(key, body[key]);
                }
            }
        }
        return this.httpClient.post(fullUrl, httpParams, options);
    }

    public put(url: string, body: any = null, options?: Object): Observable<{}> {
        let httpParams = new HttpParams();
        let fullUrl = this.apiBasicUrl + url;
        if (body) {
            for (let key in body) {
                if (body[key] !== undefined) {
                    httpParams = httpParams.set(key, body[key]);
                }
            }
        }
        return this.httpClient.put(fullUrl, httpParams, options);
    }

    public delete(url: string, options?: Object): Observable<{}> {
        let fullUrl = this.apiBasicUrl + url;
        return this.httpClient.delete(fullUrl, options);
    }

    public batchRequest(requestParams: BatchRequestParam[]): Observable<{}> {
        return forkJoin(requestParams.map(req => {
            let { url, params, options, body } = req.params;
            switch (req.method) {
                case 'get':
                    return this.get(url, params, options);
                case 'post':
                    return this.post(url, body, options);
                case 'put':
                    return this.put(url, body, options);
                case 'delete':
                    return this.delete(url, options);
            }
        }));
    }
}
