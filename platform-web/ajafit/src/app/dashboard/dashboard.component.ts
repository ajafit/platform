import { Component } from '@angular/core';
import {ProfileService} from '../profile/profile.service';
import {OnInit} from '@angular/core';
import {Profile} from '../profile/profile';



@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css'],
})
export class DashboardComponent implements OnInit {
  constructor(private profileService: ProfileService) {}
  profiles: Profile[];
  getProfiles(): void {
 /* this.profileService.getProfiles('e').then(profiles => this.profiles = profiles.slice(2, 4)); */
  }
  ngOnInit(): void {
   /* this.getProfiles(); */
  }
}
