import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { AuthService } from '../service/auth.service';

@Injectable()
export class TokenInterceptor implements HttpInterceptor {

    constructor(
        private authService: AuthService
    ) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        let noNeedAuth = this.authService.getAuthIgnoreList().some(url => request.url.indexOf(url) > -1);
        let token = this.authService.getToken();
        let newReq = request;
        if (!noNeedAuth && token) {
            newReq = request.clone({
                headers: request.headers.set('token', token)
            });
        }
        return next.handle(newReq);
    }
}