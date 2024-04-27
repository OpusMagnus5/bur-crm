import {Component} from '@angular/core';
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatNoDataRow,
  MatRow,
  MatRowDef,
  MatTable
} from "@angular/material/table";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {UserListDataSource} from "./service/user-list.data-source";
import {UserHttpService} from "./service/user-http.service";
import {MatIconButton} from "@angular/material/button";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {MatIcon} from "@angular/material/icon";
import {UserListDataInterface} from "./model/user-list-data.interface";
import {MatDialog} from "@angular/material/dialog";
import {UserDetailsComponent} from "./user-details.component";
import {MatSnackBar} from "@angular/material/snack-bar";
import {Subject} from "rxjs";
import {UserListResponseInterface} from "./model/user-list-response.interface";

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
    MatNoDataRow,
  ],
  templateUrl: './users-list.component.html',
  styleUrl: './users-list.component.css'
})
export class UsersListComponent {

  data: Subject<UserListResponseInterface> = new Subject<UserListResponseInterface>()
  protected dataSource: UserListDataSource = new UserListDataSource(this.data);
  protected readonly columnsDef: string[] = ['email', 'firstName', 'lastName', 'role'];
  protected readonly rowsDef: string[] = ['email', 'firstName', 'lastName', 'role', 'options'];

  constructor(
    private http: UserHttpService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private translator: TranslateService
  ) {
    this.http.getUserPage(1, 10).subscribe(response =>
      this.data.next(response)
    );
  }

  onPageChange(event: PageEvent) {
    this.http.getUserPage(event.pageIndex + 1, event.pageSize).subscribe(response =>{
        this.data.next(response);
      }
    )
  }

  onDetails(element: UserListDataInterface) {
    this.http.getUserDetails(element.id).subscribe(response => {
      this.dialog.open(UserDetailsComponent, { data: response })
    })
  }

  onRemove(element: UserListDataInterface) {
    this.http.deleteUser(element.id).subscribe(response => {
      const action = this.translator.instant('delete-user.close');
      this.snackBar.open(response.message, action, {
        horizontalPosition: "center",
        verticalPosition: "top",
        duration: 3000
      })
    })
  }
}
