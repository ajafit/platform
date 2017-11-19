import {Injectable} from '@angular/core';
import {Profile} from './profile';
import {Http, Headers, Response, RequestOptions} from '@angular/http';
import {HttpHeaders} from '@angular/common/http';
import 'rxjs/add/operator/toPromise';

const headers = new Headers();
const opts = new RequestOptions();
opts.headers = headers;
opts.withCredentials = true;

@Injectable()
export class ProfileService {
  profile: Profile;
  profilesURL = 'http://www.ajafit.com.br/ajafit/platform/service/profile/persons/';
  profileRegisterURL = 'http://www.ajafit.com.br/ajafit/platform/service/profile/person/register';
  validationURL = 'http://www.ajafit.com.br/ajafit/platform/service/profile/person/validation';
  profileUpdateURL = 'http://www.ajafit.com.br/ajafit/platform/service/profile/person/update';
  profileLoginURL = 'http://www.ajafit.com.br/ajafit/platform/service/profile/person/login';
  profileLogoutURL = 'http://www.ajafit.com.br/ajafit/platform/service/profile/person/logout';
  profileCurrentURL = 'http://www.ajafit.com.br/ajafit/platform/service/profile/person/current';
  constructor(private http: Http) {
  this.loadCurrentProfile();
  }
  getProfile(id: number): Promise<Profile> {
    return this.getProfiles('e').then(profiles => profiles.find(profile => profile.id === id));
  }
  getProfiles(filter: string): Promise<Profile[]> {
      const options = new RequestOptions();
      options.withCredentials = true;
      const a = this.profilesURL + filter;
      return this.http.get(a,  { withCredentials: true }).toPromise().then(respo => this.parse(respo)).catch(this.error);
  }
  loadCurrentProfile(): void {
    if (sessionStorage.getItem('_ajafit_logged') === 'true') {
     const a = this.profileCurrentURL;
     this.http.get(a,  { withCredentials: true }).toPromise().then(respo => this.setSelectedProfile(this.parse1(respo))).catch(this.error);
    }
  }
  loadValidation(): Promise<Profile> {
     const a = this.validationURL;
     return this.http.get(a,  { withCredentials: true }).toPromise().then(respo => this.parse1(respo));
  }

  setSelectedProfile(profile: Profile): Profile {
    this.profile = profile;
    sessionStorage.setItem('_ajafit_logged', 'true');
    return this.profile;
  }
  getSelectedProfile(): Profile {
    return this.profile;
  }
  cleanSelectedProfile(): void {
    sessionStorage.removeItem('_ajafit_logged');
    sessionStorage.clear();
    this.profile = null;
  }
  parse(data: Response): Profile[] {
    return (data.json() as Profile[]);
  }
  parse1(data: Response): Profile {
    return (data.json() as Profile);
  }
  parse2(data: Response): Profile {
    this.profile = (data.json() as Profile);
    sessionStorage.setItem('_ajafit_logged', 'true');
    return (data.json() as Profile);
  }
  getProfilesSlowly(): Promise<Profile[]> {
    return new Promise((resolve, resolver) => {
      setTimeout(() => resolve(this.getProfiles('e')), 5000);
    });
  }
  private error(error: any): Promise<any> {
    console.log('vai:' + this.parse1(error).descriptions);
    return Promise.reject(this.parse1(error).descriptions);
  }
  updateProfile(profile: Profile): Promise<Profile> {
    profile.accessToken = '123';
    return this.http.put(this.profileUpdateURL, profile, opts).toPromise().then(respo => this.parse1(respo));
  }
  login(profile: Profile): Promise<Profile> {
    return this.http.post(this.profileLoginURL, profile, opts).toPromise().then(respo => this.setSelectedProfile(this.parse2(respo))).catch(r => this.error(r));
   }

   register(profile: Profile): Promise<Profile> {
    return this.http.post(this.profileRegisterURL, profile, opts).toPromise().then(respo => this.parse2(respo)).catch(r => this.error(r));
   }
   logout(): void {
     this.http.post(this.profileLogoutURL, {}, opts).toPromise().then();
     this.cleanSelectedProfile();
   }
}
