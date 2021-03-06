import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot } from "@angular/router";
import { AuthenticationService } from "../services/authentication.service";
import { Observable } from "rxjs";

@Injectable()
export class AuthenticationGuard implements CanActivate {
  constructor(public authenticationService: AuthenticationService, public router: Router) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> | boolean {
    return this.authenticationService.authenticate(undefined, undefined, () => {
      this.router.navigateByUrl('/login');
    })
  }
}
