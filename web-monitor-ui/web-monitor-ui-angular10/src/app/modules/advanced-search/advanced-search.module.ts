import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SharedModule } from '@shared/shared.module';

import { AdvancedSearchContainerComponent } from './page/advanced-search-container/advanced-search-container.component';
import { AdvancedSearchRoutingModule } from './advanced-search.routing';

@NgModule({
    declarations: [AdvancedSearchContainerComponent],
    imports: [
        CommonModule,
        SharedModule,
        AdvancedSearchRoutingModule
    ]
})
export class AdvancedSearchModule { }
