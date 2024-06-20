import {Component, signal, WritableSignal} from '@angular/core';
import {FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatError, MatFormField, MatLabel, MatSuffix} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {TranslateModule} from "@ngx-translate/core";
import {MatIcon} from "@angular/material/icon";
import {MatButton, MatIconButton} from "@angular/material/button";
import {ValidationMessageService} from "../shared/service/validation-message.service";
import {UserHttpService} from "../user/service/user-http.service";

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
    MatIconButton,
    MatSuffix,
    MatButton
  ],
  templateUrl: './login.component.html',
  styles: ['@tailwind base']
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
    private validationMessage: ValidationMessageService,
    private userHttp: UserHttpService
  ) {
  }

  protected getValidationMessage(fieldName: string, control: FormControl): string {
    return this.validationMessage.getMessage(control, this.getValidationMessageKey, fieldName);
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'login.validation.' + fieldName + '.' + validation;
  }

  protected onSubmit() {
    this.userHttp.login(this.emailControl.value!, this.passwordControl.value!).subscribe(respone => {
      console.log(respone);
    });
  }
}
