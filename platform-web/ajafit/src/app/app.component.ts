import {Injectable} from '@angular/core';
import {Component} from '@angular/core';
import {OnInit} from '@angular/core';
import {Router} from '@angular/router';

import {Profile} from './profile/profile';
import {ProfileComponent} from './profile/profile.component';
import {ProfileDetailComponent} from './profile/profile-detail.component';
import {ProfileService} from './profile/profile.service';
import {AuthComponent} from './auth/auth.component';

import {ItemService} from './item/item.service';
import {CartComponent} from './cart/cart.component';
import {Region} from './cart/region';
import {RegionComponent} from './cart/region.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [ProfileService]
})
 @Injectable()
export class AppComponent  implements OnInit {
  constructor(private ps: ProfileService, private router: Router, private is: ItemService) {}
  title = 'app center';
  profile: Profile;
  cartNumber: number;
  region: Region;

  getProfile(): Profile {
    this.profile = this.ps.getSelectedProfile();
    return this.profile;
  }
  ngOnInit(): void {
   this.is.getCartNumber().then(resp => this.setCartNumber(resp));
  }
  logout(): void {
    this.ps.logout();
    this.setCartNumber(0);
  }
  setCartNumber(n: number): void {
    this.cartNumber = n;
  }

}
