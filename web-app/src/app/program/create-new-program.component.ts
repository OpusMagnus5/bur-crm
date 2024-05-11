import {Component} from '@angular/core';
import {AbstractControl, FormControl, FormGroup, ValidationErrors, Validators} from "@angular/forms";
import {ValidationMessageService} from "../shared/service/validation-message.service";
import {catchError, Observable, of} from "rxjs";
import {map} from "rxjs/operators";
import {ProgramHttpService} from "./service/program-http.service";
import {CreateNewProgramResponseInterface} from "./model/create-new-program-response-interface";
import {PROGRAM_LIST_PATH} from "../app.routes";
import {CreateNewProgramRequestInterface} from "./model/create-new-program-request.interface";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-create-new-program',
  standalone: true,
  imports: [],
  templateUrl: './create-new-program.component.html',
})
export class CreateNewProgramComponent {

  protected readonly form: FormGroup;
  protected readonly nameControl: FormControl;
  protected readonly operatorControl: FormControl;

  constructor(
    private validationMessage: ValidationMessageService,
    private httpService: ProgramHttpService,
    private router: Router,
    private activeRoute: ActivatedRoute,
  ) {
    this.nameControl = new FormControl(null, {
      validators: [Validators.required, Validators.pattern('[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ0-9 -/.\"\\\\]{1,150}')],
      asyncValidators: [this.validateNameOccupation.bind(this)],
      updateOn: "blur"
    });
    this.operatorControl = new FormControl(null, [Validators.required]);
    this.form = this.buildFormGroup();
  }

  private buildFormGroup(): FormGroup {
    return new FormGroup({
      'name': this.nameControl,
      'operator': this.operatorControl
    });
  }

  protected onSubmit() {
    this.httpService.createNew(this.form.value as CreateNewProgramRequestInterface).subscribe({
      next: response => {
        this.showPopUp(response);
        this.router.navigate(['../' + PROGRAM_LIST_PATH], {
          relativeTo: this.activeRoute
        });
      }
    });
  }

  protected validateNameOccupation(control: AbstractControl): Observable<ValidationErrors | null> {
    return this.httpService.getIsOperatorExists('NAME', control.value.trim()).pipe(
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
}
