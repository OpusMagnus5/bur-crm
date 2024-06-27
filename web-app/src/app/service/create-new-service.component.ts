import {Component, ElementRef, OnDestroy, OnInit, signal, ViewChild, WritableSignal} from '@angular/core';
import {
  AbstractControl,
  FormControl,
  FormGroup,
  FormGroupDirective,
  ReactiveFormsModule,
  ValidationErrors,
  Validators
} from "@angular/forms";
import {MatGridList, MatGridTile} from "@angular/material/grid-list";
import {MatError, MatFormField, MatHint, MatLabel, MatSuffix} from "@angular/material/form-field";
import {TranslateModule} from "@ngx-translate/core";
import {MatInput} from "@angular/material/input";
import {ValidationMessageService} from "../shared/service/validation-message.service";
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from "@angular/material/datepicker";
import {MatOption} from "@angular/material/core";
import {MatRadioButton, MatRadioGroup} from "@angular/material/radio";
import {
  CreateNewServiceResponse,
  CreateOrUpdateServiceRequest,
  ServiceStatusData,
  ServiceTypeData
} from "./service-dtos";
import {ServiceHttp} from "./service-http";
import {ServiceProviderHttpService} from "../service-provider/service/service-provider-http.service";
import {ServiceProviderDataInterface} from "../service-provider/model/service-provider-data.interface";
import {forkJoin, merge} from "rxjs";
import {map} from "rxjs/operators";
import {SubscriptionManager} from "../shared/util/subscription-manager";
import {MatAutocomplete, MatAutocompleteSelectedEvent, MatAutocompleteTrigger} from "@angular/material/autocomplete";
import {ProgramDataInterface} from "../program/model/program-data-interface";
import {ProgramHttpService} from "../program/service/program-http.service";
import {OperatorDataInterface} from "../operator/model/operator-data.interface";
import {OperatorHttpService} from "../operator/service/operator-http.service";
import {CustomerData} from "../customer/customer-dtos";
import {CoachData} from "../coach/coach-dtos";
import {IntermediaryData} from "../intermediary/intermediary-dtos";
import {CustomerHttpService} from "../customer/customer-http.service";
import {IntermediaryHttpService} from "../intermediary/intermediary-http.service";
import {MatChipGrid, MatChipInput, MatChipRemove, MatChipRow} from "@angular/material/chips";
import {CoachHttpService} from "../coach/coach-http.service";
import {MatIcon} from "@angular/material/icon";
import {MatButton} from "@angular/material/button";
import {SnackbarService} from "../shared/service/snackbar.service";
import {CustomDateAdapterService} from "../shared/service/custom-date-adapter.service";
import {toObservable} from "@angular/core/rxjs-interop";
import {ActivatedRoute, Router} from "@angular/router";
import {SERVICES_LIST_PATH} from "../app.routes";

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
    MatChipGrid,
    MatChipRow,
    MatChipRemove,
    MatIcon,
    MatChipInput,
    MatButton,
  ],
  templateUrl: './create-new-service.component.html'
})
export class CreateNewServiceComponent implements OnInit, OnDestroy {

  protected readonly form: FormGroup;
  numberControl: FormControl<string | null>;
  nameControl: FormControl<string | null>;
  typeControl: FormControl<string | null>;
  startDateControl: FormControl<Date | null>;
  endDateControl: FormControl<Date | null>;
  numberOfParticipantsControl: FormControl<number | null>;
  serviceProviderIdControl: FormControl<ServiceProviderDataInterface | string | null>;
  programIdControl: FormControl<ProgramDataInterface | string | null>;
  operatorControl: FormControl<OperatorDataInterface | string | null>;
  customerIdControl: FormControl<CustomerData | string | null>;
  coachIdsControl: FormControl<CoachData[] | string[] | null>;
  intermediaryIdControl: FormControl<IntermediaryData | string | null>;
  statusControl: FormControl<ServiceStatusData | string | null>

  protected readonly serviceTypes: WritableSignal<ServiceTypeData[]> = signal([]);
  private readonly serviceProviders: WritableSignal<ServiceProviderDataInterface[]> = signal([]);
  private readonly programs: WritableSignal<ProgramDataInterface[]> = signal([]);
  private readonly operators: WritableSignal<OperatorDataInterface[]> = signal([]);
  private readonly customers: WritableSignal<CustomerData[]> = signal([]);
  private readonly intermediaries: WritableSignal<IntermediaryData[]> = signal([]);
  private readonly coaches: WritableSignal<CoachData[]> = signal([]);
  private readonly statuses: WritableSignal<ServiceStatusData[]> = signal([]);

  protected readonly filteredServiceProviders: WritableSignal<ServiceProviderDataInterface[]> = signal([]);
  protected readonly filteredPrograms: WritableSignal<ProgramDataInterface[]> = signal([]);
  protected readonly filteredOperators: WritableSignal<OperatorDataInterface[]> = signal([]);
  protected readonly filteredCustomers: WritableSignal<CustomerData[]> = signal([]);
  protected readonly filteredIntermediaries: WritableSignal<IntermediaryData[]> = signal([]);
  protected readonly filteredCoaches: WritableSignal<CoachData[]> = signal([]);
  protected readonly filteredStatuses: WritableSignal<ServiceStatusData[]> = signal([]);

  private readonly subscriptions = new SubscriptionManager();
  serviceVersion: number | null = null;
  private readonly serviceId: string | null;

  @ViewChild('coachInput') private coachInput!: ElementRef;

  constructor(
    private validationMessage: ValidationMessageService,
    private serviceHttp: ServiceHttp,
    private serviceProviderHttp: ServiceProviderHttpService,
    private programHttp: ProgramHttpService,
    private operatorHttp: OperatorHttpService,
    private customerHttp: CustomerHttpService,
    private intermediaryHttp: IntermediaryHttpService,
    private coachHttp: CoachHttpService,
    private snackbar: SnackbarService,
    private dateAdapter: CustomDateAdapterService,
    private route: ActivatedRoute,
    private router: Router
  ) {
    this.serviceId = this.route.snapshot.paramMap.get('id');
    this.numberControl = new FormControl(null, {
      validators: [Validators.required, Validators.pattern('\\d{4}/\\d{2}/\\d{2}/\\d+/\\d+')]
    });
    this.nameControl = new FormControl(null, {
      validators: [Validators.required, Validators.pattern('[a-zA-ZążęćłóńśĄŻĘĆŁÓŃŚ0-9: -/.\"\\\\]{1,300}')]
    });
    this.typeControl = new FormControl(null, {
      validators: [Validators.required]
    });
    this.startDateControl = new FormControl(null, {
      validators: [Validators.required, this.isNotLaterThanEndDate.bind(this)]
    });
    this.endDateControl = new FormControl(null, {
      validators: [Validators.required, this.isNotLaterThanEndDate.bind(this)]
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
    this.operatorControl = new FormControl(null);
    this.customerIdControl = new FormControl(null, {
      validators: [Validators.required]
    });
    this.intermediaryIdControl = new FormControl(null);
    this.coachIdsControl = new FormControl([], {
      validators: [Validators.required, Validators.minLength(2)]
    })
    this.statusControl = new FormControl(null, {
      validators: [Validators.required]
    })
    this.form = this.buildFormGroup();

    merge(toObservable(this.filteredOperators), toObservable(this.filteredPrograms)).subscribe(() => {
      const disabled = this.isSingleFilteredOperator() && this.isSingleFilteredProgram() && this.isValidProgramControl();
      if (disabled) {
        this.operatorControl.disable();
      } else {
        this.operatorControl.enable();
      }
    })
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribeAll();
  }

  ngOnInit(): void {
    const serviceTypesRequest = this.serviceHttp.getAllServiceTypes();
    const serviceProvidersRequest = this.serviceProviderHttp.getAll();
    const programsRequest = this.programHttp.getAll();
    const operatorsRequest = this.operatorHttp.getAll();
    const customersRequest = this.customerHttp.getAll();
    const intermediariesRequest = this.intermediaryHttp.getAll();
    const coachesRequest = this.coachHttp.getAll();
    const statusesRequest = this.serviceHttp.getAllStatuses();

  this.subscriptions.add(forkJoin([
    serviceTypesRequest,
    serviceProvidersRequest,
    programsRequest,
    operatorsRequest,
    customersRequest,
    intermediariesRequest,
    coachesRequest,
    statusesRequest
  ]).pipe(
      map(([
        serviceTypesResponse,
        serviceProvidersResponse,
        programsResponse,
        operatorsResponse,
        customersResponse,
        intermediariesResponse,
        coachesResponse,
        statusesResponse]) => {
        return {
          serviceTypes: serviceTypesResponse.serviceTypes,
          serviceProviders: serviceProvidersResponse.serviceProviders,
          programs: programsResponse.programs,
          operators: operatorsResponse.operators,
          customers: customersResponse.customers,
          intermediaries: intermediariesResponse.intermediaries,
          coaches: coachesResponse.coaches,
          statuses: statusesResponse.statuses
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
        this.customers.set(responses.customers);
        this.filteredCustomers.set(responses.customers);
        this.intermediaries.set(responses.intermediaries);
        this.filteredIntermediaries.set(responses.intermediaries);
        this.coaches.set(responses.coaches);
        this.filteredCoaches.set(responses.coaches);
        this.statuses.set(responses.statuses);
        this.filteredStatuses.set(responses.statuses);
      }
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
      'coachIds': this.coachIdsControl,
      'intermediaryId': this.intermediaryIdControl,
      'status': this.statusControl
    });
  }

  get coachIdsArray(): CoachData[] {
    return this.coachIdsControl.value as CoachData[];
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

  protected onCustomerChange(event: Event) {
    const name: string = (<HTMLInputElement>event.target).value;
    this.filterCustomersByName(name);
  }

  protected onIntermediaryChange(event: Event) {
    const name: string = (<HTMLInputElement>event.target).value;
    this.filterIntermediariesByName(name);
  }

  protected onCoachChange(event: Event) {
    const name: string = (<HTMLInputElement>event.target).value;
    this.filterCoachesByName(name);
  }

  protected onStatusChange(event: Event) {
    const value: string = (<HTMLInputElement>event.target).value;
    this.filterStatusesByValue(value);
  }

  protected onProgramSelected(event: MatAutocompleteSelectedEvent) {
    const programId = (<ProgramDataInterface>event.option.value).id;
    const operatorName = (<ProgramDataInterface>event.option.value).operator.name;
    this.filterProgramsById(programId);
    this.filterOperatorsByName(operatorName);
    if (this.isSingleFilteredOperator()) {
      this.operatorControl.setValue(this.filteredOperators()[0]);
    }
  }

  onOperatorSelected(event: MatAutocompleteSelectedEvent) {
    const id = (<OperatorDataInterface>event.option.value).id;
    const name = (<OperatorDataInterface>event.option.value).name;
    this.filterOperatorsById(id);
    this.filterProgramsByOperatorName(name);
  }

  protected onCustomerSelected(event: MatAutocompleteSelectedEvent) {
    const id = (<OperatorDataInterface>event.option.value).id;
    this.filterCustomersById(id);
  }

  protected onIntermediarySelected(event: MatAutocompleteSelectedEvent) {
    const id = (<IntermediaryData>event.option.value).id;
    this.filterIntermediariesById(id);
  }

  protected onCoachSelected(event: MatAutocompleteSelectedEvent) {
    const coach = (<CoachData>event.option.value);
    (<CoachData[]>this.coachIdsControl.value).push(coach);
    (<HTMLInputElement>this.coachInput.nativeElement).value = '';
    this.coaches.set(this.coaches().filter(item => item.id !== coach.id))
    this.filterCoachesByName('');
    this.coachIdsControl.updateValueAndValidity({ onlySelf: true, emitEvent: true });
  }

  protected onStatusSelected(event: MatAutocompleteSelectedEvent) {
    const value = (<ServiceStatusData>event.option.value).value;
    this.filterStatusesByValue(value);
  }

  private isNotLaterThanEndDate(control: AbstractControl): ValidationErrors | null {
    try {
      if (!this.startDateControl?.value || !this.endDateControl?.value!) {
        this.clearIncorrectDateValidation();
        return null;
      }
      const startDate = new Date(this.startDateControl!.value!);
      const endDate = new Date(this.endDateControl!.value!);
      if (startDate > endDate) {
        return { 'incorrect': true }
      }
    } catch (error) {
      this.clearIncorrectDateValidation();
      return null;
    }
    this.clearIncorrectDateValidation();
    return null;
  }

  private clearIncorrectDateValidation(): void {
    if (this.startDateControl?.hasError('incorrect')) {
      delete this.startDateControl!.errors!['incorrect'];
      if (Object.keys(this.startDateControl!.errors!).length === 0) {
        this.startDateControl!.setErrors(null);
      }
    }
    if (this.endDateControl?.hasError('incorrect')) {
      delete this.endDateControl!.errors!['incorrect'];
      if (Object.keys(this.endDateControl!.errors!).length === 0) {
        this.endDateControl!.setErrors(null);
      }
    }
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

  private filterCustomersByName(name: string) {
    this.filteredCustomers.set(
      this.customers().filter(customer => customer.name.toLowerCase().includes(name.toLowerCase().trim()))
    );
  }

  private filterCustomersById(id: string) {
    this.filteredCustomers.set(
      this.customers().filter(customer => customer.id === id)
    );
  }

  private filterIntermediariesByName(name: string) {
    this.filteredIntermediaries.set(
      this.intermediaries().filter(intermediary => intermediary.name.toLowerCase().includes(name.toLowerCase().trim()))
    );
  }

  private filterIntermediariesById(id: string) {
    this.filteredIntermediaries.set(
      this.intermediaries().filter(intermediary => intermediary.id === id)
    );
  }

  private filterCoachesByName(name: string) {
    const searchName = name.toLowerCase().trim();
    this.filteredCoaches.set(
      this.coaches().filter(coach => coach.firstName.toLowerCase().includes(searchName)
        || coach.lastName.toLowerCase().includes(searchName))
    );
  }

  private filterStatusesByValue(value: string) {
    const searchValue = value.toLowerCase().trim();
    this.filteredStatuses.set(
      this.statuses().filter(status => status.value.toLowerCase().includes(searchValue)
    ));
  }

  protected onSubmit(formDirective: FormGroupDirective) {
    const request = this.mapFormToRequest();
    this.serviceHttp.createNew(request).subscribe(response => {
      this.showPopUp(response);
      if (this.serviceId === null) {
        this.resetForm(formDirective);
      } else {
        this.router.navigate(['../../', SERVICES_LIST_PATH], { relativeTo: this.route });
      }
    });
  }

  private resetForm(formDirective: FormGroupDirective) {
    formDirective.resetForm();
    this.form.reset();
  }

  private mapFormToRequest(): CreateOrUpdateServiceRequest {
    return {
      id: this.serviceId,
      version: this.serviceVersion,
      number: this.numberControl.value!,
      name: this.nameControl.value!,
      type: this.typeControl.value!,
      startDate: this.startDateControl.value!,
      endDate: this.endDateControl.value!,
      numberOfParticipants: this.numberOfParticipantsControl.value!,
      serviceProviderId: (<ServiceProviderDataInterface>this.serviceProviderIdControl.value!).id,
      programId: (<ProgramDataInterface>this.programIdControl.value!).id,
      customerId: (<CustomerData>this.customerIdControl.value!).id,
      intermediaryId: (<IntermediaryData>this.intermediaryIdControl?.value)?.id,
      coachIds: (<CoachData[]>this.coachIdsControl.value!).map(coach => coach.id),
      status: (<ServiceStatusData>this.statusControl.value!).value
    }
  }

  private showPopUp(response: CreateNewServiceResponse) {
    this.snackbar.openTopCenterSnackbar(response.messages.join('\n'));
  }

  protected removeCoach(coach: CoachData) {
    const index = (<CoachData[]>this.coachIdsControl.value).indexOf(coach);
    if (index !== -1) {
      (<CoachData[]>this.coachIdsControl.value).splice(index, 1);
    }
    this.coaches().push(coach);
    this.filteredCoaches().push(coach);
  }

  protected onServiceNumberBlur() {
    const value = this.numberControl.value;
    if (value !== null) {
      this.serviceHttp.getServiceFromBur(value.trim()).subscribe(response => {
          this.numberControl.setValue(response.number);
          this.nameControl.setValue(response.title);
          this.typeControl.setValue(response.serviceType);
          this.startDateControl.setValue(response.startDate);
          this.endDateControl.setValue(response.endDate);
          const serviceProvider = this.getServiceProviderByName(response.serviceProviderName);
          if (serviceProvider) {
            this.serviceProviderIdControl.setValue(serviceProvider);
          }
          this.statusControl.setValue(response.status);
        }
      )
    }
  }

  private getServiceProviderByName(name: string) {
    return this.serviceProviders().find(item => item.name === name)
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

  protected displayOperatorName(operator: OperatorDataInterface | string): string {
    return typeof operator === 'string' ? operator : operator?.name;
  }

  protected displayCustomerName(customer: CustomerData | string): string {
    return typeof customer === 'string' ? customer : customer?.name;
  }

  protected displayIntermediaryName(intermediary: IntermediaryData | string): string {
    return typeof intermediary === 'string' ? intermediary : intermediary?.name;
  }

  protected displayStatusName(status: ServiceStatusData | string): string {
    return typeof status === 'string' ? status : status?.name;
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'new-service.validation.' + fieldName + '.' + validation;
  }

  private isSingleFilteredOperator(): boolean {
    return this.filteredOperators().length === 1;
  }

  private isSingleFilteredProgram(): boolean {
    return this.filteredPrograms().length === 1;
  }

  private isValidProgramControl(): boolean {
    const value = this.programIdControl?.value;
    return typeof value === 'object' && value !== null && 'id' in value;
  }

  protected getDateHintFormat(): string {
    return this.dateAdapter.getFormat()
  }
}
