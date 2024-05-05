import {Component, OnDestroy} from '@angular/core';
import {MatPaginator, MatPaginatorIntl, PageEvent} from "@angular/material/paginator";
import {PaginatorLocalizerService} from "../shared/service/paginator-localizer.service";
import {Subject, Subscription} from "rxjs";
import {MatDialog} from "@angular/material/dialog";
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {MatSnackBar} from "@angular/material/snack-bar";
import {OperatorHttpService} from "./service/operator-http.service";
import {OperatorPageResponseInterface} from "./model/operator-page-response.interface";
import {OperatorPageDataSource} from "./operator-page-data-source";
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
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {OperatorDataInterface} from "./model/operator-data.interface";
import {DeleteOperatorConfirmationComponent} from "./dialog/delete-operator-confirmation.component";

@Component({
  selector: 'app-operator-list',
  standalone: true,
  imports: [
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderRow,
    MatHeaderRowDef,
    MatIcon,
    MatIconButton,
    MatMenu,
    MatMenuItem,
    MatPaginator,
    MatRow,
    MatRowDef,
    MatTable,
    TranslateModule,
    MatHeaderCellDef,
    MatMenuTrigger,
    MatNoDataRow
  ],
  providers: [{provide: MatPaginatorIntl, useClass: PaginatorLocalizerService}],
  templateUrl: './operator-list.component.html',
  styleUrl: './operator-list.component.css'
})
export class OperatorListComponent implements OnDestroy {

  private readonly data: Subject<OperatorPageResponseInterface> = new Subject<OperatorPageResponseInterface>();
  protected readonly dataSource: OperatorPageDataSource = new OperatorPageDataSource(this.data);
  protected readonly columnsDef: string[] = ['name'];
  protected readonly rowsDef: string[] = ['name', 'options'];
  protected pageDef: { pageNumber: number; pageSize: number; } = { pageNumber: 1, pageSize: 10 };

  private removeSubscription: Subscription | undefined;

  constructor(
    private http: OperatorHttpService,
    private dialog: MatDialog,
    private translator: TranslateService,
    private snackBar: MatSnackBar
  ) {
    this.http.getOperatorPage(1, 10).subscribe(response =>
      this.data.next(response)
    );
  }

  ngOnDestroy(): void {
    this.removeSubscription?.unsubscribe();
  }

  onPageChange(event: PageEvent) {
    this.pageDef = { pageNumber: event.pageIndex + 1, pageSize: event.pageSize };
    this.http.getOperatorPage(event.pageIndex + 1, event.pageSize).subscribe(response =>{
        this.data.next(response);
      }
    )
  }

  onRemove(element: OperatorDataInterface) {
    const dialogRef = this.dialog.open(DeleteOperatorConfirmationComponent);
    this.removeSubscription = dialogRef.componentInstance.deleteConfirmation.subscribe(value => {
      if (value) {
        dialogRef.close();
        this.deleteOperator(element);
      }
    });
  }

  private deleteOperator(element: OperatorDataInterface) {
    this.http.delete(element.id).subscribe(response => {
      this.translator.get('common.close-button').subscribe(text => {
        this.snackBar.open(response.message, text, {
          horizontalPosition: "center",
          verticalPosition: "top",
          duration: 3000
        });
      });
      this.onPageChange({ pageIndex: this.pageDef.pageNumber - 1, pageSize: this.pageDef.pageSize, previousPageIndex: 1, length: 1 })
    })
  }

  onEdit(element: OperatorPageDataSource) {

  }

  onDetails(element: OperatorPageDataSource) {

  }
}
