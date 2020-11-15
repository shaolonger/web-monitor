import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AlarmManageComponent } from './page/alarm-manage-container/alarm-manage.component';

const routes: Routes = [
    {
        path: '',
        component: AlarmManageComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AlarmManageRoutingModule { }
