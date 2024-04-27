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
import {TranslateModule} from "@ngx-translate/core";
import {UserListDataSource} from "./service/user-list.data-source";
import {UsersListService} from "./users-list.service";
import {UserHttpService} from "./service/user-http.service";
import {MatIconButton} from "@angular/material/button";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {MatIcon} from "@angular/material/icon";
import {UserListDataInterface} from "./model/user-list-data.interface";
import {MatDialog} from "@angular/material/dialog";
import {UserDetailsComponent} from "./user-details.component";

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
    MatTable,
    MatHeaderCellDef,
    TranslateModule,
    MatIconButton,
    MatMenuTrigger,
    MatIcon,
    MatMenu,
    MatMenuItem,
  ],
  templateUrl: './users-list.component.html',
  styleUrl: './users-list.component.css',
  providers: [UsersListService]
})
export class UsersListComponent {

  protected dataSource: UserListDataSource = new UserListDataSource(this.userListService);
  protected readonly columnsDef: string[] = ['email', 'firstName', 'lastName', 'role'];
  protected readonly rowsDef: string[] = ['email', 'firstName', 'lastName', 'role', 'options'];

  constructor(
    private userListService: UsersListService,
    private http: UserHttpService,
    private dialog: MatDialog
  ) {
  }

  onPageChange(event: PageEvent) {
    this.http.getUserPage(event.pageIndex + 1, event.pageSize).subscribe(response =>
      this.userListService.data.next(response)
    )
  }

  onDetails(element: UserListDataInterface) {
    this.http.getUserDetails(element.id).subscribe(response => {
      this.dialog.open(UserDetailsComponent, { data: response })
    })
  }

  onRemove(element: UserListDataInterface) {

  }
}
