import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {RouterModule, Routes} from '@angular/router';

import {AppComponent} from './app.component';
import {ProfileComponent} from './profile/profile.component';
import {ProfileDetailComponent} from './profile/profile-detail.component';
import {DashboardComponent} from './dashboard/dashboard.component';
import {AuthComponent} from './auth/auth.component';
import {AboutComponent} from './about/about.component';

import {ItemComponent} from './item/item.component';
import {ItemDetailComponent} from './item/item-detail.component';
import {CartComponent} from './cart/cart.component';

const routes: Routes = [{
  path: 'auth',
  component: AuthComponent
}, {
  path: 'about',
  component: AboutComponent
}, {
  path: 'cart',
  component: CartComponent
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
  path: 'item-detail/:screen/:coupon',
  component: ItemDetailComponent
}, {
  path: '',
  component: DashboardComponent
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


