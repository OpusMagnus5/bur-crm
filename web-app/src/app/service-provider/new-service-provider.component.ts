import {Component} from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators
} from "@angular/forms";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {MatInput} from "@angular/material/input";
import {ValidationMessageService} from "../shared/service/validation-message.service";
import {MatButton} from "@angular/material/button";
import {ServiceProviderHttpService} from "./service/service-provider-http.service";
import {catchError, forkJoin, Observable, of} from "rxjs";
import {map} from "rxjs/operators";
import {ServiceProviderCreateNewRequestInterface} from "./model/service-provider-create-new-request.interface";
import {MatSnackBar} from "@angular/material/snack-bar";
import {ServiceProviderCreateNewResponseInterface} from "./model/service-provider-create-new-response.interface";
import {ActivatedRoute, Router} from "@angular/router";
import {SERVICE_PROVIDER_LIST_PATH} from "../app.routes";

@Component({
  selector: 'app-new-service-provider',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormField,
    TranslateModule,
    MatInput,
    MatButton,
    MatError,
    MatLabel
  ],
  templateUrl: './new-service-provider.component.html',
  styleUrl: './new-service-provider.component.css'
})
export class NewServiceProviderComponent {

  protected readonly form: FormGroup;
  protected readonly nipControl: FormControl;
  protected readonly nameControl: FormControl;

  constructor(
    private validationMessage: ValidationMessageService,
    private httpService: ServiceProviderHttpService,
    private snackBar: MatSnackBar,
    private translator: TranslateService,
    private router: Router,
    private activeRoute: ActivatedRoute
  ) {
    this.nipControl = new FormControl(null, {
     validators: [Validators.required, Validators.pattern('\\d{10}'), this.validateNip.bind(this)],
     asyncValidators: [this.validateNipOccupation.bind(this)],
     updateOn: 'blur'
    });
    this.nameControl = new FormControl(null,
      [Validators.required, Validators.pattern('[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ -/.\"\\\\]{1,150}')])
    this.form = this.buildFormGroup();
  }

  private buildFormGroup(): FormGroup {
    return new FormGroup({
      'nip': this.nipControl,
      'name': this.nameControl
    });
  }

  validateNip(control: AbstractControl): ValidationErrors | null {
    if (!control.value) {
      return { 'incorrect': true };
    }
    const nip = control.value as string;

    const weight = [6, 5, 7, 2, 3, 4, 5, 6, 7];
    let sum = 0;
    const controlNumber = parseInt(nip.substring(9, 10));
    const weightCount = weight.length;
    for (let i = 0; i < weightCount; i++) {
      sum += (parseInt(nip.substring(i, i + 1)) * weight[i]);
    }

    return sum % 11 == controlNumber ? null : { 'incorrect': true };
  }

  validateNipOccupation(control: AbstractControl): Observable<ValidationErrors | null> {
    const isProviderExists = this.httpService.getIsProviderExists('NIP', control.value.trim());
    const providerNameFromBur = this.httpService.getProviderNameFromBur(control.value.trim());

    return forkJoin([isProviderExists, providerNameFromBur]).pipe(
      map(([exists, name]) => {
        if (!exists.exists && name.name.length > 0) {
          this.nameControl.setValue(name.name);
        }
        return exists.exists ? { 'exists': true } : null;
      }),
    catchError(() => of(null))
    );
  }

  protected onSubmit() {
    this.httpService.createNew(this.form.value as ServiceProviderCreateNewRequestInterface).subscribe({
      next: response => {
        this.showPopUp(response);
        this.router.navigate(['../' + SERVICE_PROVIDER_LIST_PATH], {
          relativeTo: this.activeRoute
        })
      }
    });
  }

  private showPopUp(response: ServiceProviderCreateNewResponseInterface) {
    const action = this.translator.instant('new-service-provider.close');
    this.snackBar.open(response.message, action, {
      horizontalPosition: "center",
      verticalPosition: "top",
      duration: 3000
    })
  }

  protected getValidationMessage(fieldName: string, control: FormControl): string {
    return this.validationMessage.getMessage(control, this.getValidationMessageKey, fieldName);
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'new-service-provider.validation.' + fieldName + '.' + validation;
  }
}
