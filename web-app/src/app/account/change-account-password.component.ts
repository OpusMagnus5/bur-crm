import {Component, signal, WritableSignal} from '@angular/core';
import {AuthService} from "../auth/auth.service";
import {UserHttpService} from "../user/service/user-http.service";
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators
} from "@angular/forms";
import {MatError, MatFormField, MatLabel, MatSuffix} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {TranslateModule} from "@ngx-translate/core";
import {ValidationMessageService} from "../shared/service/validation-message.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ChangeUserPasswordRequest} from "../user/model/user-dtos";
import {SnackbarService} from "../shared/service/snackbar.service";
import {ACCOUNT_DETAILS_PATH} from "../app.routes";
import {MatButton, MatIconButton} from "@angular/material/button";
import {MatIcon} from "@angular/material/icon";
import {MatTooltip} from "@angular/material/tooltip";

@Component({
  selector: 'app-change-account-password',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormField,
    MatInput,
    MatLabel,
    TranslateModule,
    MatError,
    MatButton,
    MatIcon,
    MatIconButton,
    MatSuffix,
    MatTooltip
  ],
  templateUrl: './change-account-password.component.html',
  styles: ['@tailwind base']
})
export class ChangeAccountPasswordComponent {

  private readonly ALLOWED_CAPITAL_LETTERS: string = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
  private readonly ALLOWED_LOWER_LETTERS: string = 'abcdefghijklmnopqrstuvwxyz';
  private readonly ALLOWED_DIGITS: string = '0123456789';
  private readonly ALLOWED_SPECIAL_CHARS: string = '!@#$%^&*()';
  private readonly MIN_PASSWORD_LENGTH: number = 12;

  private readonly id: string;
  protected readonly userDetailsVersion: number;
  protected oldPasswordVisibility: WritableSignal<boolean> = signal(false);
  protected newPasswordVisibility: WritableSignal<boolean> = signal(false);
  protected repeatedNewPasswordVisibility: WritableSignal<boolean> = signal(false);
  protected readonly oldPasswordControl: FormControl<string | null> = new FormControl(null,
    [Validators.required]
  );
  protected readonly newPasswordControl: FormControl<string | null> = new FormControl(null,
    [Validators.required, this.validatePassword.bind(this)]
  );
  protected readonly repeatedNewPasswordControl: FormControl<string | null> = new FormControl(null,
    [Validators.required, this.validateRepeatedPassword.bind(this)]
  );
  protected readonly form: FormGroup = new FormGroup({
    'newPassword': this.newPasswordControl,
    'oldPassword': this.oldPasswordControl,
    'repeatedNewPassword': this.repeatedNewPasswordControl
  })

  constructor(
    private http: UserHttpService,
    private route: ActivatedRoute,
    private authService: AuthService,
    private validationMessage: ValidationMessageService,
    private snackbar: SnackbarService,
    private router: Router
  ) {
    this.id = this.authService.getAuthData()!.id;
    this.userDetailsVersion = Number(this.route.snapshot.queryParamMap.get('userVersion')!);
  }

  protected onSubmit() {
    let request = this.buildRequest();
    this.http.changePassword(request).subscribe(response => {
      this.snackbar.openTopCenterSnackbar(response.message);
      this.router.navigate(['../', ACCOUNT_DETAILS_PATH], { relativeTo: this.route })
    })
  }

  private buildRequest(): ChangeUserPasswordRequest {
    return {
      userId: this.id,
      newPassword: this.newPasswordControl.value!,
      oldPassword: this.oldPasswordControl.value!,
      version: this.userDetailsVersion,
    }
  }

  private validatePassword(control: AbstractControl): ValidationErrors | null {
    const value: string = control.value;
    if(!value) {
      return null;
    }
    const valueChars: string[] = control.value.trim().split('');
    const valid: boolean = valueChars.some(char => this.ALLOWED_CAPITAL_LETTERS.includes(char)) &&
      valueChars.some(char => this.ALLOWED_LOWER_LETTERS.includes(char)) &&
      valueChars.some(char => this.ALLOWED_DIGITS.includes(char)) &&
    valueChars.some(char => this.ALLOWED_SPECIAL_CHARS.includes(char)) &&
      valueChars.length >= this.MIN_PASSWORD_LENGTH;

    return valid ? null : { 'incorrect': true };
  }

  private validateRepeatedPassword(control: AbstractControl): ValidationErrors | null {
    const value: string = control.value;
    if(!value) {
      return null;
    }
    const valid: boolean = value === this.newPasswordControl?.value;
    return valid ? null : { 'notTheSame': true };
  }

  protected getValidationMessage(fieldName: string, control: FormControl): string {
    return this.validationMessage.getMessage(control, this.getValidationMessageKey, fieldName);
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'change-account-password.validation.' + fieldName + '.' + validation;
  }
}
