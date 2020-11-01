import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SharedModule } from '@shared/shared.module';

import { SystemManageRoutingModule } from './system-manage.routing';
import { UserRegisterAuditComponent } from './page/user-register-audit/user-register-audit.component';
import { ProjectManageComponent } from './page/project-manage/project-manage.component';

@NgModule({
  declarations: [UserRegisterAuditComponent, ProjectManageComponent],
  imports: [
    CommonModule,
    SharedModule,
    SystemManageRoutingModule
  ]
})
export class SystemManageModule { }
