import { Component } from '@angular/core';
import { HomeAccountComponent } from "./home-account.component";
import { AuthenticationService } from "../../shared/services/authentication.service";
import { AccountService } from "../../shared/services/account.service";
import { FollowerService } from "../../shared/services/follower.service";
import { ActivatedRoute } from "@angular/router";

@Component({
  selector: 'app-followers',
  templateUrl: './home-account.component.html'
})
export class FollowersComponent extends HomeAccountComponent {
  constructor(authenticationService: AuthenticationService, accountService: AccountService, activatedRoute: ActivatedRoute,
              private followerService: FollowerService) {
    super(authenticationService, accountService, activatedRoute);

    this.title = 'Followers';
  }

  getData(userName: string) {
    this.followerService.getFollowers(userName).subscribe(data => {
      this.accounts = data;
    });
  }
}
