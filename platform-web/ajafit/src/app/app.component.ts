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

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [ProfileService]
})
 @Injectable()
export class AppComponent  implements OnInit {
  constructor(private profileService: ProfileService, private router: Router, private itemService: ItemService) {}
  title = 'app center';
  profile: Profile;
  cartNumber: number;
  //cartNumber = 1;
  getProfile(): Profile {
    this.profile = this.profileService.getSelectedProfile();
  //  if (this.router.url === '/dashboard' || this.router.url === '/') {
   // } else if (this.profile == null && this.router.url !== '/auth') {
   //   this.router.navigate(['/auth']);
   // }
  return this.profile;
  }
  ngOnInit(): void {
   this.itemService.getCartNumber().then(resp => this.setCartNumber(resp));
  }
  logout(): void {
    this.profileService.logout();
  }
  setCartNumber(n: number): void {
    this.cartNumber = n;
    console.log('atualizando: '+this.cartNumber);
  }

}
