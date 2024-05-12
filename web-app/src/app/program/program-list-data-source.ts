import {CollectionViewer, DataSource} from "@angular/cdk/collections";
import {ProgramDataInterface} from "./model/program-data-interface";
import {Observable, tap} from "rxjs";
import {WritableSignal} from "@angular/core";
import {ProgramPageResponseInterface} from "./model/program-page-response.interface";
import {toObservable} from "@angular/core/rxjs-interop";
import {map} from "rxjs/operators";

export class ProgramListDataSource implements DataSource<ProgramDataInterface> {

  data: ProgramDataInterface[] = [];
  totalPrograms: number = 0;

  constructor(private dataSource: WritableSignal<ProgramPageResponseInterface>) {
  }

  connect(collectionViewer: CollectionViewer): Observable<readonly ProgramDataInterface[]> {
    return toObservable(this.dataSource).pipe(
      tap(response => {
        this.data = response.programs;
        this.totalPrograms = response.totalPrograms;
      }),
      map(response => response.programs)
    )
  }
  disconnect(collectionViewer: CollectionViewer): void {
  }
}
