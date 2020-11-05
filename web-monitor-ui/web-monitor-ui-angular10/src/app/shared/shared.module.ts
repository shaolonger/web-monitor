import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

// ng-zorro
import { NgZorroAntdModule } from './ng-zorro-antd.module';

// ngx-highlightjs
import { NpxHighlightModule } from './npx-highlight.module';

// ngx-echarts
import { NgxEchartsModule } from 'ngx-echarts';

// components
import { SpinnerComponent } from './components/spinner/spinner.component';
import { LogDetailComponent } from './components/log-detail/log-detail.component';

@NgModule({
    declarations: [
        SpinnerComponent,
        LogDetailComponent
    ],
    imports: [
        CommonModule,

        // ng-zorro-antd
        NgZorroAntdModule,

        // ngx-echarts
        NgxEchartsModule.forRoot({
            echarts: () => import('echarts')
        }),
    ],
    exports: [
        // angular
        FormsModule,
        ReactiveFormsModule,

        // ng-zorro-antd
        NgZorroAntdModule,

        // ngx-highlightjs
        NpxHighlightModule,

        // ngx-echarts
        NgxEchartsModule,

        // components
        SpinnerComponent,
        LogDetailComponent
    ],
    providers: [
        // ngx-highlightjs
        NpxHighlightModule
    ]
})
export class SharedModule { }
