import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { OverviewComponent } from './page/overview/overview.component';
import { JsErrorLogComponent } from './page/js-error-log/js-error-log.component';
import { HttpErrorLogComponent } from './page/http-error-log/http-error-log.component';
import { ResourceLoadErrorLogComponent } from './page/resource-load-error-log/resource-load-error-log.component';
import { CustomErrorLogComponent } from './page/custom-error-log/custom-error-log.component';

const routes: Routes = [
    {
        path: 'overview',
        component: OverviewComponent
    },
    {
        path: 'jsErrorLog',
        component: JsErrorLogComponent
    },
    {
        path: 'httpErrorLog',
        component: HttpErrorLogComponent
    },
    {
        path: 'resourceLoadErrorLog',
        component: ResourceLoadErrorLogComponent
    },
    {
        path: 'customErrorLog',
        component: CustomErrorLogComponent
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class ProjectRoutingModule { }
