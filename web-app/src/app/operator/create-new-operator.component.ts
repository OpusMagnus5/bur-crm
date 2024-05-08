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
import {TranslateModule} from "@ngx-translate/core";
import {ValidationMessageService} from "../shared/service/validation-message.service";
import {OperatorHttpService} from "./service/operator-http.service";
import {CreateNewOperatorRequestInterface} from "./model/create-new-operator-request.interface";
import {CreateNewOperatorResponseInterface} from "./model/create-new-operator-response.interface";
import {catchError, Observable, of} from "rxjs";
import {map} from "rxjs/operators";
import {ActivatedRoute, Router} from "@angular/router";
import {OPERATOR_LIST_PATH} from "../app.routes";
import {SnackbarService} from "../shared/service/snackbar.service";

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
  protected readonly notesControl: FormControl;

  constructor(
    private validationMessage: ValidationMessageService,
    private httpService: OperatorHttpService,
    private activeRoute: ActivatedRoute,
    private router: Router,
    private snackbar: SnackbarService
  ) {
    this.nameControl = new FormControl(null, {
      validators: [Validators.required, Validators.pattern('[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ0-9 -/.\"\\\\]{1,150}')],
      asyncValidators: [this.validateNameOccupation.bind(this)],
      updateOn: "blur"
    });
    this.notesControl = new FormControl(null);

    this.form = this.buildFormGroup();
  }

  private buildFormGroup(): FormGroup {
    return new FormGroup({
      'name': this.nameControl,
      'notes': this.notesControl
    });
  }

  onSubmit() {
    this.httpService.createNew(this.form.value as CreateNewOperatorRequestInterface).subscribe({
      next: response => {
        this.showPopUp(response);
        this.router.navigate(['../' + OPERATOR_LIST_PATH], {
          relativeTo: this.activeRoute
        });
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
    this.snackbar.openTopCenterSnackbar(response.message);
  }

  protected getValidationMessage(fieldName: string, control: FormControl): string {
    return this.validationMessage.getMessage(control, this.getValidationMessageKey, fieldName);
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'new-operator.validation.' + fieldName + '.' + validation;
  }
}
