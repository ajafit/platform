import { Component } from '@angular/core';
import {Router} from '@angular/router';

import {ProfileService} from '../profile/profile.service';
import {OnInit} from '@angular/core';
import {Profile} from '../profile/profile';



@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css'],
})
export class AuthComponent implements OnInit {
  constructor(private profileService: ProfileService, private router: Router) {}
  login =  '';
  senha = '';
  ngOnInit(): void {
  }
  onClick(): void {
    this.profileService.login(this.login, this.senha).then(resp => this.navigate(resp));
  }
  navigate(profile: Profile): void {
    this.router.navigate(['/']);
  }
}
