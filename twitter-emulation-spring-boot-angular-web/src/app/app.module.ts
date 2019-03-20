import { BrowserModule } from '@angular/platform-browser';
import { Injectable, NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { RouterModule, Routes } from '@angular/router';

import { AppComponent } from './app.component';
import { AccountComponent } from './components/home/account.component';
import { AccountInfoComponent } from './components/home/account-info.component';
import { DeleteAccountComponent } from './components/profile/delete-account.component';
import { FollowComponent } from './components/home/follow.component';
import { LoginComponent } from './components/login/login.component';
import { NewTweetComponent } from './components/tweet/new-tweet.component';
import { NotFoundComponent } from './components/unknown/not-found.component';
import { ProfileComponent } from './components/profile/profile.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { SearchComponent } from './components/home/search.component';
import { TopBarComponent } from './components/home/top-bar.component';
import { AutofocusDirective } from './directives/autofocus.directive';
import { EqualValidatorDirective } from './directives/equal-validator.directive';
import { AuthenticationGuard } from "./guards/authentication.guard";
import { AccountService } from './services/account.service';
import { AuthenticationService } from './services/authentication.service';
import { TweetService } from './services/tweet.service';
import { ValidationService } from './services/validation.service';

@Injectable()
export class XhrInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const xhr = req.clone({
      headers: req.headers.set('X-Requested-With', 'XMLHttpRequest')
    });
    return next.handle(xhr);
  }
}

const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'account/register', component: RegistrationComponent},
  {path: 'account/show', component: AccountComponent, canActivate: [AuthenticationGuard]},
  {path: 'account/profile', component: ProfileComponent, canActivate: [AuthenticationGuard]},
  {path: 'account/delete', component: DeleteAccountComponent, canActivate: [AuthenticationGuard]},
  {path: 'account/search', component: SearchComponent, canActivate: [AuthenticationGuard]},
  {path: 'tweet', component: NewTweetComponent, canActivate: [AuthenticationGuard]},
  {path: '', pathMatch: 'full', redirectTo: 'account/show'},
  {path: "**", component: NotFoundComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    AccountComponent,
    LoginComponent,
    RegistrationComponent,
    ProfileComponent,
    DeleteAccountComponent,
    NewTweetComponent,
    SearchComponent,
    NotFoundComponent,
    TopBarComponent,
    AccountInfoComponent,
    FollowComponent,
    EqualValidatorDirective,
    AutofocusDirective
  ],
  imports: [
    RouterModule.forRoot(routes),
    BrowserModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [AccountService, AuthenticationService, AuthenticationGuard, TweetService, ValidationService, {
    provide: HTTP_INTERCEPTORS,
    useClass: XhrInterceptor,
    multi: true
  }],
  bootstrap: [AppComponent]
})

export class AppModule {
}
