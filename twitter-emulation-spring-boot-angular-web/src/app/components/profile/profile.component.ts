import { Component, OnInit } from '@angular/core';
import { NgForm } from "@angular/forms";
import { ValidationService } from "../../services/validation.service";
import { Account } from "../../models/account.model";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html'
})
export class ProfileComponent implements OnInit {
  formSubmitted: boolean = false;
  oldAccount: Account = new Account();

  constructor(private validation: ValidationService) {
  }

  ngOnInit() {
  }

  save(account: Account) {
    //TODO: implement
  }

  submitForm(form: NgForm) {
    this.formSubmitted = true;
    if (form.valid) {
      this.save(this.oldAccount);
      this.oldAccount = new Account();
      form.reset();
      this.formSubmitted = false;
    }
  }

  getFormValidationMessages(form: NgForm): string[] {
    return this.validation.getFormValidationMessages(form);
  }
}
