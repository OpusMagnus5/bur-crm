import {CollectionViewer, DataSource} from "@angular/cdk/collections";
import {ServiceData, ServicePageResponse} from "./service-dtos";
import {WritableSignal} from "@angular/core";
import {Observable} from "rxjs";
import {toObservable} from "@angular/core/rxjs-interop";
import {map} from "rxjs/operators";

export class ServiceListDataSource implements DataSource<ServiceData> {

  constructor(
    private dataSource: WritableSignal<ServicePageResponse>
  ) {
  }

  connect(collectionViewer: CollectionViewer): Observable<ServiceData[]> {
    return toObservable(this.dataSource).pipe(
      map(response => response.services)
    );
  }

  disconnect(collectionViewer: CollectionViewer) {
  }
}
