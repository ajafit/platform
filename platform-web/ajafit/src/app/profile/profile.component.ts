import {Component} from '@angular/core';
import {OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Profile} from './profile';
import {ProfileDetailComponent} from './profile-detail.component';
import {ProfileService} from './profile.service';


@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css'],
  providers: [ProfileService]
})
export class ProfileComponent implements OnInit {
  constructor(private profileService: ProfileService, private router: Router) {}
  filter: string;
  title = 'ajafit title';
  profiles: Profile[];
  selectedProfile: Profile;
  onKey(event: any) {
    this.filter = event.target.value;
    console.log('filter:' + this.filter);
    this.getProfiles();
  }
  onSelect(pro: Profile): void {
    this.selectedProfile = pro;
  }
  getProfiles(): void {
    this.filter = this.filter == null ? '' : this.filter;
    this.profileService.getProfiles(this.filter).then(profiles => this.setProfiles(profiles));
  }
  ngOnInit(): void {
    this.getProfiles();
  }
  goToDetail(): void {
    this.router.navigate(['/profile-detail', this.selectedProfile.id]);
  }
  setProfiles(profiles: Profile[]) {
    this.profiles = profiles;
  }
}
