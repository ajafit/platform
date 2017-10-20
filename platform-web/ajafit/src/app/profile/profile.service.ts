import {Injectable} from '@angular/core';
import {Profile} from './profile';
import {Http, Headers, Response, RequestOptions} from '@angular/http';
import {HttpHeaders} from '@angular/common/http';
import 'rxjs/add/operator/toPromise';

const headers = new Headers();
headers.append('authorization', '1234567890');
const opts = new RequestOptions();
opts.headers = headers;
opts.withCredentials = true;

@Injectable()
export class ProfileService {
  profile: Profile;
  profilesURL = 'http://localhost:8080/ajafit/platform/service/profile/persons/';
  profileUpdateURL = 'http://localhost:8080/ajafit/platform/service/profile/person/update';
  profileLoginURL = 'http://localhost:8080/ajafit/platform/service/profile/person/login';
  constructor(private http: Http) {}
  getProfile(id: number): Promise<Profile> {
    return this.getProfiles('e').then(profiles => profiles.find(profile => profile.id === id));
  }
  getProfiles(filter: string): Promise<Profile[]> {
      const options = new RequestOptions();
      options.withCredentials = true;
      const a = this.profilesURL + filter;
      return this.http.get(a,  { withCredentials: true }).toPromise().then(respo => this.parse(respo)).catch(this.error);
  }
  getSelectedProfile(): Profile {
    return this.profile;
  }
  cleanSelectedProfile(): void {
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
    console.log('baca inicio');
    this.updateProfile(this.profile);
    console.log('baca meio');
    this.getProfile(this.profile.id);
    console.log('baca fim');
    return this.profile;
  }
  getProfilesSlowly(): Promise<Profile[]> {
    return new Promise((resolve, resolver) => {
      setTimeout(() => resolve(this.getProfiles('e')), 5000);
    });
  }
  private error(error: any): Promise<any> {
    console.log('deu merda!!');
    return Promise.reject('>>' + error.status + '<<' || error);
  }
  updateProfile(profile: Profile): Promise<Profile> {
    profile.accessToken = '123';
    return this.http.put(this.profileUpdateURL, profile, opts).toPromise().then(respo => this.parse1(respo));
  }
  login(email: string, senha: string): Promise<Profile> {
    const profile = new Profile();
    profile.email = email;
    profile.password = senha;
    return this.http.post(this.profileLoginURL, profile, opts).toPromise().then(respo => this.parse2(respo));
   }
}
