import {CollectionViewer, DataSource} from "@angular/cdk/collections";
import {Observable, tap} from "rxjs";
import {WritableSignal} from "@angular/core";
import {toObservable} from "@angular/core/rxjs-interop";
import {map} from "rxjs/operators";
import {IntermediaryData, IntermediaryPageResponse} from "./intermediary-dtos";

export class IntermediaryListDataSource implements DataSource<IntermediaryData> {

  data: IntermediaryData[] = [];
  totalPrograms: number = 0;

  constructor(private dataSource: WritableSignal<IntermediaryPageResponse>) {
  }

  connect(collectionViewer: CollectionViewer): Observable<readonly IntermediaryData[]> {
    return toObservable(this.dataSource).pipe(
      tap(response => {
        this.data = response.intermediaries;
        this.totalPrograms = response.totalIntermediaries;
      }),
      map(response => response.intermediaries)
    )
  }
  disconnect(collectionViewer: CollectionViewer): void {
  }
}
