import {CollectionViewer, DataSource} from "@angular/cdk/collections";
import {ServiceProviderDataInterface} from "./service-provider-data.interface";
import {Observable, Subject, tap} from "rxjs";
import {ServiceProviderListResponseInterface} from "./service-provider-list-response.interface";
import {map} from "rxjs/operators";

export class ServiceProviderListDataSource implements DataSource<ServiceProviderDataInterface> {

  data: ServiceProviderDataInterface[] | undefined;
  totalServiceProviders: number = 0;

  constructor(private dataSource: Subject<ServiceProviderListResponseInterface>) {
  }

  connect(collectionViewer: CollectionViewer): Observable<readonly ServiceProviderDataInterface[]> {
        return this.dataSource.pipe(
          tap(response => {
            this.data = response.providers;
            this.totalServiceProviders = response.totalProviders;
          }),
          map(response => response.providers)
        );
  }
  disconnect(collectionViewer: CollectionViewer): void {
  }

}
