import {DataSource} from "@angular/cdk/collections";
import {Observable, Subject, tap} from "rxjs";
import {UserListDataInterface} from "../model/user-list-data.interface";
import {map} from "rxjs/operators";
import {UserListResponseInterface} from "../model/user-list-response.interface";

export class UserListDataSource implements DataSource<UserListDataInterface> {

  data: UserListDataInterface[] | undefined;
  totalUsers: number = 0;

  constructor(private dataSource: Subject<UserListResponseInterface>) {
  }

  connect(): Observable<readonly UserListDataInterface[]> {
   return this.dataSource.pipe(
     tap(response => {
       this.data = response.users;
       this.totalUsers = response.totalUsers;
     }),
     map(response => response.users)
   );

  }

  disconnect(): void {
  }

}
