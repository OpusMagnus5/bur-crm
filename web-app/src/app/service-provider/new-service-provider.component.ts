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
import {TranslateModule} from "@ngx-translate/core";
import {MatInput} from "@angular/material/input";
import {ValidationMessageService} from "../shared/service/validation-message.service";
import {MatButton} from "@angular/material/button";
import {ServiceProviderHttpService} from "./service/service-provider-http.service";
import {Observable} from "rxjs";
import {ServiceProviderCreateNewRequestInterface} from "./model/service-provider-create-new-request.interface";
import {ServiceProviderCreateNewResponseInterface} from "./model/service-provider-create-new-response.interface";
import {ActivatedRoute, Router} from "@angular/router";
import {SERVICE_PROVIDER_LIST_PATH} from "../app.routes";
import {ServiceProviderService} from "./service/service-provider.service";
import {SnackbarService} from "../shared/service/snackbar.service";
import {NipUtils} from "../shared/util/nip-utils";

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
  providers: [
    ServiceProviderService
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
    private router: Router,
    private activeRoute: ActivatedRoute,
    private service: ServiceProviderService,
    private snackbar: SnackbarService
  ) {
    this.nipControl = new FormControl(null, {
     validators: [Validators.required, Validators.pattern('\\d{10}'), NipUtils.validateNip.bind(this)],
     asyncValidators: [this.validateNipOccupation.bind(this)],
     updateOn: 'blur'
    });
    this.nameControl = new FormControl(null,
      [Validators.required, Validators.pattern('[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ0-9 -/.\"\\\\]{1,150}')])
    this.form = this.buildFormGroup();
  }

  private buildFormGroup(): FormGroup {
    return new FormGroup({
      'nip': this.nipControl,
      'name': this.nameControl
    });
  }

  validateNipOccupation(control: AbstractControl): Observable<ValidationErrors | null> {
    return this.service.validateNipOccupationAndGetProviderName(control, this.nameControl);
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
    this.snackbar.openTopCenterSnackbar(response.message);
  }

  protected getValidationMessage(fieldName: string, control: FormControl): string {
    return this.validationMessage.getMessage(control, this.getValidationMessageKey, fieldName);
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'new-service-provider.validation.' + fieldName + '.' + validation;
  }
}
