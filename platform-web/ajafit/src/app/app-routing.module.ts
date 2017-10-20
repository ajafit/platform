import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {RouterModule, Routes} from '@angular/router';

import {AppComponent} from './app.component';
import {ProfileComponent} from './profile/profile.component';
import {ProfileDetailComponent} from './profile/profile-detail.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import {AuthComponent} from './auth/auth.component';

import {ItemComponent} from './item/item.component';


const routes: Routes = [{
  path: 'auth',
  component: AuthComponent
}, {
  path: 'profile',
  component: ProfileComponent
}, {
  path: 'profile-detail/:id',
  component: ProfileDetailComponent
}, {
  path: 'dashboard',
  component: DashboardComponent
}, {
  path: 'item',
  component: ItemComponent
}, {
  path: '',
  redirectTo: '/dashboard',
  pathMatch: 'full'
}];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule {}


