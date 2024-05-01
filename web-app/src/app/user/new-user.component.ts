import {Component} from '@angular/core';
import {MatFormFieldModule} from '@angular/material/form-field';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators
} from "@angular/forms";
import {MatInput} from "@angular/material/input";
import {MatButton} from "@angular/material/button";
import {MatOption} from "@angular/material/autocomplete";
import {MatSelect} from "@angular/material/select";
import {UserHttpService} from "./service/user-http.service";
import {TranslateModule} from "@ngx-translate/core";
import {ValidationMessageService} from "../shared/service/validation-message.service";
import {catchError, Observable, of} from "rxjs";
import {map} from "rxjs/operators";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {NewUserDetailsComponent} from "./new-user-details.component";
import {CreateNewUserResponseInterface} from "./model/create-new-user-response.interface";
import {CreateNewUserRequestInterface} from "./model/create-new-user-request.interface";
import {ActivatedRoute, Router} from "@angular/router";
import {USER_LIST_PATH} from "../app.routes";

@Component({
  selector: 'new-user',
  standalone: true,
  imports: [
    MatFormFieldModule,
    ReactiveFormsModule,
    MatInput,
    MatButton,
    MatOption,
    MatSelect,
    TranslateModule],
  templateUrl: './new-user.component.html',
  styleUrl: './new-user.component.css'
})
export class NewUserComponent {

  protected readonly form: FormGroup;
  protected readonly emailControl: FormControl;
  protected readonly firstNameControl: FormControl;
  protected readonly lastNameControl: FormControl;
  protected readonly roleControl: FormControl;

  protected roles: {name: string, value: string}[] = [];

  constructor(
    private httpService: UserHttpService,
    private validationMessage: ValidationMessageService,
    private dialog: MatDialog,
    private router: Router,
    private activeRoute: ActivatedRoute
  ) {
    this.emailControl = new FormControl(null, {
      validators: [Validators.required, Validators.email],
      asyncValidators: [this.validateEmailOccupation.bind(this)],
      updateOn: 'blur'
    });
    this.firstNameControl = new FormControl(null, [Validators.required, Validators.pattern('[a-zA-ZążęćłóńĄŻĘĆŁÓŃ]{1,15}')]);
    this.lastNameControl = new FormControl(null, [Validators.required, Validators.pattern('[a-zA-ZążęćłóńĄŻĘĆŁÓŃ -]{1,60}')]);
    this.roleControl = new FormControl(null, Validators.required);
    this.form = this.buildFormGroup();

    this.getRolesFromServer();
  }

  private buildFormGroup(): FormGroup {
    return new FormGroup({
      'email': this.emailControl,
      'firstName': this.firstNameControl,
      'lastName': this.lastNameControl,
      'role': this.roleControl
    });
  }

  private getRolesFromServer() {
    this.httpService.getAllRoles().subscribe((response) =>
      response.roles.forEach(role => {
        this.roles.push({name: role.name, value: role.role})
      })
    );
  }

  validateEmailOccupation(control: AbstractControl): Observable<ValidationErrors | null> {
    return this.httpService.getIsUserExists('EMAIL', control.value.trim()).pipe(
      map(response => (response.exists ? { 'exists': true } : null)),
      catchError(() => of(null))
    );
  }

  protected onSubmit() {
    this.httpService.createNew(this.form.value as CreateNewUserRequestInterface).subscribe({
      next: response => this.openDialog(response)
    });
  }

  private openDialog(response: CreateNewUserResponseInterface) {
    const dialogRef: MatDialogRef<NewUserDetailsComponent> = this.dialog.open(NewUserDetailsComponent, {
      data: {
        login: response.login.trim(),
        password: response.password.trim()
      },
      maxWidth: '50em',
      disableClose: true
    });

    dialogRef.afterClosed().subscribe(() =>
      this.router.navigate(['../' + USER_LIST_PATH], {
        relativeTo: this.activeRoute
      })
    )
  }

  protected getValidationMessage(fieldName: string, control: FormControl): string {
      return this.validationMessage.getMessage(control, this.getValidationMessageKey, fieldName);
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'new-user.validation.' + fieldName + '.' + validation;
  }
}
