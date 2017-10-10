import {Component, Input, OnInit} from '@angular/core';
import {ActivatedRoute, ParamMap} from '@angular/router';
import {Location} from '@angular/common';


import 'rxjs/add/operator/switchMap';

import {ProfileService} from '../profile/profile.service';
import {Profile} from './profile';

@Component({
  selector: 'app-profile-detail',
  templateUrl: './profile-detail.component.html',
  styleUrls: ['./profile-detail.component.css']
})
export class ProfileDetailComponent implements OnInit {
  constructor(private profileService: ProfileService, private route: ActivatedRoute, private location: Location) {}
  @Input() profile: Profile;
  ngOnInit(): void {
    this.route.paramMap.switchMap((params: ParamMap) => this.profileService.getProfile(+params.get('id'))).subscribe(p => this.profile = p);
  }
  goBack(): void {
    this.location.back();
  }
  changeName(event: any) {
   this.profile.name = event.target.value;
   this.profileService.updateProfile(this.profile);
  }
  changeEmail(event: any) {
   this.profile.email = event.target.value;
   this.profileService.updateProfile(this.profile);
  }
}
