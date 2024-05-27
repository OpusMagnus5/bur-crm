import {
  Component,
  computed,
  ElementRef,
  OnDestroy,
  OnInit,
  Signal,
  signal,
  ViewChild,
  WritableSignal
} from '@angular/core';
import {FormArray, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatGridList, MatGridTile} from "@angular/material/grid-list";
import {MatError, MatFormField, MatHint, MatLabel, MatSuffix} from "@angular/material/form-field";
import {TranslateModule} from "@ngx-translate/core";
import {MatInput} from "@angular/material/input";
import {ValidationMessageService} from "../shared/service/validation-message.service";
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from "@angular/material/datepicker";
import {MatOption, provideNativeDateAdapter} from "@angular/material/core";
import {MatRadioButton, MatRadioGroup} from "@angular/material/radio";
import {ServiceTypeData} from "./service-dtos";
import {ServiceHttp} from "./service-http";
import {ServiceProviderHttpService} from "../service-provider/service/service-provider-http.service";
import {ServiceProviderDataInterface} from "../service-provider/model/service-provider-data.interface";
import {forkJoin} from "rxjs";
import {map} from "rxjs/operators";
import {SubscriptionManager} from "../shared/util/subscription-manager";
import {MatAutocomplete, MatAutocompleteSelectedEvent, MatAutocompleteTrigger} from "@angular/material/autocomplete";
import {ProgramDataInterface} from "../program/model/program-data-interface";
import {ProgramHttpService} from "../program/service/program-http.service";
import {OperatorDataInterface} from "../operator/model/operator-data.interface";
import {OperatorHttpService} from "../operator/service/operator-http.service";

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
    MatRadioButton,
    MatAutocompleteTrigger,
    MatAutocomplete,
    MatOption,
  ],
  providers: [provideNativeDateAdapter()],
  templateUrl: './create-new-service.component.html'
})
export class CreateNewServiceComponent implements OnInit, OnDestroy {

  protected readonly form: FormGroup;
  protected readonly numberControl: FormControl<string | null>;
  protected readonly nameControl: FormControl<string | null>;
  protected readonly typeControl: FormControl<string | null>;
  protected readonly startDateControl: FormControl<Date | null>;
  protected readonly endDateControl: FormControl<Date | null>;
  protected readonly numberOfParticipantsControl: FormControl<number | null>;
  protected readonly serviceProviderIdControl: FormControl<string | null>;
  protected readonly programIdControl: FormControl<ProgramDataInterface | null>;
  protected readonly customerIdControl: FormControl<string | null>;
  protected readonly coachIdsControls: FormControl<string | null>[] = [];
  protected readonly coachIdsFormArray: FormArray<FormControl>;
  protected readonly intermediaryIdControl: FormControl<string | null>;

  protected readonly serviceTypes: WritableSignal<ServiceTypeData[]> = signal([]);
  private readonly serviceProviders: WritableSignal<ServiceProviderDataInterface[]> = signal([]);
  private readonly programs: WritableSignal<ProgramDataInterface[]> = signal([]);
  private readonly operators: WritableSignal<OperatorDataInterface[]> = signal([]);

  protected readonly filteredServiceProviders: WritableSignal<ServiceProviderDataInterface[]> = signal([]);
  protected readonly filteredPrograms: WritableSignal<ProgramDataInterface[]> = signal([]);
  protected readonly filteredOperators: WritableSignal<OperatorDataInterface[]> = signal([]);

  protected readonly operatorInputDisabled: Signal<boolean> = computed(() =>
    (this.filteredOperators().length === 1 && this.filteredPrograms().length === 1) ?? this.programIdControl?.value?.id
  )
  private readonly subscriptions = new SubscriptionManager();

  @ViewChild('operatorInput') private operatorInput!: ElementRef;

  constructor(
    private validationMessage: ValidationMessageService,
    private serviceHttp: ServiceHttp,
    private serviceProviderHttp: ServiceProviderHttpService,
    private programHttp: ProgramHttpService,
    private operatorHttp: OperatorHttpService
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

  ngOnDestroy(): void {
    this.subscriptions.unsubscribeAll();
  }

  ngOnInit(): void {
    const serviceTypesRequest = this.serviceHttp.getAllServiceTypes();
    const serviceProvidersRequest = this.serviceProviderHttp.getAll();
    const programsRequest = this.programHttp.getAll();
    const operatorsRequest = this.operatorHttp.getAll();

  this.subscriptions.add(forkJoin([
    serviceTypesRequest,
    serviceProvidersRequest,
    programsRequest,
    operatorsRequest
  ]).pipe(
      map(([
        serviceTypesResponse,
        serviceProvidersResponse,
        programsResponse,
        operatorsResponse]) => {
        return {
          serviceTypes: serviceTypesResponse.serviceTypes,
          serviceProviders: serviceProvidersResponse.serviceProviders,
          programs: programsResponse.programs,
          operators: operatorsResponse.operators
        }
      })
    ).subscribe({
      next: responses => {
        this.serviceTypes.set(responses.serviceTypes);
        this.serviceProviders.set(responses.serviceProviders);
        this.filteredServiceProviders.set(responses.serviceProviders);
        this.programs.set(responses.programs);
        this.filteredPrograms.set(responses.programs);
        this.operators.set(responses.operators);
        this.filteredOperators.set(responses.operators);
      }
    }));
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

  protected onServiceProviderChange(event: Event) {
    const value: string = (<HTMLInputElement>event.target).value;
    this.filteredServiceProviders.set(
      this.serviceProviders().filter(provider => provider.name.toLowerCase().includes(value))
    );
  }

  protected onProgramChange(event: Event) {
    const value: string = (<HTMLInputElement>event.target).value;
    this.filterProgramsByName(value);
  }

  protected onOperatorChange(event: Event) {
    const name: string = (<HTMLInputElement>event.target).value;
    this.filterOperatorsByName(name);
    this.filterProgramsByOperatorName(name);
  }

  protected onProgramSelected(event: MatAutocompleteSelectedEvent) {
    const programId = (<ProgramDataInterface>event.option.value).id;
    const operatorName = (<ProgramDataInterface>event.option.value).operator.name;
    this.filterProgramsById(programId);
    this.filterOperatorsByName(operatorName);
    if (this.filteredOperators().length === 1) {
      (<HTMLInputElement>this.operatorInput.nativeElement).value = this.filteredOperators()[0].name;
    }
  }

  protected onOperatorSelected(event: MatAutocompleteSelectedEvent) {
    const id = (<OperatorDataInterface>event.option.value).id;
    const name = (<OperatorDataInterface>event.option.value).name;
    this.filterOperatorsById(id);
    this.filterProgramsByOperatorName(name);
  }

  private filterProgramsByName(name: string) {
    this.filteredPrograms.set(
      this.programs().filter(program => program.name.toLowerCase().includes(name.toLowerCase().trim()))
    );
  }

  private filterProgramsById(id: string) {
    this.filteredPrograms.set(
      this.programs().filter(program => program.id === id)
    );
  }

  private filterProgramsByOperatorName(name: string) {
    this.filteredPrograms.set(
      this.programs().filter(program => program.operator.name.toLowerCase().includes(name.toLowerCase().trim()))
    );
  }

  private filterOperatorsByName(name: string) {
    this.filteredOperators.set(
      this.operators().filter(operator => operator.name.toLowerCase().includes(name.toLowerCase().trim()))
    );
  }

  private filterOperatorsById(id: string) {
    this.filteredOperators.set(
      this.operators().filter(operator => operator.id === id)
    );
  }

  protected onSubmit() {

  }

  protected getValidationMessage(fieldName: string, control: FormControl): string {
    return this.validationMessage.getMessage(control, this.getValidationMessageKey, fieldName);
  }

  protected displayServiceProviderName(serviceProvider: ServiceProviderDataInterface | string): string {
    return typeof serviceProvider === 'string' ? serviceProvider : serviceProvider?.name;
  }

  protected displayProgramName(program: ProgramDataInterface | string): string {
    return typeof program === 'string' ? program : program?.name;
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'new-service.validation.' + fieldName + '.' + validation;
  }
}
