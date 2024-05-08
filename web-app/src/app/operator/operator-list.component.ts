import {
  AfterViewInit,
  Component,
  computed,
  ElementRef,
  OnDestroy,
  Signal,
  signal,
  ViewChild,
  WritableSignal
} from '@angular/core';
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
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {HttpQueryFiltersInterface} from "../shared/model/http-query-filters.interface";
import {FormsModule} from "@angular/forms";
import {MatDialog} from "@angular/material/dialog";
import {OperatorDetailsComponent} from "./operator-details.component";
import {UpdateOperatorComponent} from "./update-operator.component";
import {SubscriptionManager} from "../shared/util/subscription-manager";
import {DeleteRecordConfirmationComponent} from "../shared/component/delete-record-confirmation.component";

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
    { provide: MatPaginatorIntl, useClass: PaginatorLocalizerService }
  ],
  templateUrl: './operator-list.component.html',
  styleUrl: './operator-list.component.css'
})
export class OperatorListComponent implements AfterViewInit, OnDestroy {

  private subscriptions: SubscriptionManager = new SubscriptionManager();
  private readonly data: Subject<OperatorPageResponseInterface> = new Subject<OperatorPageResponseInterface>();
  protected readonly dataSource: OperatorPageDataSource = new OperatorPageDataSource(this.data);
  protected readonly columnsDef: string[] = ['name'];
  protected readonly rowsDef: string[] = ['name', 'options'];
  protected pageDef = signal({ pageNumber: 1, pageSize: 10 });
  protected nameFilter:WritableSignal<string> = signal('');
  private filters: Signal<HttpQueryFiltersInterface> = computed(() => {
    return {
      'name': this.nameFilter(),
      'pageNumber': this.pageDef().pageNumber,
      'pageSize': this.pageDef().pageSize
    };
  });
  @ViewChild('filter') filter!: ElementRef;

  constructor(
    private http: OperatorHttpService,
    private snackbarService: SnackbarService,
    private dialog: MatDialog,
  ) {
    this.http.getOperatorPage(this.filters()).subscribe(response => {
      this.data.next(response);
    })
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribeAll();
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
    const dialogRef = this.dialog.open(
      DeleteRecordConfirmationComponent, {
        data: {
          codeForTranslation: 'delete-operator'
        }
      });
    this.subscriptions.add(dialogRef.componentInstance.deleteConfirmation.subscribe(value => {
      if (value) {
        dialogRef.close();
        this.deleteOperator(element);
      }
    }));
  }

  private deleteOperator(element: OperatorDataInterface) {
    this.http.delete(element.id).subscribe(response => {
      this.snackbarService.openTopCenterSnackbar(response.message);
      this.onPageChange({ pageIndex: this.pageDef().pageNumber - 1, pageSize: this.pageDef().pageSize, previousPageIndex: 1, length: 1 })
    })
  }

  onEdit(element: OperatorDataInterface) {
    this.http.getDetails(element.id).subscribe(response => {
      const dialogRef = this.dialog.open(
        UpdateOperatorComponent, {
          data: response,
          disableClose: true
        });
      this.subscriptions.add(dialogRef.componentInstance.updateConfirmation.subscribe(value => {
        if (value) {
          dialogRef.close();
          this.onPageChange({
            pageIndex: this.pageDef().pageNumber - 1,
            pageSize: this.pageDef().pageSize,
            previousPageIndex: 1,
            length: 1
          });
        }
      }));
    });
  }

  onDetails(element: OperatorDataInterface) {
    this.http.getDetails(element.id).subscribe(response => {
      this.dialog.open(OperatorDetailsComponent, { data: response })
    })
  }
}
