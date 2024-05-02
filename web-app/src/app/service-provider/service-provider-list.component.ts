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
import {ServiceProviderListDataSource} from "./model/service-provider-list-data-source";
import {Subject} from "rxjs";
import {ServiceProviderListResponseInterface} from "./model/service-provider-list-response.interface";
import {ServiceProviderHttpService} from "./service/service-provider-http.service";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {MatDialog} from "@angular/material/dialog";
import {ServiceProviderDetailsComponent} from "./service-provider-details.component";
import {DeleteServiceProviderConfirmationComponent} from "./dialog/delete-service-provider-confirmation.component";
import {ServiceProviderDataInterface} from "./model/service-provider-data.interface";
import {MatSnackBar} from "@angular/material/snack-bar";

@Component({
  selector: 'app-service-provider-list',
  standalone: true,
  imports: [
    MatTable,
    MatColumnDef,
    MatHeaderCell,
    MatHeaderCellDef,
    MatCell,
    MatCellDef,
    TranslateModule,
    MatHeaderRow,
    MatHeaderRowDef,
    MatRow,
    MatRowDef,
    MatIcon,
    MatIconButton,
    MatMenu,
    MatMenuItem,
    MatMenuTrigger,
    MatNoDataRow,
    MatPaginator
  ],
  templateUrl: './service-provider-list.component.html',
  styleUrl: './service-provider-list.component.css'
})
export class ServiceProviderListComponent {

  private readonly data: Subject<ServiceProviderListResponseInterface> = new Subject<ServiceProviderListResponseInterface>();
  protected readonly dataSource: ServiceProviderListDataSource = new ServiceProviderListDataSource(this.data);
  protected readonly columnsDef: string[] = ['name', 'nip'];
  protected readonly rowsDef: string[] = ['name', 'nip', 'options'];
  protected pageDef: { pageNumber: number; pageSize: number; } = { pageNumber: 1, pageSize: 10 };

  constructor(
    private http: ServiceProviderHttpService,
    private dialog: MatDialog,
    private translator: TranslateService,
    private snackBar: MatSnackBar
  ) {
    this.http.getProviderPage(1, 10).subscribe(response =>
      this.data.next(response)
    );
  }

  onPageChange(event: PageEvent) {
    this.pageDef = { pageNumber: event.pageIndex + 1, pageSize: event.pageSize };
    this.http.getProviderPage(event.pageIndex + 1, event.pageSize).subscribe(response =>{
        this.data.next(response);
      }
    )
  }

  onDetails(element: ServiceProviderDataInterface) {
    this.http.getDetails(element.id).subscribe(response => {
      this.dialog.open(ServiceProviderDetailsComponent, { data: response })
    })
  }

  onRemove(element: ServiceProviderDataInterface) {
    const dialogRef = this.dialog.open(DeleteServiceProviderConfirmationComponent);
    dialogRef.componentInstance.deleteConfirmation.subscribe(value => {
      if (value) {
        dialogRef.close();
        this.deleteServiceProvider(element);
      }
    })
  }

  private deleteServiceProvider(element: ServiceProviderDataInterface) {
    this.http.delete(element.id).subscribe(response => {
      const action = this.translator.instant('common.close-button');
      this.snackBar.open(response.message, action, {
        horizontalPosition: "center",
        verticalPosition: "top",
        duration: 3000
      })
      this.onPageChange({ pageIndex: this.pageDef.pageNumber - 1, pageSize: this.pageDef.pageSize, previousPageIndex: 1, length: 1 })
    })
  }

  onEdit(element: ServiceProviderDataInterface) {

  }
}
