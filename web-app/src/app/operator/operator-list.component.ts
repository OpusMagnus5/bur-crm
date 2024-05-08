import {AfterViewInit, Component, computed, ElementRef, Signal, signal, ViewChild, WritableSignal} from '@angular/core';
import {MatPaginator, MatPaginatorIntl, PageEvent} from "@angular/material/paginator";
import {PaginatorLocalizerService} from "../shared/service/paginator-localizer.service";
import {debounceTime, fromEvent, Subject} from "rxjs";
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
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {HttpQueryFiltersInterface} from "../shared/model/http-query-filters.interface";
import {FormsModule} from "@angular/forms";
import {MatDialog} from "@angular/material/dialog";
import {OperatorDetailsComponent} from "./operator-details.component";
import {DialogDataInterface} from "../shared/model/dialog-data.interface";
import {UpdateOperatorComponent} from "./update-operator.component";

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
    MatNoDataRow,
    MatFormField,
    MatInput,
    MatLabel,
    FormsModule
  ],
  providers: [
    { provide: MatPaginatorIntl, useClass: PaginatorLocalizerService },
    DialogService
  ],
  templateUrl: './operator-list.component.html',
  styleUrl: './operator-list.component.css'
})
export class OperatorListComponent implements AfterViewInit {

  private readonly data: Subject<OperatorPageResponseInterface> = new Subject<OperatorPageResponseInterface>();
  protected readonly dataSource: OperatorPageDataSource = new OperatorPageDataSource(this.data);
  protected readonly columnsDef: string[] = ['name'];
  protected readonly rowsDef: string[] = ['name', 'options'];
  protected pageDef = signal({ pageNumber: 1, pageSize: 10 });
  private deleteConfirmationData: DeleteConfirmationDataInterface = {
    codeForTranslation: 'delete-operator',
    callbackArgument: '',
    removeCallback: this.deleteOperator
  };
  protected nameFilter:WritableSignal<string> = signal('');
  private filters: Signal<HttpQueryFiltersInterface> = computed(() => {
    return {
      'name': this.nameFilter(),
      'pageNumber': this.pageDef().pageNumber,
      'pageSize': this.pageDef().pageSize
    };
  });
  @ViewChild('filter') filter!: ElementRef;

  private updateDialogData: Signal<DialogDataInterface> = computed(() => {
    return {
      component: UpdateOperatorComponent,
      config: {
        data: null,
        disableClose: true
      },
      callbackArguments: [
        {
          pageIndex: this.pageDef().pageNumber - 1,
          pageSize: this.pageDef().pageSize,
          previousPageIndex: 1,
          length: 1
        }
      ],
      callback: this.onPageChange
    }
  });

  constructor(
    private http: OperatorHttpService,
    private snackbarService: SnackbarService,
    private deleteConfirmation: DialogService,
    private dialog: MatDialog,
    private dialogService: DialogService
  ) {
    this.http.getOperatorPage(this.filters()).subscribe(response => {
      this.data.next(response);
    })
  }

  ngAfterViewInit(): void {
    fromEvent(this.filter.nativeElement, 'input')
      .pipe(debounceTime(1000))
      .subscribe({
        next: () => {
            this.pageDef().pageNumber = 1;
            this.http.getOperatorPage(this.filters()).subscribe(response => {
              this.data.next(response);
            });
        }
      });
  }

  onPageChange(event: PageEvent) {
    this.pageDef.set({ pageNumber: event.pageIndex + 1, pageSize: event.pageSize });
    this.http.getOperatorPage(this.filters()).subscribe(response =>{
        this.data.next(response);
      }
    )
  }

  onRemove(element: OperatorDataInterface) {
    this.deleteConfirmationData.callbackArgument = element.id;
    this.deleteConfirmation.openDeleteConfirmation(this.deleteConfirmationData);
  }

  private deleteOperator(id: string) { //TODO nie działa
    this.http.delete(id).subscribe(response => {
      this.snackbarService.openTopCenterSnackbar(response.message);
      this.onPageChange({
        pageIndex: this.pageDef().pageNumber - 1,
        pageSize: this.pageDef().pageSize,
        previousPageIndex: 1,
        length: 1
      });
    })
  }

  onEdit(element: OperatorDataInterface) {
    this.http.getDetails(element.id).subscribe(response => {
      this.updateDialogData().config.data = response;
      this.dialogService.openDetailsDialog(this.updateDialogData());
    });
  }

  onDetails(element: OperatorDataInterface) {
    this.http.getDetails(element.id).subscribe(response => {
      this.dialog.open(OperatorDetailsComponent, { data: response })
    })
  }
}
