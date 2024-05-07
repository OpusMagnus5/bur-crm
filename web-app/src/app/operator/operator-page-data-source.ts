import {CollectionViewer, DataSource} from "@angular/cdk/collections";
import {OperatorDataInterface} from "./model/operator-data.interface";
import {Observable, Subject, tap} from "rxjs";
import {map} from "rxjs/operators";
import {OperatorPageResponseInterface} from "./model/operator-page-response.interface";

export class OperatorPageDataSource implements DataSource<OperatorDataInterface> {

  data: OperatorDataInterface[] = [];
  totalOperators: number = 0;

  constructor(private dataSource: Subject<OperatorPageResponseInterface>) {
  }

  connect(collectionViewer: CollectionViewer): Observable<readonly OperatorDataInterface[]> {
    return this.dataSource.pipe(
      tap(response => {
        this.data = response.operators;
        this.totalOperators = response.totalOperators;
      }),
      map(response => response.operators)
    );
  }
  disconnect(collectionViewer: CollectionViewer): void {
  }

}
