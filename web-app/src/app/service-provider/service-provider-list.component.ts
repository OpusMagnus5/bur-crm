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
import {ServiceProviderListDataSource} from "./model/service-provider-list-data-source";
import {Subject, Subscription} from "rxjs";
import {ServiceProviderListResponseInterface} from "./model/service-provider-list-response.interface";
import {ServiceProviderHttpService} from "./service/service-provider-http.service";
import {MatPaginatorIntl, MatPaginatorModule, PageEvent} from "@angular/material/paginator";
import {TranslateModule} from "@ngx-translate/core";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {MatDialog} from "@angular/material/dialog";
import {ServiceProviderDetailsComponent} from "./service-provider-details.component";
import {ServiceProviderDataInterface} from "./model/service-provider-data.interface";
import {UpdateServiceProviderComponent} from "./update-service-provider.component";
import {PaginatorLocalizerService} from "../shared/service/paginator-localizer.service";
import {SnackbarService} from "../shared/service/snackbar.service";
import {DeleteConfirmationDataInterface} from "../shared/model/delete-confirmation-data.interface";
import {DialogService} from "../shared/service/dialog.service";

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
    MatPaginatorModule
  ],
  providers: [
    { provide: MatPaginatorIntl, useClass: PaginatorLocalizerService },
    DialogService
  ],
  templateUrl: './service-provider-list.component.html',
  styleUrl: './service-provider-list.component.css'
})
export class ServiceProviderListComponent implements OnDestroy {

  private readonly data: Subject<ServiceProviderListResponseInterface> = new Subject<ServiceProviderListResponseInterface>();
  protected readonly dataSource: ServiceProviderListDataSource = new ServiceProviderListDataSource(this.data);
  protected readonly columnsDef: string[] = ['name', 'nip'];
  protected readonly rowsDef: string[] = ['name', 'nip', 'options'];
  protected pageDef: { pageNumber: number; pageSize: number; } = { pageNumber: 1, pageSize: 10 };
  private updateSubscription: Subscription | undefined;
  private deleteConfirmationData: DeleteConfirmationDataInterface = {
    codeForTranslation: 'delete-service-provider',
    callbackArgument: '',
    removeCallback: this.deleteServiceProvider
  };

  constructor(
    private http: ServiceProviderHttpService,
    private dialog: MatDialog,
    private snackbar: SnackbarService,
    private deleteConfirmation: DialogService
  ) {
    this.http.getProviderPage(1, 10).subscribe(response =>
      this.data.next(response)
    );
  }

  ngOnDestroy(): void {
        this.updateSubscription?.unsubscribe();
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
      this.dialog.open(ServiceProviderDetailsComponent, { data: response, disableClose: true })
    })
  }

  onRemove(element: ServiceProviderDataInterface) {
    this.deleteConfirmationData.callbackArgument = element.id;
    this.deleteConfirmation.openDeleteConfirmation(this.deleteConfirmationData);
  }

  private deleteServiceProvider(id: string) {
    this.http.delete(id).subscribe(response => {
      this.snackbar.openTopCenterSnackbar(response.message);
      this.onPageChange({ pageIndex: this.pageDef.pageNumber - 1, pageSize: this.pageDef.pageSize, previousPageIndex: 1, length: 1 })
    })
  }

  onEdit(element: ServiceProviderDataInterface) { //TODO wydzielic service z otwieraniem dialogu z callbackiem
    this.http.getDetails(element.id).subscribe(response => {
      const dialogRef = this.dialog.open(UpdateServiceProviderComponent, { data: response, disableClose: true });
      this.updateSubscription = dialogRef.componentInstance.updateConfirmation.subscribe(value => {
        if (value) {
          dialogRef.close();
          this.onPageChange({ pageIndex: this.pageDef.pageNumber - 1, pageSize: this.pageDef.pageSize, previousPageIndex: 1, length: 1 });
        }
      })
    });
  }
}
