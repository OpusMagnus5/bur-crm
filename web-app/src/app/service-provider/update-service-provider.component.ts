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
import {TranslateModule} from "@ngx-translate/core";
import {Observable, of, Subject} from "rxjs";
import {ServiceProviderService} from "./service/service-provider.service";
import {ValidationMessageService} from "../shared/service/validation-message.service";
import {ServiceProviderHttpService} from "./service/service-provider-http.service";
import {UpdateServiceProviderRequestInterface} from "./model/update-service-provider-request.interface";
import {SnackbarService} from "../shared/service/snackbar.service";
import {OnSubmitInterface} from "../shared/model/on-submit.interface";
import {NipUtils} from "../shared/util/nip-utils";

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
export class UpdateServiceProviderComponent implements OnSubmitInterface {

  updateConfirmation: Subject<boolean> = new Subject();

  protected readonly form: FormGroup;
  protected readonly nipControl: FormControl;
  protected readonly nameControl: FormControl;

  constructor(
    @Inject(MAT_DIALOG_DATA) protected data: ServiceProviderDetailsResponseInterface,
    private service: ServiceProviderService,
    private validationMessage: ValidationMessageService,
    private httpService: ServiceProviderHttpService,
    private snackbar: SnackbarService
  ) {
    this.nipControl = new FormControl(data.nip, {
      validators: [Validators.required, Validators.pattern('\\d{10}'), NipUtils.validateNip.bind(this)],
      asyncValidators: [this.validateNipOccupationAndGetProviderName.bind(this)],
      updateOn: 'blur'
    });
    this.nameControl = new FormControl(data.name,
      [Validators.required, Validators.pattern('[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ0-9 -/.\"\\\\]{1,150}')])
    this.form = this.buildFormGroup();
  }

  private buildFormGroup(): FormGroup {
    return new FormGroup({
      'nip': this.nipControl,
      'name': this.nameControl
    });
  }

  protected validateNipOccupationAndGetProviderName(control: AbstractControl): Observable<ValidationErrors | null> {
    if (control.value !== this.data.nip) {
      return this.service.validateNipOccupationAndGetProviderName(control, this.nameControl);
    }
    return of(null);
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
        this.snackbar.openTopCenterSnackbar(response.message);
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
}
