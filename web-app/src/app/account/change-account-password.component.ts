import {Component} from '@angular/core';
import {AuthService} from "../auth/auth.service";
import {UserHttpService} from "../user/service/user-http.service";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {TranslateModule} from "@ngx-translate/core";
import {ValidationMessageService} from "../shared/service/validation-message.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ChangeUserPasswordRequest} from "../user/model/user-dtos";
import {SnackbarService} from "../shared/service/snackbar.service";
import {ACCOUNT_DETAILS_PATH} from "../app.routes";
import {MatButton} from "@angular/material/button";

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
    MatButton
  ],
  templateUrl: './change-account-password.component.html',
  styles: ['@tailwind base']
})
export class ChangeAccountPasswordComponent {

  private readonly id: string;
  protected readonly userDetailsVersion: number;
  protected readonly oldPasswordControl: FormControl<string | null> = new FormControl(null, [Validators.required]);
  protected readonly newPasswordControl: FormControl<string | null> = new FormControl(null, [Validators.required]);
  protected readonly repeatedNewPasswordControl: FormControl<string | null> = new FormControl(null, [Validators.required]);
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

  protected getValidationMessage(fieldName: string, control: FormControl): string {
    return this.validationMessage.getMessage(control, this.getValidationMessageKey, fieldName);
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'change-account-password.validation.' + fieldName + '.' + validation;
  }
}
