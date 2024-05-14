import {Component, Inject} from '@angular/core';
import {OnSubmitInterface} from "../shared/model/on-submit.interface";
import {catchError, Observable, of, Subject} from 'rxjs';
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
import {OperatorDetailsResponseInterface} from "./model/operator-details-response.interface";
import {map} from "rxjs/operators";
import {OperatorHttpService} from "./service/operator-http.service";
import {ValidationMessageService} from "../shared/service/validation-message.service";
import {UpdateOperatorRequestInterface} from "./model/update-operator-request.interface";
import {SnackbarService} from "../shared/service/snackbar.service";
import {MatButton} from "@angular/material/button";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-update-operator',
  standalone: true,
  imports: [
    FormsModule,
    MatButton,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    TranslateModule,
    ReactiveFormsModule,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatDialogClose
  ],
  templateUrl: './update-operator.component.html',
  styleUrl: './update-operator.component.css'
})
export class UpdateOperatorComponent implements OnSubmitInterface {

  updateConfirmation: Subject<boolean> = new Subject();

  protected readonly form: FormGroup;
  protected readonly nameControl: FormControl;
  protected readonly notesControl: FormControl;


  constructor(
    @Inject(MAT_DIALOG_DATA) protected data: OperatorDetailsResponseInterface,
    private httpService: OperatorHttpService,
    private validationMessage: ValidationMessageService,
    private snackbar: SnackbarService
  ) {
    this.nameControl = new FormControl(data.name, {
      validators: [Validators.required, Validators.pattern('[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ0-9 -/.\"\\\\]{1,150}')],
      asyncValidators: [this.validateNameOccupation.bind(this)],
      updateOn: "blur"
    });
    this.notesControl = new FormControl(data.notes);
    this.form = this.buildFormGroup();
  }

  validateNameOccupation(control: AbstractControl): Observable<ValidationErrors | null> {
    if (control.value.trim() !== this.data.name) {
      return this.httpService.getIsOperatorExists('NAME', control.value.trim()).pipe(
        map(response => (response.exists ? { 'exists': true } : null)),
        catchError(() => of(null))
      );
    }
    return of(null);
  }

  protected getValidationMessage(fieldName: string, control: FormControl): string {
    return this.validationMessage.getMessage(control, this.getValidationMessageKey, fieldName);
  }

  protected onSubmit() {
    this.httpService.update(this.mapFormToRequest()).subscribe({
      next: response => {
        this.snackbar.openTopCenterSnackbar(response.message);
        this.updateConfirmation.next(true);
      }
    })
  }

  private mapFormToRequest(): UpdateOperatorRequestInterface {
    return {
      id: this.data.id,
      version: this.data.version,
      name: this.nameControl.value,
      notes: this.notesControl.value
    }
  }

  private buildFormGroup(): FormGroup {
    return new FormGroup({
      'name': this.nameControl,
      'notes': this.notesControl
    });
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'update-operator.validation.' + fieldName + '.' + validation;
  }
}
