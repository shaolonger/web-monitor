import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SharedModule } from '@shared/shared.module';

import { OverviewComponent } from './page/overview/overview.component';
import { JsErrorLogComponent } from './page/js-error-log/js-error-log.component';
import { HttpErrorLogComponent } from './page/http-error-log/http-error-log.component';
import { ResourceLoadErrorLogComponent } from './page/resource-load-error-log/resource-load-error-log.component';
import { CustomErrorLogComponent } from './page/custom-error-log/custom-error-log.component';

import { ProjectRoutingModule } from './project.routing';

@NgModule({
    declarations: [OverviewComponent, JsErrorLogComponent, HttpErrorLogComponent, ResourceLoadErrorLogComponent, CustomErrorLogComponent],
    imports: [
        CommonModule,
        SharedModule,
        ProjectRoutingModule
    ]
})
export class ProjectModule { }
