import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SharedModule } from '@shared/shared.module';

import { HomeContainerComponent } from './page/home-container/home-container.component';
import { HomeRoutingModule } from './home.routing';

@NgModule({
    declarations: [HomeContainerComponent],
    imports: [
        CommonModule,
        SharedModule,
        HomeRoutingModule
    ],
    entryComponents: [HomeContainerComponent]
})
export class HomeModule { }
