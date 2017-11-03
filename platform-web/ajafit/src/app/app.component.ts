import {Component} from '@angular/core';
import {OnInit} from '@angular/core';
import {Router} from '@angular/router';

import {Profile} from './profile/profile';
import {ProfileComponent} from './profile/profile.component';
import {ProfileDetailComponent} from './profile/profile-detail.component';
import {ProfileService} from './profile/profile.service';
import {AuthComponent} from './auth/auth.component';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [ProfileService]
})
export class AppComponent {
  constructor(private profileService: ProfileService, private router: Router) {}
  title = 'app center';
  profile: Profile;
  getProfile(): Profile {
    this.profile = this.profileService.getSelectedProfile();
  //  if (this.router.url === '/dashboard' || this.router.url === '/') {
   // } else if (this.profile == null && this.router.url !== '/auth') {
   //   this.router.navigate(['/auth']);
   // }
  return this.profile;
  }
  logout(): void {
    this.profileService.logout();
  }
}
