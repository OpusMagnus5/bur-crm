import {Component} from '@angular/core';
import {MatFormFieldModule} from '@angular/material/form-field';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatInput} from "@angular/material/input";
import {MatButton} from "@angular/material/button";
import {MatOption} from "@angular/material/autocomplete";
import {MatSelect} from "@angular/material/select";
import {UserHttpService} from "./service/user-http.service";
import {TranslateModule} from "@ngx-translate/core";
import {ValidationMessageService} from "../shared/service/validation-message.service";

@Component({
  selector: 'new-user',
  standalone: true,
  imports: [MatFormFieldModule, ReactiveFormsModule, MatInput, MatButton, MatOption, MatSelect, TranslateModule],
  templateUrl: './new-user.component.html',
  styleUrl: './new-user.component.css'
})
export class NewUserComponent {

  protected readonly form: FormGroup;
  protected readonly emailControl: FormControl;
  protected readonly firstNameControl: FormControl;
  protected readonly lastNameControl: FormControl;
  protected readonly roleControl: FormControl;

  protected roles: {name: string, value: string}[] = [ //TODO get roles from server
    { name: 'User', value: 'USER'},
    { name: 'Admin', value: 'ADMIN'}
  ];

  constructor(
    private httpService: UserHttpService,
    private validationMessage: ValidationMessageService
  ) {
    this.emailControl = new FormControl(null, [Validators.required, Validators.email]); //TODO async validator for email exists
    this.firstNameControl = new FormControl(null, [Validators.required, Validators.pattern('[a-zA-ZążęćłóńĄŻĘĆŁÓŃ]{1,15}')]);
    this.lastNameControl = new FormControl(null, [Validators.required, Validators.pattern('[a-zA-ZążęćłóńĄŻĘĆŁÓŃ -]{1,60}')]);
    this.roleControl = new FormControl(null, Validators.required);

    this.form = new FormGroup({
      'email': this.emailControl,
      'firstName': this.firstNameControl,
      'lastName': this.lastNameControl,
      'role': this.roleControl
    });
  }

  protected onSubmit() {

  }

  protected getValidationMessage(fieldName: string, control: FormControl): string {
      return this.validationMessage.getMessage(control, this.getValidationMessageKey, fieldName);
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'new-user.validation.' + fieldName + '.' + validation;
  }
}
