import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { CoreModule } from '@core/core.module';
import { SharedModule } from '@shared/shared.module';
import { EventService } from '@core/service/event.service';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

// modules & component
import { ContentLayoutComponent } from './layout/content-layout/content-layout.component';
import { AuthLayoutComponent } from './layout/auth-layout/auth-layout.component';
import { NotFoundLayoutComponent } from './layout/not-found-layout/not-found-layout.component';

@NgModule({
    declarations: [
        AppComponent,
        ContentLayoutComponent,
        AuthLayoutComponent,
        NotFoundLayoutComponent
    ],
    imports: [
        // angular
        BrowserModule,
        BrowserAnimationsModule,

        // core & shared
        CoreModule,
        SharedModule,

        // app
        AppRoutingModule,
    ],
    providers: [
        EventService
    ],
    bootstrap: [AppComponent]
})
export class AppModule { }
