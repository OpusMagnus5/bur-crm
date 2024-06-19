import {Component, signal, WritableSignal} from '@angular/core';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {TranslateModule} from "@ngx-translate/core";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {ValidationMessageService} from "../shared/service/validation-message.service";

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    TranslateModule,
    ReactiveFormsModule,
    MatIcon,
    MatIconButton
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  protected passwordVisibility: WritableSignal<boolean> = signal(false);

  protected readonly emailControl: FormControl<string | null> = new FormControl(null, [
    Validators.required, Validators.email
  ]);
  protected readonly passwordControl: FormControl<string | null> = new FormControl(null, [
    Validators.required, Validators.minLength(12)
  ]);
  protected readonly form: FormGroup = new FormGroup({
    'email': this.emailControl,
    'password': this.passwordControl
  });

  constructor(
    private validationMessage: ValidationMessageService
  ) {
  }

  protected getValidationMessage(fieldName: string, control: FormControl): string {
    return this.validationMessage.getMessage(control, this.getValidationMessageKey, fieldName);
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'login.validation.' + fieldName + '.' + validation;
  }

  onSubmit() {

  }
}
