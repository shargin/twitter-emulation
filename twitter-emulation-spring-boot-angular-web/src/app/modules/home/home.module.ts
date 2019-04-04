import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AccountComponent } from "./account.component";
import { AccountInfoComponent } from "./account-info.component";
import { FollowComponent } from "./follow.component";
import { SearchComponent } from "./search.component";
import { TopBarComponent } from "./top-bar.component";
import { MessageModule } from "../message/message.module";

@NgModule({
  declarations: [
    AccountComponent,
    AccountInfoComponent,
    FollowComponent,
    SearchComponent,
    TopBarComponent
  ],
  imports: [
    CommonModule,
    MessageModule
  ]
})
export class HomeModule {
}