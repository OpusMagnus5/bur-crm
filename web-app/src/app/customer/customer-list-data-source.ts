import {CollectionViewer, DataSource} from "@angular/cdk/collections";
import {Observable, tap} from "rxjs";
import {WritableSignal} from "@angular/core";
import {toObservable} from "@angular/core/rxjs-interop";
import {map} from "rxjs/operators";
import {CustomerData, CustomerPageResponse} from "./customer-dtos";

export class CustomerListDataSource implements DataSource<CustomerData> {

  data: CustomerData[] = [];
  totalPrograms: number = 0;

  constructor(private dataSource: WritableSignal<CustomerPageResponse>) {
  }

  connect(collectionViewer: CollectionViewer): Observable<readonly CustomerData[]> {
    return toObservable(this.dataSource).pipe(
      tap(response => {
        this.data = response.customers;
        this.totalPrograms = response.totalCustomers;
      }),
      map(response => response.customers)
    )
  }
  disconnect(collectionViewer: CollectionViewer): void {
  }
}
