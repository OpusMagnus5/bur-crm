import {Component, OnDestroy} from '@angular/core';
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
import {MatPaginatorIntl, MatPaginatorModule, PageEvent} from "@angular/material/paginator";
import {TranslateModule} from "@ngx-translate/core";
import {UserListDataSource} from "./service/user-list.data-source";
import {UserHttpService} from "./service/user-http.service";
import {MatIconButton} from "@angular/material/button";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {MatIcon} from "@angular/material/icon";
import {UserListDataInterface} from "./model/user-list-data.interface";
import {MatDialog} from "@angular/material/dialog";
import {UserDetailsComponent} from "./user-details.component";
import {Subject} from "rxjs";
import {UserListResponseInterface} from "./model/user-list-response.interface";
import {PaginatorLocalizerService} from "../shared/service/paginator-localizer.service";
import {SnackbarService} from "../shared/service/snackbar.service";
import {SubscriptionManager} from "../shared/util/subscription-manager";
import {DeleteRecordConfirmationComponent} from "../shared/component/delete-record-confirmation.component";

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
    MatPaginatorModule,
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
  providers: [
    { provide: MatPaginatorIntl, useClass: PaginatorLocalizerService },
  ],
  templateUrl: './users-list.component.html',
  styleUrl: './users-list.component.css'
})
export class UsersListComponent implements OnDestroy {

  private subscriptions: SubscriptionManager = new SubscriptionManager();
  private readonly data: Subject<UserListResponseInterface> = new Subject<UserListResponseInterface>()
  protected readonly dataSource: UserListDataSource = new UserListDataSource(this.data);
  protected readonly columnsDef: string[] = ['email', 'firstName', 'lastName', 'role'];
  protected readonly rowsDef: string[] = ['email', 'firstName', 'lastName', 'role', 'options'];
  protected pageDef: { pageNumber: number; pageSize: number; } = { pageNumber: 1, pageSize: 10 };

  constructor(
    private http: UserHttpService,
    private dialog: MatDialog,
    private snackbar: SnackbarService,
  ) {
    this.http.getUserPage(1, 10).subscribe(response =>
      this.data.next(response)
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribeAll();
  }

  onPageChange(event: PageEvent) {
    this.pageDef = { pageNumber: event.pageIndex + 1, pageSize: event.pageSize };
    this.http.getUserPage(event.pageIndex + 1, event.pageSize).subscribe(response =>{
        this.data.next(response);
      }
    )
  }

  onDetails(element: UserListDataInterface) {
    this.http.getUserDetails(element.id).subscribe(response => {
      this.dialog.open(UserDetailsComponent, { data: response, disableClose: true })
    })
  }

  onRemove(element: UserListDataInterface) {
    const dialogRef = this.dialog.open(
      DeleteRecordConfirmationComponent, {
        data: {
          codeForTranslation: 'delete-user'
        }
      }
    );
    this.subscriptions.add(dialogRef.componentInstance.deleteConfirmation.subscribe(value => {
      if (value) {
        dialogRef.close();
        this.deleteUser(element.id);
      }
    }));
  }

  private deleteUser(id: string) {
    this.http.deleteUser(id).subscribe(response => {
      this.snackbar.openTopCenterSnackbar(response.message);
      this.onPageChange({ pageIndex: this.pageDef.pageNumber - 1, pageSize: this.pageDef.pageSize, previousPageIndex: 1, length: 1 })
    })
  }
}
