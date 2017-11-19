import {Component} from '@angular/core';


import { FormsModule, FormGroup, FormControl, Validators, ReactiveFormsModule, ValidatorFn, AbstractControl}   from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';

import {Router} from '@angular/router';

import {ProfileService} from '../profile/profile.service';
import {OnInit} from '@angular/core';
import {Profile} from '../profile/profile';
import {AppComponent} from '../app.component';


declare var jquery: any;
declare var $: any;

@Component({
  selector: 'app-auth',
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.css'],
})
export class AuthComponent implements OnInit {
  constructor(private profileService: ProfileService, private router: Router, private appComponent: AppComponent) {}
  login : string;
  password : string;
  rLogin : string;
  rPassword : string;
  serverMessage: string;
  validation: Profile;
  iconLogin: string;
  iconRegister: string;
  name: string;
  heroForm: FormGroup;
  loginForm: FormGroup;
  registerForm: FormGroup;
  
  
   
   
  onSubmit() { console.log('submited......'+this.name); }
  ngOnInit(): void {
    this.profileService.loadValidation().then(resp => this.setValidation(resp));
 //   this.heroForm = new FormGroup({'name': new FormControl(this.name, [Validators.required,Validators.email,  Validators.minLength(4), forbiddenNameValidator(/alicia/i)])});
       this.heroForm = new FormGroup({'name': new FormControl(this.name, [Validators.required, Validators.minLength(4), forbiddenNameValidator(/alicia/i)])});
       this.loginForm = new FormGroup({'login': new FormControl(this.login, [Validators.email]),'password': new FormControl(this.password, [Validators.required,Validators.minLength(8)])});
       this.registerForm = new FormGroup({'login': new FormControl(this.rLogin, [Validators.email]),'password': new FormControl(this.rPassword, [Validators.required,Validators.minLength(8)])});
       $("password").attr("autocomplete", "off"); 
       console.log('iniciado..');
    
  }
  
  setValidation(val: Profile): void {
    console.log('validations:'+val);
    this.validation = val;
    this.validation.email = null;
    console.log('validation email: '+this.validation.email);
  }
  onClickLogin(): void {
    console.log('login:'+this.login);
    this.validation.email = this.login;
    this.validation.password = this.password;
    this.validation.validationChoose = this.iconLogin;
    this.profileService.login(this.validation).then(resp => this.logged(resp)).catch(e => this.message(e));
  }
  onClickRegister(): void {
    this.validation.email = this.rLogin;
    this.validation.password = this.rPassword;
    this.validation.validationChoose = this.iconRegister;
    console.log('validationChoose: '+this.iconRegister);
    this.profileService.register(this.validation).then(resp => this.registered(resp)).catch(e => this.message(e));
  }
  navigate(profile: Profile): void {
    this.router.navigate(['/']);
    this.appComponent.ngOnInit();
  }
  onKeyLogin(e: any): void {
    if (e.key === 'Enter' && this.iconLogin) {
      this.onClickLogin();
    }
  }
  logged(profile: Profile): void {
    console.log('logged: '+ profile);
    this.navigate(profile);
  }
  registered(profile: Profile): void {
    this.message(profile.descriptions);
    this.registerForm.reset();
    this.appComponent.ngOnInit();
  }
  message(e: any): void {
    console.log('mensagem:' + e);
    this.serverMessage = e;
    $('#serverMessage').modal('toggle');
    this.ngOnInit();
  }
  onKeyRegister(e: any): void {
  if( e.key === 'Enter' ) {
    this.onClickRegister();
  }
 }
  setIconRegister(icon: string): void {
    this.iconRegister = icon;
  }
  setIconLogin(icon: string): void {
    this.iconLogin = icon;
  }
  
}
export function forbiddenNameValidator(nameRe: RegExp): ValidatorFn {
  return (control: AbstractControl): {[key: string]: any} => {
    const forbidden = nameRe.test(control.value);
    return forbidden ? {'forbiddenName': {value: control.value}} : null;
  };
}
