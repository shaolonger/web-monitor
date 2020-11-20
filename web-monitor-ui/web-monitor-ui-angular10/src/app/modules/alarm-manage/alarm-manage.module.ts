import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SharedModule } from '@shared/shared.module';

import { AlarmConfigComponent } from './page/alarm-config/alarm-config.component';
import { AlarmManageRoutingModule } from './alarm-manage.routing';



@NgModule({
  declarations: [AlarmConfigComponent],
  imports: [
    CommonModule,
    SharedModule,
    AlarmManageRoutingModule
  ]
})
export class AlarmManageModule { }
