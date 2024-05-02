import {Component, Inject} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle
} from "@angular/material/dialog";
import {ServiceProviderDetailsResponseInterface} from "./model/service-provider-details-response.interface";
import {MatButton} from "@angular/material/button";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators
} from "@angular/forms";
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {Observable, Subject} from "rxjs";
import {ServiceProviderService} from "./service/service-provider.service";
import {ValidationMessageService} from "../shared/service/validation-message.service";
import {ServiceProviderHttpService} from "./service/service-provider-http.service";
import {UpdateServiceProviderRequestInterface} from "./model/update-service-provider-request.interface";
import {MatSnackBar} from "@angular/material/snack-bar";
import {UpdateServiceProviderResponseInterface} from "./model/update-service-provider-response.interface";

@Component({
  selector: 'app-update-service-provider',
  standalone: true,
  imports: [
    MatButton,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
    TranslateModule,
    MatDialogContent,
    MatDialogTitle,
    MatDialogActions,
    MatDialogClose
  ],
  providers: [
    ServiceProviderService
  ],
  templateUrl: './update-service-provider.component.html',
  styleUrl: './update-service-provider.component.css'
})
export class UpdateServiceProviderComponent {

  updateConfirmation: Subject<boolean> = new Subject();

  protected readonly form: FormGroup;
  protected readonly nipControl: FormControl;
  protected readonly nameControl: FormControl;

  constructor(
    @Inject(MAT_DIALOG_DATA) protected data: ServiceProviderDetailsResponseInterface,
    private service: ServiceProviderService,
    private validationMessage: ValidationMessageService,
    private httpService: ServiceProviderHttpService,
    private translator: TranslateService,
    private snackBar: MatSnackBar,
  ) {
    this.nipControl = new FormControl(data.nip, { //TODO trzy żadania z walidacją na starcie
      // TODO nie walidowac nipu czy istnieje bo mozemy chciez zmienic tylko nazwę, nip walidowac dopiero po zmianie
      // TODO sprawdzic komunikat optimistic locking
      // TODO nazwa firmy powinno dopuszczac cyfry
      validators: [Validators.required, Validators.pattern('\\d{10}'), this.service.validateNip.bind(this)],
      asyncValidators: [this.validateNipOccupation.bind(this)],
      updateOn: 'blur'
    });
    this.nameControl = new FormControl(data.name,
      [Validators.required, Validators.pattern('[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ -/.\"\\\\]{1,150}')])
    this.form = this.buildFormGroup();
  }

  private buildFormGroup(): FormGroup {
    return new FormGroup({
      'nip': this.nipControl,
      'name': this.nameControl
    });
  }

  protected validateNipOccupation(control: AbstractControl): Observable<ValidationErrors | null> {
    return this.service.validateNipOccupation(control, this.nameControl);
  }

  protected getValidationMessage(fieldName: string, control: FormControl): string {
    return this.validationMessage.getMessage(control, this.getValidationMessageKey, fieldName);
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'update-service-provider.validation.' + fieldName + '.' + validation;
  }

  protected onSubmit() {
    this.httpService.update(this.mapFormToRequest()).subscribe({
      next: response => {
        this.showPopUp(response);
        this.updateConfirmation.next(true);
      }
    })
  }

  private mapFormToRequest(): UpdateServiceProviderRequestInterface {
    return {
      id: this.data.id,
      version: this.data.version,
      name: this.nameControl.value,
      nip: this.nipControl.value
    }
  }

  private showPopUp(response: UpdateServiceProviderResponseInterface) {
    const action = this.translator.instant('common.close-button');
    this.snackBar.open(response.message, action, {
      horizontalPosition: "center",
      verticalPosition: "top",
      duration: 3000
    })
  }
}
