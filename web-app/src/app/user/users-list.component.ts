import {Component} from '@angular/core';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable
} from "@angular/material/table";
import {MatPaginator} from "@angular/material/paginator";
import {MatSort, MatSortHeader} from "@angular/material/sort";
import {TranslateModule} from "@ngx-translate/core";
import {UserListDataInterface} from "./model/user-list-data.interface";
import {UserHttpService} from "./service/user-http.service";

@Component({
  selector: 'app-users-list',
  standalone: true,
  imports: [
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderRow,
    MatHeaderRowDef,
    MatPaginator,
    MatRow,
    MatRowDef,
    MatSort,
    MatSortHeader,
    MatTable,
    MatHeaderCellDef,
    TranslateModule,
  ],
  templateUrl: './users-list.component.html',
  styleUrl: './users-list.component.css'
})
export class UsersListComponent {


  protected users: UserListDataInterface[];

  constructor(private http: UserHttpService) {
  }

  private getDataPage(): UserListDataInterface[] {
    this.http.getUserPage().subscribe(data =>
      this.users = data.users
    )
  }
}
