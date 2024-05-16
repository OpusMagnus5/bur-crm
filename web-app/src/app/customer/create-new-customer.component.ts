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
import {CustomerHttpService} from "./customer-http.service";
import {map} from "rxjs/operators";
import {CUSTOMER_LIST_PATH} from "../app.routes";
import {CreateNewCustomerRequest, CreateNewCustomerResponse} from "./customer-dtos";
import {ActivatedRoute, Router} from "@angular/router";
import {SnackbarService} from "../shared/service/snackbar.service";
import {ValidationMessageService} from "../shared/service/validation-message.service";
import {MatButton} from "@angular/material/button";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-create-new-customer',
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
  templateUrl: './create-new-customer.component.html'
})
export class CreateNewCustomerComponent {

  protected readonly form: FormGroup;
  protected readonly nameControl: FormControl;
  protected readonly nipControl: FormControl;

  constructor(
    private customerHttp: CustomerHttpService,
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
    return this.customerHttp.isClientExists(control.value).pipe(
      map(response => {
        return response.exists ? { 'exists': true } : null;
      }),
      catchError(() => of(null))
    );
  }

  protected onSubmit() {
    this.customerHttp.createNew(this.form.value as CreateNewCustomerRequest).subscribe({
      next: response => {
        this.showPopUp(response);
        this.router.navigate(['../' + CUSTOMER_LIST_PATH], {
          relativeTo: this.activeRoute
        })
      }
    });
  }

  private showPopUp(response: CreateNewCustomerResponse) {
    this.snackbar.openTopCenterSnackbar(response.message);
  }

  protected getValidationMessage(fieldName: string, control: FormControl): string {
    return this.validationMessage.getMessage(control, this.getValidationMessageKey, fieldName);
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'new-customer.validation.' + fieldName + '.' + validation;
  }
}
