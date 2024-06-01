import {Component, computed, Signal, signal, WritableSignal} from '@angular/core';
import {ServiceHttp} from "./service-http";
import {ServicePageResponse} from "./service-dtos";
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
    MatNoDataRow
  ],
  templateUrl: './service-list.component.html'
})
export class ServiceListComponent {

  protected readonly data: WritableSignal<ServicePageResponse> = signal({ services: [], totalServices: 0 });
  protected readonly dataSource: ServiceListDataSource = new ServiceListDataSource(this.data);
  protected columnsDef: string[] = [
    'number', 'name', 'type', 'serviceProvider.name', 'operator.name', 'customer.name', 'startDate', 'endDate'
  ];
  protected rowsDef: string[] = [...this.columnsDef, 'options'];
  protected pageDef = signal({ pageNumber: 1, pageSize: 10 });
  private filters: Signal<HttpQueryFiltersInterface> = computed(() => {
    return {
      'pageNumber': this.pageDef().pageNumber,
      'pageSize': this.pageDef().pageSize
    }
  })

  constructor(
    private serviceHttp: ServiceHttp,
  ) {
    this.serviceHttp.getServicePage(this.filters()).subscribe(response => {
      this.data.set(response);
    });
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

  protected getClassForColumn(column: string): string {
    if (column === 'number') {
      return 'break-all';
    } else if (column === 'startDate' || column === 'endDate') {
      return 'whitespace-nowrap';
    }
    return '';

  }
}
