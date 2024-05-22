import {Component} from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  ValidationErrors,
  Validators
} from "@angular/forms";
import {NipUtils} from "../shared/util/nip-utils";
import {catchError, Observable, of} from "rxjs";
import {IntermediaryHttpService} from "./intermediary-http.service";
import {map} from "rxjs/operators";
import {CUSTOMER_LIST_PATH} from "../app.routes";
import {ActivatedRoute, Router} from "@angular/router";
import {SnackbarService} from "../shared/service/snackbar.service";
import {ValidationMessageService} from "../shared/service/validation-message.service";
import {MatButton} from "@angular/material/button";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {TranslateModule} from "@ngx-translate/core";
import {CreateNewIntermediaryRequest, CreateNewIntermediaryResponse} from "./intermediary-dtos";

@Component({
  selector: 'app-create-new-intermediary',
  standalone: true,
  imports: [
    FormsModule,
    MatButton,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    TranslateModule,
    ReactiveFormsModule
  ],
  templateUrl: './create-new-intermediary.component.html'
})
export class CreateNewIntermediaryComponent {

  protected readonly form: FormGroup;
  protected readonly nameControl: FormControl;
  protected readonly nipControl: FormControl;

  constructor(
    private intermediaryHttp: IntermediaryHttpService,
    private router: Router,
    private activeRoute: ActivatedRoute,
    private snackbar: SnackbarService,
    private validationMessage: ValidationMessageService
  ) {
    this.nameControl = new FormControl(null, [Validators.required, Validators.pattern('[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ0-9 -/.\"\\\\]{1,150}')]);
    this.nipControl = new FormControl(null, {
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
    return this.intermediaryHttp.isClientExists(control.value).pipe(
      map(response => {
        return response.exists ? { 'exists': true } : null;
      }),
      catchError(() => of(null))
    );
  }

  protected onSubmit() {
    this.intermediaryHttp.createNew(this.form.value as CreateNewIntermediaryRequest).subscribe({
      next: response => {
        this.showPopUp(response);
        this.router.navigate(['../' + CUSTOMER_LIST_PATH], {
          relativeTo: this.activeRoute
        })
      }
    });
  }

  private showPopUp(response: CreateNewIntermediaryResponse) {
    this.snackbar.openTopCenterSnackbar(response.message);
  }

  protected getValidationMessage(fieldName: string, control: FormControl): string {
    return this.validationMessage.getMessage(control, this.getValidationMessageKey, fieldName);
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'new-intermediary.validation.' + fieldName + '.' + validation;
  }
}
