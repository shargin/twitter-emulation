import { Component, Inject } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { Observer } from "rxjs";
import { HomeTweetsComponent } from "./home-tweets.component";
import { AuthenticationService } from "../../shared/services/authentication.service";
import { TweetService } from "../../shared/services/tweet.service";
import { DATA_USERNAME } from "../../shared/models/user.model";

@Component({
  selector: 'app-tweets',
  templateUrl: './home-tweets.component.html'
})
export class TweetsComponent extends HomeTweetsComponent {
  constructor(authenticationService: AuthenticationService, activatedRoute: ActivatedRoute,
              @Inject(DATA_USERNAME) observer: Observer<string>, private tweetService: TweetService) {
    super(authenticationService, activatedRoute, observer);

    this.title = 'Tweets';
  }

  getData(userName: string) {
    this.tweetService.getTweets(userName).subscribe(data => {
      this.tweets = data;
    });
  }
}
