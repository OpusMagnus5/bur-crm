import {Component} from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators
} from "@angular/forms";
import {MatFormField} from "@angular/material/form-field";
import {TranslateModule} from "@ngx-translate/core";
import {MatInput} from "@angular/material/input";
import {ValidationMessageService} from "../shared/service/validation-message.service";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-new-service-provider',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatFormField,
    TranslateModule,
    MatInput,
    MatButton
  ],
  templateUrl: './new-service-provider.component.html',
  styleUrl: './new-service-provider.component.css'
})
export class NewServiceProviderComponent {

  protected readonly form: FormGroup;
  protected readonly nipControl: FormControl;
  protected readonly nameControl: FormControl;

  constructor(private validationMessage: ValidationMessageService) {
    this.nipControl = new FormControl(null, [
      Validators.required, Validators.pattern('\d{10}'), this.validateNip.bind(this)]); //TODO async validator
    this.nameControl = new FormControl(null,
      [Validators.required, Validators.pattern('[a-zA-ZążęćłóńĄŻĘĆŁÓŃ -/.\"\\\\]{1,150}')])
    this.form = this.buildFormGroup();
  }

  private buildFormGroup(): FormGroup {
    return new FormGroup({
      'nip': this.nipControl,
      'name': this.nameControl
    });
  }

  validateNip(control: AbstractControl): ValidationErrors | null {
    let nip = control.value as string;

    let weight = [6, 5, 7, 2, 3, 4, 5, 6, 7];
    let sum = 0;
    let controlNumber = parseInt(nip.substring(9, 10));
    let weightCount = weight.length;
    for (let i = 0; i < weightCount; i++) {
      sum += (parseInt(nip.substring(i, 1)) * weight[i]);
    }

    return sum % 11 === controlNumber ? null : { 'exists': true };
  }

  protected onSubmit() {
    //TODO implement me
  }

  protected getValidationMessage(fieldName: string, control: FormControl): string {
    return this.validationMessage.getMessage(control, this.getValidationMessageKey, fieldName);
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'new-service-provider.validation.' + fieldName + '.' + validation;
  }
}
