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
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {MatSort, MatSortHeader} from "@angular/material/sort";
import {TranslateModule} from "@ngx-translate/core";
import {UserListDataSource} from "./model/user-list.data-source";
import {UsersListService} from "./users-list.service";
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
  styleUrl: './users-list.component.css',
  providers: [UsersListService]
})
export class UsersListComponent {

  protected dataSource: UserListDataSource = new UserListDataSource(this.userListService);
  protected readonly displayedColumns: String[] = ['email', 'firstName', 'lastName', 'role']

  constructor(private userListService: UsersListService, private http: UserHttpService) {
  } //TODO tłumaczenia, sortowanie, szukajka

  onPageChange(event: PageEvent) {
    this.http.getUserPage(event.pageIndex + 1, event.pageSize).subscribe(response =>
      this.userListService.data.next(response)
    )
  }
}
