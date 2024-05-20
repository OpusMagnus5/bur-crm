import {Component} from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators
} from "@angular/forms";
import {CoachHttpService} from "./coach-http.service";
import {COACH_LIST_PATH} from "../app.routes";
import {CreateNewCoachRequest, CreateNewCoachResponse} from "./coach-dtos";
import {SnackbarService} from "../shared/service/snackbar.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ValidationMessageService} from "../shared/service/validation-message.service";
import {MatButton} from "@angular/material/button";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {TranslateModule} from "@ngx-translate/core";
import {catchError, Observable, of} from "rxjs";
import {map} from "rxjs/operators";

@Component({
  selector: 'app-create-new-coach',
  standalone: true,
  imports: [
    MatButton,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
    TranslateModule
  ],
  templateUrl: './create-new-coach.component.html'
})
export class CreateNewCoachComponent {

  protected readonly form: FormGroup;
  protected readonly firstNameControl: FormControl;
  protected readonly lastNameControl: FormControl;
  protected readonly peselControl: FormControl;

  constructor(
    protected coachHttp: CoachHttpService,
    private snackbar: SnackbarService,
    private router: Router,
    private activeRoute: ActivatedRoute,
    private validationMessage: ValidationMessageService
  ) {
    this.firstNameControl = new FormControl(null, {
      validators: [Validators.required, Validators.pattern('[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ]{1,15}')]
    });
    this.lastNameControl = new FormControl(null, {
      validators: [Validators.required, Validators.pattern('[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ -]{1,60}')]
    });
    this.peselControl = new FormControl(null, {
      validators: [Validators.required, Validators.pattern('\\d{11}')],
      asyncValidators: [this.validatePeselOccupation.bind(this)],
      updateOn: "blur"
    });
    this.form = this.buildFormGroup();
  }

  validatePeselOccupation(control: AbstractControl): Observable<ValidationErrors | null> {
    return this.coachHttp.checkOperatorExistence(control.value.trim()).pipe(
      map(response => response.exists ? { 'exists': true } : null),
      catchError(() => of(null))
    );
  }

  private buildFormGroup(): FormGroup {
    return new FormGroup({
      'firstName': this.firstNameControl,
      'lastName': this.lastNameControl,
      'pesel': this.peselControl
    });
  }

  protected onSubmit() {
    const request: CreateNewCoachRequest = this.buildCreateNewCoachRequest();
    this.coachHttp.createNew(request).subscribe({
      next: response => {
        this.showPopUp(response);
        this.router.navigate(['../' + COACH_LIST_PATH], {
          relativeTo: this.activeRoute
        });
      }
    });
  }

  private buildCreateNewCoachRequest(): CreateNewCoachRequest {
    return {
      firstName: this.firstNameControl.value.trim(),
      lastName: this.lastNameControl.value.trim(),
      pesel: this.peselControl.value.trim()
    }
  }

  private showPopUp(response: CreateNewCoachResponse) {
    this.snackbar.openTopCenterSnackbar(response.message);
  }

  protected getValidationMessage(fieldName: string, control: FormControl): string {
    return this.validationMessage.getMessage(control, this.getValidationMessageKey, fieldName);
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'new-coach.validation.' + fieldName + '.' + validation;
  }
}
