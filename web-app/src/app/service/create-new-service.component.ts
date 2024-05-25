import {Component, OnInit, signal, WritableSignal} from '@angular/core';
import {FormArray, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatGridList, MatGridTile} from "@angular/material/grid-list";
import {MatError, MatFormField, MatHint, MatLabel, MatSuffix} from "@angular/material/form-field";
import {TranslateModule} from "@ngx-translate/core";
import {MatInput} from "@angular/material/input";
import {ValidationMessageService} from "../shared/service/validation-message.service";
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from "@angular/material/datepicker";
import {provideNativeDateAdapter} from "@angular/material/core";
import {MatRadioButton, MatRadioGroup} from "@angular/material/radio";
import {ServiceTypeData} from "./service-dtos";
import {ServiceHttp} from "./service-http";

@Component({
  selector: 'app-create-new-service',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatGridList,
    MatGridTile,
    MatFormField,
    TranslateModule,
    MatInput,
    MatDatepickerInput,
    MatDatepickerToggle,
    MatSuffix,
    MatDatepicker,
    MatLabel,
    MatError,
    MatHint,
    MatRadioGroup,
    MatRadioButton
  ],
  providers: [provideNativeDateAdapter()],
  templateUrl: './create-new-service.component.html'
})
export class CreateNewServiceComponent implements OnInit {

  protected readonly form: FormGroup;
  protected readonly numberControl: FormControl<string | null>;
  protected readonly nameControl: FormControl<string | null>;
  protected readonly typeControl: FormControl<string | null>;
  protected readonly startDateControl: FormControl<Date | null>;
  protected readonly endDateControl: FormControl<Date | null>;
  protected readonly numberOfParticipantsControl: FormControl<number | null>;
  protected readonly serviceProviderIdControl: FormControl<string | null>;
  protected readonly programIdControl: FormControl<string | null>;
  protected readonly customerIdControl: FormControl<string | null>;
  protected readonly coachIdsControls: FormControl<string | null>[] = [];
  protected readonly coachIdsFormArray: FormArray<FormControl>;
  protected readonly intermediaryIdControl: FormControl<string | null>;

  protected readonly serviceTypes: WritableSignal<ServiceTypeData[]> = signal([]);

  constructor(
    private validationMessage: ValidationMessageService,
    private serviceHttp: ServiceHttp
  ) {
    this.numberControl = new FormControl(null, {
      validators: [Validators.required, Validators.pattern('\\d{4}/\\d{2}/\\d{2}/\\d+/\\d+')]
    });
    this.nameControl = new FormControl(null, {
      validators: [Validators.required, Validators.pattern('[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ0-9 -/.\"\\\\]{1,300}')]
    });
    this.typeControl = new FormControl(null, {
      validators: [Validators.required]
    });
    this.startDateControl = new FormControl(null, {
      validators: [Validators.required]
    });
    this.endDateControl = new FormControl(null, {
      validators: [Validators.required]
    });
    this.numberOfParticipantsControl = new FormControl(null, {
      validators: [Validators.required, Validators.min(1), Validators.max(Number.MAX_SAFE_INTEGER)]
    });
    this.serviceProviderIdControl = new FormControl(null, {
      validators: [Validators.required]
    });
    this.programIdControl = new FormControl(null, {
      validators: [Validators.required]
    });
    this.customerIdControl = new FormControl(null, {
      validators: [Validators.required]
    });
    this.intermediaryIdControl = new FormControl(null, {
      validators: [Validators.required]
    });
    this.addCoachIdControl();
    this.coachIdsFormArray = new FormArray(this.coachIdsControls);
    this.form = this.buildFormGroup();
  }

  ngOnInit(): void {
    this.serviceHttp.getAllServiceTypes().subscribe(response => {
      this.serviceTypes.set(response.serviceTypes);
    });
  }

  private addCoachIdControl() {
    this.coachIdsControls.push(new FormControl<string | null>(null, {
      validators: [Validators.required]
    }));
  }

  private buildFormGroup() {
    return new FormGroup({
      'number': this.numberControl,
      'name': this.nameControl,
      'type': this.typeControl,
      'startDate': this.startDateControl,
      'endDate': this.endDateControl,
      'numberOfParticipants': this.numberOfParticipantsControl,
      'serviceProviderId': this.serviceProviderIdControl,
      'programId': this.programIdControl,
      'customerId': this.customerIdControl,
      'coachIds': this.coachIdsFormArray,
      'intermediaryId': this.intermediaryIdControl
    });
  }

  protected onSubmit() {

  }

  protected getValidationMessage(fieldName: string, control: FormControl): string {
    return this.validationMessage.getMessage(control, this.getValidationMessageKey, fieldName);
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'new-service.validation.' + fieldName + '.' + validation;
  }
}
