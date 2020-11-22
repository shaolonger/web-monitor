import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AlarmConfigComponent } from './page/alarm-config/alarm-config.component';
import { AlarmRecordComponent } from './page/alarm-record/alarm-record.component';
import { AlarmNotifyRecordComponent } from './page/alarm-notify-record/alarm-notify-record.component';

const routes: Routes = [
    {
        path: 'alarmConfig',
        component: AlarmConfigComponent
    },
    {
        path: 'alarmRecord',
        component: AlarmRecordComponent
    },
    {
        path: 'alarmNotifyRecord',
        component: AlarmNotifyRecordComponent
    },
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AlarmManageRoutingModule { }
