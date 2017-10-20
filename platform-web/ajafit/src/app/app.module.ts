import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';
import {HttpModule} from '@angular/http';
import {HTTP_INTERCEPTORS} from '@angular/common/http';

import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { TooltipModule } from 'ngx-bootstrap/tooltip';
import { ModalModule } from 'ngx-bootstrap/modal';

import {AppComponent} from './app.component';
import {ProfileComponent} from './profile/profile.component';
import {ProfileDetailComponent} from './profile/profile-detail.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import {AuthComponent} from './auth/auth.component';
import {ProfileService} from './profile/profile.service';
import {AppRoutingModule} from './app-routing.module';
import {AjafitInterceptor} from './ajafitinterceptor';

import {ItemService} from './item/item.service';
import {ItemComponent} from './item/item.component';
import {ItemDetailComponent} from './item/item-detail.component';

@NgModule({
  declarations: [
    AppComponent,
    ProfileComponent,
    ProfileDetailComponent,
    AuthComponent,
    DashboardComponent,
    ItemComponent,
    ItemDetailComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    AppRoutingModule,
     BsDropdownModule.forRoot(),
    TooltipModule.forRoot(),
    ModalModule.forRoot()
  ],
  providers: [{provide: HTTP_INTERCEPTORS, useClass: AjafitInterceptor, multi: true}, ProfileService, ItemService],
 /* providers: [ProfileService],*/
  bootstrap: [AppComponent]
})
export class AppModule {}


