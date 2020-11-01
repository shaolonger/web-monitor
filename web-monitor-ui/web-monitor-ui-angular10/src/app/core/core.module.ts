import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';

import { AuthGuard } from './guard/auth.guard';
import { httpInterceptorProviders } from './interceptors/base.interceptor';

@NgModule({
    declarations: [],
    imports: [
        CommonModule,
        HttpClientModule
    ],
    providers: [
        AuthGuard,
        httpInterceptorProviders
    ]
})
export class CoreModule { }
