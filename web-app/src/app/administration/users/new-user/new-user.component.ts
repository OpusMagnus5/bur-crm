import {Component} from '@angular/core';
import {MatFormFieldModule} from '@angular/material/form-field';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatInput} from "@angular/material/input";
import {MatButton} from "@angular/material/button";
import {MatOption} from "@angular/material/autocomplete";
import {MatSelect} from "@angular/material/select";
import {ValidationMessageService} from "../../../shared/service/validation-message.service";
import {FormComponentSelector} from "../../../shared/form-component-selector.class";

@Component({
  selector: FormComponentSelector.NEW_USER,
  standalone: true,
  imports: [MatFormFieldModule, ReactiveFormsModule, MatInput, MatButton, MatOption, MatSelect],
  templateUrl: './new-user.component.html',
  styleUrl: './new-user.component.css'
})
export class NewUserComponent {
  protected readonly selector: string = FormComponentSelector.NEW_USER;

  static readonly EMAIL_FIELD: string = 'email';
  static readonly FIRST_NAME_FIELD: string = 'firstName';
  static readonly LAST_NAME_FIELD: string = 'lastName';
  static readonly ROLE_FIELD: string = 'role';

  protected readonly form: FormGroup;
  protected readonly emailControl: FormControl;
  protected readonly firstNameControl: FormControl;
  protected readonly lastNameControl: FormControl;
  protected readonly roleControl: FormControl;

  protected roles: {name: string, value: string}[] = [ //TODO get roles from server
    { name: 'User', value: 'USER'},
    { name: 'Admin', value: 'ADMIN'}
  ];

  constructor(protected validationMessageService: ValidationMessageService) {
    this.emailControl = new FormControl(null, [Validators.required, Validators.email]); //TODO async validator for email exists
    this.firstNameControl = new FormControl(null, [Validators.required, Validators.pattern('[a-zA-ZążęćłóńĄŻĘĆŁÓŃ]{1,15}')]);
    this.lastNameControl = new FormControl(null, [Validators.required, Validators.pattern('[a-zA-ZążęćłóńĄŻĘĆŁÓŃ -]{1,60}')]);
    this.roleControl = new FormControl(null, Validators.required);

    this.form = new FormGroup({
      EMAIL_FIELD: this.emailControl,
      FIRST_NAME_FIELD: this.firstNameControl,
      LAST_NAME_FIELD: this.lastNameControl,
      ROLE_FIELD: this.roleControl
    });
  }

  onSubmit() {
    //TODO handle submit
    console.log(this.form);
  }
}
