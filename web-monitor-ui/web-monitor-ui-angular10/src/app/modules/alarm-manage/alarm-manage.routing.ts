import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AlarmConfigComponent } from './page/alarm-config/alarm-config.component';

const routes: Routes = [
    {
        path: 'alarmConfig',
        component: AlarmConfigComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AlarmManageRoutingModule { }
