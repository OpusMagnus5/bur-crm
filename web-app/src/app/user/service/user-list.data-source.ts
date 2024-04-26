import {DataSource} from "@angular/cdk/collections";
import {Observable, tap} from "rxjs";
import {UserListDataInterface} from "../model/user-list-data.interface";
import {UsersListService} from "../users-list.service";
import {map} from "rxjs/operators";

export class UserListDataSource implements DataSource<UserListDataInterface> {

  data: UserListDataInterface[] | undefined;
  totalUsers: number = 0;

  constructor(private userListService: UsersListService) {
  }

  connect(): Observable<readonly UserListDataInterface[]> {
   return this.userListService.data.pipe(
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
