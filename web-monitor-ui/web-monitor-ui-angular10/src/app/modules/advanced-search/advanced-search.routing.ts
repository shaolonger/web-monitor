import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { AdvancedSearchContainerComponent } from './page/advanced-search-container/advanced-search-container.component';

const routes: Routes = [
    {
        path: '',
        component: AdvancedSearchContainerComponent
    }
];

@NgModule({
    imports: [RouterModule.forChild(routes)],
    exports: [RouterModule]
})
export class AdvancedSearchRoutingModule { }
