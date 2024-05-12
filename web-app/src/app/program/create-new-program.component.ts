import {Component, OnInit, signal, WritableSignal} from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  ValidationErrors,
  Validators
} from "@angular/forms";
import {ValidationMessageService} from "../shared/service/validation-message.service";
import {catchError, Observable, of} from "rxjs";
import {map} from "rxjs/operators";
import {ProgramHttpService} from "./service/program-http.service";
import {CreateNewProgramResponseInterface} from "./model/create-new-program-response-interface";
import {PROGRAM_LIST_PATH} from "../app.routes";
import {CreateNewProgramRequestInterface} from "./model/create-new-program-request.interface";
import {ActivatedRoute, Router} from "@angular/router";
import {OperatorDataInterface} from "../operator/model/operator-data.interface";
import {OperatorHttpService} from "../operator/service/operator-http.service";
import {SnackbarService} from "../shared/service/snackbar.service";
import {MatButton} from "@angular/material/button";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {TranslateModule} from "@ngx-translate/core";
import {MatAutocomplete, MatAutocompleteTrigger, MatOption} from "@angular/material/autocomplete";

@Component({
  selector: 'app-create-new-program',
  standalone: true,
  imports: [
    MatButton,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
    TranslateModule,
    MatAutocompleteTrigger,
    MatAutocomplete,
    MatOption
  ],
  templateUrl: './create-new-program.component.html',
})
export class CreateNewProgramComponent implements OnInit{

  protected readonly form: FormGroup;
  protected readonly nameControl: FormControl;
  protected readonly operatorControl: FormControl;
  private operators: OperatorDataInterface[] | undefined;
  protected filteredOperators: WritableSignal<OperatorDataInterface[]> = signal([]);

  constructor(
    private validationMessage: ValidationMessageService,
    private programHttp: ProgramHttpService,
    private operatorHttp: OperatorHttpService,
    private router: Router,
    private activeRoute: ActivatedRoute,
    private snackbar: SnackbarService
  ) {
    this.nameControl = new FormControl(null, {
      validators: [Validators.required, Validators.pattern('[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ0-9 -/.\"\\\\]{1,150}')],
      asyncValidators: [this.validateNameOccupation.bind(this)],
      updateOn: "blur"
    });
    this.operatorControl = new FormControl(null, [Validators.required]);
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

  private getAllOperators() {
    this.operatorHttp.getAll().subscribe({
      next: response => {
        this.operators = response.operators;
        this.filteredOperators = signal(response.operators);
      }
    })
  }

  private buildFormGroup(): FormGroup {
    return new FormGroup({
      'name': this.nameControl,
      'operator': this.operatorControl
    });
  }

  protected onSubmit() {
    const request: CreateNewProgramRequestInterface = {
      operatorId: (this.operatorControl.value as OperatorDataInterface).id,
      name: this.nameControl.value
    };
    this.programHttp.createNew(request).subscribe({
      next: response => {
        this.showPopUp(response);
        this.router.navigate(['../' + PROGRAM_LIST_PATH], {
          relativeTo: this.activeRoute
        });
      }
    });
  }

  protected validateNameOccupation(control: AbstractControl): Observable<ValidationErrors | null> {
    return this.programHttp.getIsOperatorExists('NAME', control.value.trim()).pipe(
      map(response => (response.exists ? { 'exists': true } : null)),
      catchError(() => of(null))
    );
  }

  private showPopUp(response: CreateNewProgramResponseInterface) {
    this.snackbar.openTopCenterSnackbar(response.message);
  }

  protected getValidationMessage(fieldName: string, control: FormControl): string {
    return this.validationMessage.getMessage(control, this.getValidationMessageKey, fieldName);
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'new-program.validation.' + fieldName + '.' + validation;
  }

  protected displayOperatorName(value: OperatorDataInterface): string {
    return value ? value.name : '';
  }
}
