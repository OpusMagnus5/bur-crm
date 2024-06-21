import {Component, computed, OnDestroy, Signal, signal, WritableSignal} from '@angular/core';
import {ServiceHttp} from "./service-http";
import {ServiceData, ServicePageResponse, ServiceTypeData} from "./service-dtos";
import {HttpQueryFiltersInterface} from "../shared/model/http-query-filters.interface";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {ServiceListDataSource} from "./service-list.data-source";
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatNoDataRow,
  MatRow,
  MatRowDef,
  MatTable
} from "@angular/material/table";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {TranslateModule} from "@ngx-translate/core";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatOption, MatSelect} from "@angular/material/select";
import {ServiceProviderDataInterface} from "../service-provider/model/service-provider-data.interface";
import {ServiceProviderHttpService} from "../service-provider/service/service-provider-http.service";
import {CustomerHttpService} from "../customer/customer-http.service";
import {CustomerData} from "../customer/customer-dtos";
import {MatAutocomplete, MatAutocompleteSelectedEvent, MatAutocompleteTrigger} from "@angular/material/autocomplete";
import {ActivatedRoute, Router} from "@angular/router";
import {toObservable} from "@angular/core/rxjs-interop";
import {SubscriptionManager} from "../shared/util/subscription-manager";
import {concat, debounceTime, forkJoin, merge, skip, tap} from "rxjs";
import {EDIT_SERVICE_PATH, SERVICE_DETAILS_PATH} from "../app.routes";
import {ConfirmationDialogComponent} from "../shared/component/confirmation-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {ProgramDataInterface} from "../program/model/program-data-interface";
import {SnackbarService} from "../shared/service/snackbar.service";
import {LocalizedDatePipe} from "../shared/pipe/localized-date.pipe";

@Component({
  selector: 'app-service-list',
  standalone: true,
  imports: [
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderRow,
    MatHeaderRowDef,
    MatIcon,
    MatIconButton,
    MatMenu,
    MatMenuItem,
    MatPaginator,
    MatRow,
    MatRowDef,
    MatTable,
    TranslateModule,
    MatHeaderCellDef,
    MatMenuTrigger,
    MatNoDataRow,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
    FormsModule,
    MatSelect,
    MatOption,
    MatAutocomplete,
    MatAutocompleteTrigger,
    MatError
  ],
  templateUrl: './service-list.component.html'
})
export class ServiceListComponent implements OnDestroy {

  protected readonly data: WritableSignal<ServicePageResponse> = signal({ services: [], totalServices: 0 });
  protected readonly dataSource: ServiceListDataSource = new ServiceListDataSource(this.data);

  protected readonly allServiceTypes: WritableSignal<ServiceTypeData[]> = signal([]);
  protected readonly allServiceProviders: WritableSignal<ServiceProviderDataInterface[]> = signal([]);
  protected readonly allCustomers: WritableSignal<CustomerData[]> = signal([]);
  protected readonly filteredCustomers: WritableSignal<CustomerData[]> = signal([]);

  protected columnsDef: string[] = [
    'number', 'name', 'type', 'serviceProvider.name', 'operator.name', 'customer.name', 'startDate', 'endDate'
  ];
  protected rowsDef: string[] = [...this.columnsDef, 'options'];

  protected readonly pageDef = signal({ pageNumber: 1, pageSize: 10 });
  protected serviceNumberFilter = signal('');
  protected serviceTypeFilter = signal('');
  protected serviceProviderFilter = signal('');
  protected customerFilter: WritableSignal<CustomerData | null> = signal(null);
  private readonly filters: Signal<HttpQueryFiltersInterface> = computed(() => {
    const customerFilter = this.customerFilter();
    return {
      'pageNumber': this.pageDef().pageNumber,
      'pageSize': this.pageDef().pageSize,
      'number': this.serviceNumberFilter(),
      'type': this.serviceTypeFilter(),
      'serviceProviderId': this.serviceProviderFilter(),
      'customerId': customerFilter ? customerFilter.id : ''
    }
  });
  private readonly filtersObservable = toObservable(this.filters);
  private readonly allFiltersObservable = merge(
    toObservable(this.serviceNumberFilter),
    toObservable(this.serviceTypeFilter),
    toObservable(this.serviceProviderFilter),
    toObservable(this.customerFilter))

  private readonly subscriptionManager = new SubscriptionManager();

  constructor(
    private serviceHttp: ServiceHttp,
    private serviceProviderHttp: ServiceProviderHttpService,
    private customerHttp: CustomerHttpService,
    private route: ActivatedRoute,
    private router: Router,
    private dialog: MatDialog,
    private snackBarService: SnackbarService,
    private datePipe: LocalizedDatePipe
  ) {

    const subscription = concat(this.initFiltersData(), this.initTableData())
      .subscribe({
        complete: () => {
          this.fetchDataAfterFiltersChange();
          this.changeLinkAfterFiltersChange();
        }
      });
    this.subscriptionManager.add(subscription);

  }

  ngOnDestroy(): void {
    this.subscriptionManager.unsubscribeAll();
  }

  private initFiltersFromRoute() {
    const params = this.route.snapshot.queryParamMap;
    const pageNumber = params.get('pageNumber');
    const pageSize = params.get('pageSize');
    if (pageNumber && pageSize) {
      this.pageDef.set({pageNumber: Number(pageNumber), pageSize: Number(pageSize)});
    }
    const number = params.get('number');
    this.serviceNumberFilter.set(number ? number : '');
    const type = params.get('type');
    this.serviceTypeFilter.set(type ? type : '');
    const serviceProviderId = params.get('serviceProviderId');
    this.serviceProviderFilter.set(serviceProviderId ? serviceProviderId : '');
    const customerId = params.get('customerId');
    this.customerFilter.set(this.getCustomerById(customerId));
    this.filterCustomersById(customerId);
  }

  private initTableData() {
    return this.serviceHttp.getServicePage(this.filters()).pipe(
      tap(response => this.data.set(response)));
  }

  private initFiltersData() {
    return forkJoin([this.serviceHttp.getAllServiceTypes(),
      this.serviceProviderHttp.getAll(),
      this.customerHttp.getAll()
    ]).pipe(
        tap(([
        serviceTypes,
        serviceProviders,
        customers]) => {
        this.allServiceTypes.set(serviceTypes.serviceTypes);
        this.allServiceProviders.set(serviceProviders.serviceProviders);
        this.allCustomers.set(customers.customers);
        this.initFiltersFromRoute();
        })
    );
  }

  private changeLinkAfterFiltersChange() {
    const subscription = this.filtersObservable
      .subscribe(() => {
          this.router.navigate([], {
          relativeTo: this.route,
          queryParams: this.getOnlyNotEmptyFilters()
          });
      });
    this.subscriptionManager.add(subscription);
  }

  private fetchDataAfterFiltersChange() {
    const subscription = this.allFiltersObservable.pipe(
      debounceTime(1500),
      skip(1)
    ).subscribe(() => this.onPageChange({ pageIndex: 0, pageSize: 10, previousPageIndex: 0, length: 0 }));
    this.subscriptionManager.add(subscription);
  }

  protected onPageChange(event: PageEvent) {
    this.pageDef.set({ pageNumber: event.pageIndex + 1, pageSize: event.pageSize });
    this.serviceHttp.getServicePage(this.filters()).subscribe(response =>{
        this.data.set(response);
      }
    );
  }

  protected getColumnData(element: any, column: string): any {
    const fields: string[] = column.split(".");
    let data;
    if (fields.length === 2) {
      data = element[fields[0]][fields[1]];
    } else {
      data = element[fields[0]];
    }
    if (data instanceof Date) {
      return this.datePipe.transform(new Date(), 'mediumDate');
    }
    return data;
  }

  protected onDetails(element: any) {
    this.router.navigate(['../', SERVICE_DETAILS_PATH, element.id], {
      relativeTo: this.route
    });
  }

  protected onEdit(element: ServiceData) {
    this.router.navigate(['../', EDIT_SERVICE_PATH, element.id], {
      relativeTo: this.route
    });
  }

  protected onRemove(element: any) {
    const dialogRef = this.dialog.open(
      ConfirmationDialogComponent, {
        data: {
          codeForTranslation: 'delete-service'
        }
      });
    this.subscriptionManager.add(dialogRef.componentInstance.deleteConfirmation.subscribe(value => {
      if (value) {
        dialogRef.close();
        this.deleteOperator(element);
      }
    }));
  }

  private deleteOperator(element: ProgramDataInterface) {
    this.serviceHttp.delete(element.id).subscribe(response => {
      this.snackBarService.openTopCenterSnackbar(response.message);
      this.onPageChange({ pageIndex: this.pageDef().pageNumber - 1, pageSize: this.pageDef().pageSize, previousPageIndex: 1, length: 1 })
    })
  }

  protected onCustomerChange(event: Event) {
    const name: string = (<HTMLInputElement>event.target).value;
    this.filterCustomersByName(name);
  }

  protected displayCustomerName(customer: CustomerData | string): string {
    return typeof customer === 'string' ? customer : customer?.name;
  }

  protected onCustomerSelected(event: MatAutocompleteSelectedEvent) {
    const id = (<CustomerData>event.option.value).id;
    this.filterCustomersById(id);
    this.customerFilter.set(<CustomerData>event.option.value);
  }

  private filterCustomersByName(name: string) {
    this.filteredCustomers.set(
      this.allCustomers().filter(customer => customer.name.toLowerCase().includes(name.toLowerCase().trim()))
    );
  }

  private filterCustomersById(id: string | null) {
    if (id) {
      this.filteredCustomers.set(
        this.allCustomers().filter(customer => customer.id === id)
      );
    } else {
      this.filteredCustomers.set(this.allCustomers());
    }
  }

  private getCustomerById(id: string | null): CustomerData | null {
    if (id === null) {
      return null;
    }
    const customer = this.allCustomers().find(customer => customer.id === id);
    if (customer) {
      return customer
    }
    return null;
  }

  protected getClassForColumn(column: string): string {
    if (column === 'number') {
      return 'break-all';
    } else if (column === 'startDate' || column === 'endDate') {
      return 'whitespace-nowrap';
    }
    return '';
  }

  private getOnlyNotEmptyFilters(): HttpQueryFiltersInterface {
    return Object.entries(this.filters())
      .filter(([key, value]) => value !== null && value !== '')
      .reduce((acc, [key, value]) => {
        acc[key] = value;
        return acc;
      }, {} as HttpQueryFiltersInterface);
  }
}
