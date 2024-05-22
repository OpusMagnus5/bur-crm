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
import {SubscriptionManager} from "../shared/util/subscription-manager";
import {IntermediaryListDataSource} from "./intermediary-list-data-source";
import {HttpQueryFiltersInterface} from "../shared/model/http-query-filters.interface";
import {IntermediaryHttpService} from "./intermediary-http.service";
import {debounceTime, fromEvent, merge} from "rxjs";
import {MatPaginator, PageEvent} from "@angular/material/paginator";
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
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {MatInput} from "@angular/material/input";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {TranslateModule} from "@ngx-translate/core";
import {DeleteRecordConfirmationComponent} from "../shared/component/delete-record-confirmation.component";
import {SnackbarService} from "../shared/service/snackbar.service";
import {MatDialog} from "@angular/material/dialog";
import {IntermediaryDetailsComponent} from "./intermediary-details.component";
import {UpdateIntermediaryComponent} from "./update-intermediary.component";
import {IntermediaryData, IntermediaryPageResponse} from "./intermediary-dtos";

@Component({
  selector: 'app-intermediary-list',
  standalone: true,
  imports: [
    MatCell,
    MatCellDef,
    MatColumnDef,
    MatFormField,
    MatHeaderCell,
    MatHeaderRow,
    MatHeaderRowDef,
    MatIcon,
    MatIconButton,
    MatInput,
    MatLabel,
    MatMenu,
    MatMenuItem,
    MatPaginator,
    MatRow,
    MatRowDef,
    MatTable,
    ReactiveFormsModule,
    TranslateModule,
    FormsModule,
    MatHeaderCellDef,
    MatMenuTrigger,
    MatNoDataRow
  ],
  templateUrl: './intermediary-list.component.html'
})
export class IntermediaryListComponent implements OnDestroy, AfterViewInit {

  private subscriptions = new SubscriptionManager();
  private readonly data: WritableSignal<IntermediaryPageResponse> = signal({
    intermediaries: [],
    totalIntermediaries: 0
  });
  protected readonly dataSource: IntermediaryListDataSource = new IntermediaryListDataSource(this.data);
  protected readonly columnsDef: string[] = ['name', 'nip'];
  protected readonly rowsDef: string[] = ['name', 'nip', 'options'];
  protected pageDef = signal({ pageNumber: 1, pageSize: 10 });
  protected nameFilter: WritableSignal<string> = signal('');
  protected nipFilter: WritableSignal<string> = signal('');
  private filters: Signal<HttpQueryFiltersInterface> = computed(() => {
    return {
      'nip': this.nipFilter(),
      'name': this.nameFilter(),
      'pageNumber': this.pageDef().pageNumber,
      'pageSize': this.pageDef().pageSize
    };
  });
  @ViewChild('nameFilterRef') nameFilterRef!: ElementRef;
  @ViewChild('nipFilterRef') nipFilterRef!: ElementRef;

  constructor(
    private intermediaryHttp: IntermediaryHttpService,
    private snackbarService: SnackbarService,
    private dialog: MatDialog
  ) {
    this.intermediaryHttp.getIntermediaryPage(this.filters()).subscribe(response => {
      this.data.set(response);
    });
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribeAll();
  }

  ngAfterViewInit(): void {
    const nameEvent = fromEvent(this.nameFilterRef.nativeElement, 'input');
    const nipEvent = fromEvent(this.nipFilterRef.nativeElement, 'input');
    merge(nameEvent, nipEvent)
      .pipe(debounceTime(1000))
      .subscribe({
        next: () => {
          this.pageDef().pageNumber = 1;
          this.intermediaryHttp.getIntermediaryPage(this.filters()).subscribe(response => {
            this.data.set(response);
          });
        }
      });
  }

  protected onPageChange(event: PageEvent) {
    this.pageDef.set({ pageNumber: event.pageIndex + 1, pageSize: event.pageSize });
    this.intermediaryHttp.getIntermediaryPage(this.filters()).subscribe(response =>{
        this.data.set(response);
      }
    )
  }

  protected onRemove(element: IntermediaryData): void {
    const dialogRef = this.dialog.open(
      DeleteRecordConfirmationComponent, {
        data: { codeForTranslation: 'delete-intermediary' }
      });
    this.subscriptions.add(dialogRef.componentInstance.deleteConfirmation.subscribe(value => {
      if (value) {
        dialogRef.close();
        this.deleteIntermediary(element);
      }
    }));
  }

  private deleteIntermediary(element: IntermediaryData) {
    this.intermediaryHttp.delete(element.id).subscribe(response => {
      this.snackbarService.openTopCenterSnackbar(response.message);
      this.onPageChange({ pageIndex: this.pageDef().pageNumber - 1, pageSize: this.pageDef().pageSize, previousPageIndex: 1, length: 1 })
    })
  }

  protected onEdit(element: IntermediaryData): void {
    this.intermediaryHttp.getDetails(element.id).subscribe(response => {
      const dialogRef = this.dialog.open(
        UpdateIntermediaryComponent, {
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

  protected onDetails(element: IntermediaryData): void {
    this.intermediaryHttp.getDetails(element.id).subscribe(response => {
      this.dialog.open(IntermediaryDetailsComponent, { data: response })
    })
  }
}
