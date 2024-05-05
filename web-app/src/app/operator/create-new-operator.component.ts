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
import {MatButton} from "@angular/material/button";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {ValidationMessageService} from "../shared/service/validation-message.service";
import {OperatorHttpService} from "./service/operator-http.service";
import {CreateNewOperatorRequestInterface} from "./model/create-new-operator-request.interface";
import {MatSnackBar} from "@angular/material/snack-bar";
import {CreateNewOperatorResponseInterface} from "./model/create-new-operator-response.interface";
import {catchError, Observable, of} from "rxjs";
import {map} from "rxjs/operators";

@Component({
  selector: 'app-create-new-operator',
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
  templateUrl: './create-new-operator.component.html',
  styleUrl: './create-new-operator.component.css'
})
export class CreateNewOperatorComponent {

  protected readonly form: FormGroup;
  protected readonly nameControl: FormControl;
  protected readonly phoneNumberControl: FormControl;

  constructor(
    private validationMessage: ValidationMessageService,
    private httpService: OperatorHttpService,
    private translator: TranslateService,
    private snackBar: MatSnackBar,
  ) {
    this.nameControl = new FormControl(null, {
      validators: [Validators.required, Validators.pattern('[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ0-9 -/.\"\\\\]{1,150}')],
      asyncValidators: [this.validateNameOccupation.bind(this)],
      updateOn: "blur"
    });
    this.phoneNumberControl = new FormControl(null, [Validators.pattern('\\d{9}')]);

    this.form = this.buildFormGroup();
  }

  private buildFormGroup(): FormGroup {
    return new FormGroup({
      'name': this.nameControl,
      'phoneNumber': this.phoneNumberControl
    });
  }

  onSubmit() {
    this.httpService.createNew(this.form.value as CreateNewOperatorRequestInterface).subscribe({
      next: response => {
        this.showPopUp(response);
/*        this.router.navigate(['../' + SERVICE_PROVIDER_LIST_PATH], {
          relativeTo: this.activeRoute
        })*/
      }
    });
  }

  validateNameOccupation(control: AbstractControl): Observable<ValidationErrors | null> {
    return this.httpService.getIsOperatorExists('NAME', control.value.trim()).pipe(
      map(response => (response.exists ? { 'exists': true } : null)),
      catchError(() => of(null))
    );
  }

  private showPopUp(response: CreateNewOperatorResponseInterface) {
    this.translator.get('common.close-button').subscribe(text => {
      this.snackBar.open(response.message, text, {
        horizontalPosition: "center",
        verticalPosition: "top",
        duration: 3000
      });
    });
  }

  protected getValidationMessage(fieldName: string, control: FormControl): string {
    return this.validationMessage.getMessage(control, this.getValidationMessageKey, fieldName);
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'new-operator.validation.' + fieldName + '.' + validation;
  }
}
