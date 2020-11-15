import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SharedModule } from '@shared/shared.module';

import { AlarmManageComponent } from './page/alarm-manage-container/alarm-manage.component';
import { AlarmManageRoutingModule } from './alarm-manage.routing';



@NgModule({
  declarations: [AlarmManageComponent],
  imports: [
    CommonModule,
    SharedModule,
    AlarmManageRoutingModule
  ]
})
export class AlarmManageModule { }
