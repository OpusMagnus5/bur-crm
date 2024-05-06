import {Component} from '@angular/core';
import {MatPaginator, MatPaginatorIntl, PageEvent} from "@angular/material/paginator";
import {PaginatorLocalizerService} from "../shared/service/paginator-localizer.service";
import {Subject} from "rxjs";
import {TranslateModule} from "@ngx-translate/core";
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
import {SnackbarService} from "../shared/service/snackbar.service";
import {DeleteConfirmationDataInterface} from "../shared/model/delete-confirmation-data.interface";
import {DialogService} from "../shared/service/dialog.service";

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
  providers: [
    { provide: MatPaginatorIntl, useClass: PaginatorLocalizerService },
    DialogService
  ],
  templateUrl: './operator-list.component.html',
  styleUrl: './operator-list.component.css'
})
export class OperatorListComponent {

  private readonly data: Subject<OperatorPageResponseInterface> = new Subject<OperatorPageResponseInterface>();
  protected readonly dataSource: OperatorPageDataSource = new OperatorPageDataSource(this.data);
  protected readonly columnsDef: string[] = ['name'];
  protected readonly rowsDef: string[] = ['name', 'options'];
  protected pageDef: { pageNumber: number; pageSize: number; } = { pageNumber: 1, pageSize: 10 };
  private deleteConfirmationData: DeleteConfirmationDataInterface = {
    codeForTranslation: 'delete-operator',
    callbackArgument: '',
    removeCallback: this.deleteOperator
  };

  constructor(
    private http: OperatorHttpService,
    private snackbarService: SnackbarService,
    private deleteConfirmation: DialogService
  ) {
    http.getOperatorPage(1, 10).subscribe(response =>
      this.data.next(response)
    );
  }

  onPageChange(event: PageEvent) {
    this.pageDef = { pageNumber: event.pageIndex + 1, pageSize: event.pageSize };
    this.http.getOperatorPage(event.pageIndex + 1, event.pageSize).subscribe(response =>{
        this.data.next(response);
      }
    )
  }

  onRemove(element: OperatorDataInterface) {
    this.deleteConfirmationData.callbackArgument = element.id;
    this.deleteConfirmation.openDeleteConfirmation(this.deleteConfirmationData);
  }

  private deleteOperator(id: string) {
    this.http.delete(id).subscribe(response => {
      this.snackbarService.openTopCenterSnackbar(response.message);
      this.onPageChange({ pageIndex: this.pageDef.pageNumber - 1, pageSize: this.pageDef.pageSize, previousPageIndex: 1, length: 1 })
    })
  }

  onEdit(element: OperatorPageDataSource) {

  }

  onDetails(element: OperatorPageDataSource) {

  }
}
