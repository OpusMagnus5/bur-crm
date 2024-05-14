import {Component, Inject, OnInit, signal, WritableSignal} from '@angular/core';
import {catchError, Observable, of, Subject} from "rxjs";
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
import {ProgramDetailsResponseInterface} from "./model/program-details-response.interface";
import {ProgramHttpService} from "./service/program-http.service";
import {map} from "rxjs/operators";
import {OperatorHttpService} from "../operator/service/operator-http.service";
import {OperatorDataInterface} from "../operator/model/operator-data.interface";
import {ValidationMessageService} from "../shared/service/validation-message.service";
import {UpdateProgramRequestInterface} from "./model/update-program-request.interface";
import {SnackbarService} from "../shared/service/snackbar.service";
import {MatAutocomplete, MatAutocompleteTrigger, MatOption} from "@angular/material/autocomplete";
import {MatButton} from "@angular/material/button";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-update-program',
  standalone: true,
  imports: [
    FormsModule,
    MatAutocomplete,
    MatAutocompleteTrigger,
    MatButton,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    MatOption,
    TranslateModule,
    ReactiveFormsModule,
    MatDialogContent,
    MatDialogActions,
    MatDialogClose,
    MatDialogTitle
  ],
  templateUrl: './update-program.component.html'
})
export class UpdateProgramComponent implements OnInit {

  updateConfirmation: Subject<boolean> = new Subject();

  protected readonly form: FormGroup;
  protected readonly nameControl: FormControl;
  protected readonly operatorControl: FormControl;
  private operators: OperatorDataInterface[] | undefined;
  protected filteredOperators: WritableSignal<OperatorDataInterface[]> = signal([]);

  constructor(
    @Inject(MAT_DIALOG_DATA) protected data: ProgramDetailsResponseInterface,
    private programHttp: ProgramHttpService,
    private operatorHttp: OperatorHttpService,
    private validationMessage: ValidationMessageService,
    private snackbar: SnackbarService
  ) {
    this.nameControl = new FormControl(data.name, {
      validators: [Validators.required, Validators.pattern('[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ0-9 -/.\"\\\\]{1,150}')],
      asyncValidators: [this.validateNameOccupation.bind(this)],
      updateOn: "blur"
    });
    this.operatorControl = new FormControl(data.operator, [Validators.required]);
    this.form = this.buildFormGroup();
    this.getAllOperators();
  }

  ngOnInit(): void {
    this.operatorControl.valueChanges.pipe(
      map(value => this.filterOperator(value))
    ).subscribe(value => {
      this.filteredOperators?.set(value);
    })
  }

  private filterOperator(value: any): OperatorDataInterface[]{
    let filterValue: string;
    try {
      filterValue = value.toLowerCase();
    } catch (error) {
      filterValue = value.name.toLowerCase();
    }
    const filteredValues = this.operators?.filter(operator => operator.name.toLowerCase().includes(filterValue));
    return filteredValues ? filteredValues : [];
  }

  private buildFormGroup(): FormGroup {
    return new FormGroup({
      'name': this.nameControl,
      'operator': this.operatorControl
    });
  }

  protected validateNameOccupation(control: AbstractControl): Observable<ValidationErrors | null> {
    if (control.value.trim() !== this.data.name) {
      return this.programHttp.getIsOperatorExists('NAME', control.value.trim()).pipe(
        map(response => (response.exists ? { 'exists': true } : null)),
        catchError(() => of(null))
      );
    }
    return of(null);
  }

  protected onSubmit() {
    console.log(this.operatorControl.value);
    this.programHttp.update(this.mapFormToRequest()).subscribe({
      next: response => {
        this.snackbar.openTopCenterSnackbar(response.message);
        this.updateConfirmation.next(true);
      }
    })
  }

  private mapFormToRequest(): UpdateProgramRequestInterface {
    return {
      id: this.data.id,
      version: this.data.version,
      name: this.nameControl.value,
      operatorId: (this.operatorControl.value as OperatorDataInterface).id
    }
  }

  private getAllOperators() {
    this.operatorHttp.getAll().subscribe({
      next: response => {
        this.operators = response.operators;
        this.filteredOperators = signal(response.operators);
      }
    })
  }

  protected getValidationMessage(fieldName: string, control: FormControl): string {
    return this.validationMessage.getMessage(control, this.getValidationMessageKey, fieldName);
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'update-program.validation.' + fieldName + '.' + validation;
  }

  protected displayOperatorName(value: OperatorDataInterface): string {
    return value ? value.name : '';
  }
}
