import {Component, computed, Signal, signal, WritableSignal} from '@angular/core';
import {ServiceHttp} from "./service-http";
import {ServicePageResponse, ServiceTypeData} from "./service-dtos";
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
import {OperatorDataInterface} from "../operator/model/operator-data.interface";

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
export class ServiceListComponent {

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
  protected readonly customerFilter = signal('');
  private readonly filters: Signal<HttpQueryFiltersInterface> = computed(() => {
    return {
      'pageNumber': this.pageDef().pageNumber,
      'pageSize': this.pageDef().pageSize,
      'number': this.serviceNumberFilter(),
      'type': this.serviceTypeFilter(),
      'serviceProviderId': this.serviceProviderFilter(),
      'customerId': this.customerFilter()
    }
  })

  constructor(
    private serviceHttp: ServiceHttp,
    private serviceProviderHttp: ServiceProviderHttpService,
    private customerHttp: CustomerHttpService
  ) {
    this.serviceHttp.getServicePage(this.filters()).subscribe(response => {
      this.data.set(response);
    });
    this.serviceHttp.getAllServiceTypes().subscribe(response => {
      this.allServiceTypes.set(response.serviceTypes);
    });
    this.serviceProviderHttp.getAll().subscribe(response => {
      this.allServiceProviders.set(response.serviceProviders);
    });
    this.customerHttp.getAll().subscribe(response => {
      this.allCustomers.set(response.customers);
      this.filteredCustomers.set(response.customers);
    })
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
    if (fields.length === 2) {
      return element[fields[0]][fields[1]];
    }
    return element[fields[0]];
  }

  protected onDetails(element: any) {

  }

  protected onEdit(element: any) {

  }

  protected onRemove(element: any) {

  }

  protected onCustomerChange(event: Event) {
    const name: string = (<HTMLInputElement>event.target).value;
    this.filterCustomersByName(name);
  }

  protected displayCustomerName(customer: CustomerData | string): string {
    return typeof customer === 'string' ? customer : customer?.name;
  }

  protected onCustomerSelected(event: MatAutocompleteSelectedEvent) {
    const id = (<OperatorDataInterface>event.option.value).id;
    this.filterCustomersById(id);
    this.customerFilter.set(id);
  }

  private filterCustomersByName(name: string) {
    this.filteredCustomers.set(
      this.allCustomers().filter(customer => customer.name.toLowerCase().includes(name.toLowerCase().trim()))
    );
  }

  private filterCustomersById(id: string) {
    this.filteredCustomers.set(
      this.allCustomers().filter(customer => customer.id === id)
    );
  }

  protected getClassForColumn(column: string): string {
    if (column === 'number') {
      return 'break-all';
    } else if (column === 'startDate' || column === 'endDate') {
      return 'whitespace-nowrap';
    }
    return '';
  }
}
