import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

    constructor(
        private router: Router
    ) { }

    private handleError(her: HttpErrorResponse) {
        if (her.error instanceof ErrorEvent) {
            console.error('An error occurred:', her.error.message);
        } else {
            if (her.error?.msg === 'token已失效') {
                this.router.navigate(['/auth/login']);
            }
        }
        console.error(
            `Backend returned code ${her.status}, ` +
            `body was: ${her.error}`
        );
        return throwError('Something bad happened; please try again later');
    }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request)
            .pipe(
                catchError(this.handleError)
            );
    }
}