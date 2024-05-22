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
import {IntermediaryHttpService} from "./intermediary-http.service";
import {SnackbarService} from "../shared/service/snackbar.service";
import {ValidationMessageService} from "../shared/service/validation-message.service";
import {NipUtils} from "../shared/util/nip-utils";
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle
} from "@angular/material/dialog";
import {catchError, Observable, of, Subject} from "rxjs";
import {map} from "rxjs/operators";
import {MatButton} from "@angular/material/button";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {TranslateModule} from "@ngx-translate/core";
import {IntermediaryDetailsResponse, UpdateIntermediaryRequest} from "./intermediary-dtos";

@Component({
  selector: 'app-update-intermediary',
  standalone: true,
  imports: [
    FormsModule,
    MatButton,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    TranslateModule,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatDialogClose,
    ReactiveFormsModule
  ],
  templateUrl: './update-intermediary.component.html'
})
export class UpdateIntermediaryComponent {

  updateConfirmation: Subject<boolean> = new Subject();
  protected readonly form: FormGroup;
  protected readonly nameControl: FormControl;
  protected readonly nipControl: FormControl;

  constructor(
    @Inject(MAT_DIALOG_DATA) protected data: IntermediaryDetailsResponse,
    private intermediaryHttp: IntermediaryHttpService,
    private snackbar: SnackbarService,
    private validationMessage: ValidationMessageService
  ) {
    this.nameControl = new FormControl(data.name, [Validators.required, Validators.pattern('[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ0-9 -/.\"\\\\]{1,150}')]);
    this.nipControl = new FormControl(data.nip, {
      validators: [Validators.required, Validators.pattern('\\d{10}'), NipUtils.validateNip],
      asyncValidators: [this.validateNipOccupation.bind(this)],
      updateOn: "blur"
    });
    this.form = this.buildFormGroup();
  }

  private buildFormGroup(): FormGroup {
    return new FormGroup({
      'nip': this.nipControl,
      'name': this.nameControl
    });
  }

  private validateNipOccupation(control: AbstractControl): Observable<ValidationErrors | null> {
    if (this.nipControl?.value === this.data.nip) {
      return of(null);
    }
    return this.intermediaryHttp.isClientExists(control.value).pipe(
      map(response => {
        return response.exists ? { 'exists': true } : null;
      }),
      catchError(() => of(null))
    );
  }

  protected onSubmit() {
    this.intermediaryHttp.update(this.mapFormToRequest()).subscribe({
      next: response => {
        this.snackbar.openTopCenterSnackbar(response.message);
        this.updateConfirmation.next(true);
      }
    })
  }

  private mapFormToRequest(): UpdateIntermediaryRequest {
    return {
      id: this.data.id,
      version: this.data.version,
      name: this.nameControl.value,
      nip: this.nipControl.value
    }
  }

  protected getValidationMessage(fieldName: string, control: FormControl): string {
    return this.validationMessage.getMessage(control, this.getValidationMessageKey, fieldName);
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'update-intermediary.validation.' + fieldName + '.' + validation;
  }
}
