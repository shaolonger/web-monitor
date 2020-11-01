import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { UserRegisterAuditComponent } from './page/user-register-audit/user-register-audit.component';
import { ProjectManageComponent } from './page/project-manage/project-manage.component';

const routes: Routes = [
    {
        path: 'userRegisterAudit',
        component: UserRegisterAuditComponent
    },
    {
        path: 'projectManage',
        component: ProjectManageComponent
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class SystemManageRoutingModule { }
