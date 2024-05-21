import {Component, Inject} from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  ValidationErrors,
  Validators
} from "@angular/forms";
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle
} from "@angular/material/dialog";
import {CoachDetailsResponse, UpdateCoachRequest} from "./coach-dtos";
import {catchError, Observable, of, Subject} from "rxjs";
import {map} from "rxjs/operators";
import {CoachHttpService} from "./coach-http.service";
import {ValidationMessageService} from "../shared/service/validation-message.service";
import {SnackbarService} from "../shared/service/snackbar.service";
import {TranslateModule} from "@ngx-translate/core";
import {MatButton} from "@angular/material/button";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";

@Component({
  selector: 'app-update-coach',
  standalone: true,
  imports: [
    MatDialogTitle,
    TranslateModule,
    MatDialogContent,
    FormsModule,
    MatButton,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
    MatDialogActions,
    MatDialogClose
  ],
  templateUrl: './update-coach.component.html'
})
export class UpdateCoachComponent {

  updateConfirmation: Subject<boolean> = new Subject();
  protected readonly form: FormGroup;
  protected readonly firstNameControl: FormControl;
  protected readonly lastNameControl: FormControl;
  protected readonly peselControl: FormControl;

  constructor(
    @Inject(MAT_DIALOG_DATA) protected data: CoachDetailsResponse,
    protected coachHttp: CoachHttpService,
    private validationMessage: ValidationMessageService,
    private snackbar: SnackbarService,
  ) {
    this.firstNameControl = new FormControl(data.firstName, {
      validators: [Validators.required, Validators.pattern('[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ]{1,15}')]
    });
    this.lastNameControl = new FormControl(data.lastName, {
      validators: [Validators.required, Validators.pattern('[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ -]{1,60}')]
    });
    this.peselControl = new FormControl(data.pesel, {
      validators: [Validators.required, Validators.pattern('\\d{11}')],
      asyncValidators: [this.validatePeselOccupation.bind(this)],
      updateOn: "blur"
    });
    this.form = this.buildFormGroup();
  }

  validatePeselOccupation(control: AbstractControl): Observable<ValidationErrors | null> {
    if (control.value != this.data.pesel) {
      return this.coachHttp.checkOperatorExistence(control.value.trim()).pipe(
        map(response => response.exists ? { 'exists': true } : null),
        catchError(() => of(null))
      );
    }
    return of(null);
  }

  private buildFormGroup(): FormGroup {
    return new FormGroup({
      'firstName': this.firstNameControl,
      'lastName': this.lastNameControl,
      'pesel': this.peselControl
    });
  }

  protected onSubmit() {
    this.coachHttp.update(this.mapFormToRequest()).subscribe({
      next: response => {
        this.snackbar.openTopCenterSnackbar(response.message);
        this.updateConfirmation.next(true);
      }
    })
  }

  private mapFormToRequest(): UpdateCoachRequest {
    return {
      id: this.data.id,
      version: this.data.version,
      firstName: this.firstNameControl.value.trim(),
      lastName: this.lastNameControl.value.trim(),
      pesel: this.peselControl.value.trim()
    }
  }

  protected getValidationMessage(fieldName: string, control: FormControl): string {
    return this.validationMessage.getMessage(control, this.getValidationMessageKey, fieldName);
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'update-coach.validation.' + fieldName + '.' + validation;
  }
}
