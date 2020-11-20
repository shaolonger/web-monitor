import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SharedModule } from '@shared/shared.module';

import { AlarmManageRoutingModule } from './alarm-manage.routing';

import { AlarmConfigComponent } from './page/alarm-config/alarm-config.component';
import { AlarmRecordComponent } from './page/alarm-record/alarm-record.component';



@NgModule({
  declarations: [AlarmConfigComponent, AlarmRecordComponent],
  imports: [
    CommonModule,
    SharedModule,
    AlarmManageRoutingModule
  ]
})
export class AlarmManageModule { }
