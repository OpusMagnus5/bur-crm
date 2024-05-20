import {CollectionViewer, DataSource} from "@angular/cdk/collections";
import {Observable, tap} from "rxjs";
import {WritableSignal} from "@angular/core";
import {toObservable} from "@angular/core/rxjs-interop";
import {map} from "rxjs/operators";
import {CoachData, CoachPageResponse} from "./coach-dtos";

export class CoachListDataSource implements DataSource<CoachData> {

  data: CoachData[] = [];
  totalPrograms: number = 0;

  constructor(private dataSource: WritableSignal<CoachPageResponse>) {
  }

  connect(collectionViewer: CollectionViewer): Observable<readonly CoachData[]> {
    return toObservable(this.dataSource).pipe(
      tap(response => {
        this.data = response.coaches;
        this.totalPrograms = response.totalCoaches;
      }),
      map(response => response.coaches)
    )
  }
  disconnect(collectionViewer: CollectionViewer): void {
  }
}
