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
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {TranslateModule} from "@ngx-translate/core";
import {HttpQueryFiltersInterface} from "../shared/model/http-query-filters.interface";
import {ProgramPageResponseInterface} from "./model/program-page-response.interface";
import {ProgramListDataSource} from "./program-list-data-source";
import {SnackbarService} from "../shared/service/snackbar.service";
import {MatDialog} from "@angular/material/dialog";
import {ProgramHttpService} from "./service/program-http.service";
import {debounceTime, fromEvent} from "rxjs";
import {DeleteRecordConfirmationComponent} from "../shared/component/delete-record-confirmation.component";
import {SubscriptionManager} from "../shared/util/subscription-manager";
import {ProgramDataInterface} from "./model/program-data-interface";
import {ProgramDetailsComponent} from "./program-details.component";
import {UpdateProgramComponent} from "./update-program.component";

@Component({
  selector: 'app-program-list',
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
  templateUrl: './program-list.component.html'
})
export class ProgramListComponent implements AfterViewInit, OnDestroy {

  private subscriptions = new SubscriptionManager();
  private readonly data: WritableSignal<ProgramPageResponseInterface> = signal({
    programs: [],
    totalPrograms: 0
  });
  protected readonly dataSource: ProgramListDataSource = new ProgramListDataSource(this.data);
  protected readonly columnsDef: string[] = ['name', 'operator.name'];
  protected readonly rowsDef: string[] = ['name', 'operator.name', 'options'];
  protected pageDef = signal({ pageNumber: 1, pageSize: 10 });
  protected nameFilter: WritableSignal<string> = signal('');
  private filters: Signal<HttpQueryFiltersInterface> = computed(() => {
    return {
      'name': this.nameFilter(),
      'pageNumber': this.pageDef().pageNumber,
      'pageSize': this.pageDef().pageSize
    };
  });
  @ViewChild('filter') filter!: ElementRef;

  constructor(
    private http: ProgramHttpService,
    private snackbarService: SnackbarService,
    private dialog: MatDialog,
  ) {
    this.http.getProgramPage(this.filters()).subscribe(response => {
      this.data.set(response);
    });
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
          this.http.getProgramPage(this.filters()).subscribe(response => {
            this.data.set(response);
          });
        }
      });
  }

  protected onPageChange(event: PageEvent) {
    this.pageDef.set({ pageNumber: event.pageIndex + 1, pageSize: event.pageSize });
    this.http.getProgramPage(this.filters()).subscribe(response =>{
        this.data.set(response);
      }
    )
  }

  protected onRemove(element: ProgramDataInterface) {
    const dialogRef = this.dialog.open(
      DeleteRecordConfirmationComponent, {
        data: {
          codeForTranslation: 'delete-program'
        }
      });
    this.subscriptions.add(dialogRef.componentInstance.deleteConfirmation.subscribe(value => {
      if (value) {
        dialogRef.close();
        this.deleteOperator(element);
      }
    }));
  }

  private deleteOperator(element: ProgramDataInterface) {
    this.http.delete(element.id).subscribe(response => {
      this.snackbarService.openTopCenterSnackbar(response.message);
      this.onPageChange({ pageIndex: this.pageDef().pageNumber - 1, pageSize: this.pageDef().pageSize, previousPageIndex: 1, length: 1 })
    })
  }

  onEdit(element: ProgramDataInterface) {
    this.http.getDetails(element.id).subscribe(response => {
      const dialogRef = this.dialog.open(
        UpdateProgramComponent, {
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

  onDetails(element: ProgramDataInterface) {
    this.http.getDetails(element.id).subscribe(response => {
      this.dialog.open(ProgramDetailsComponent, { data: response })
    })
  }

  protected getColumnData(element: any, column: string): any {
    const fields: string[] = column.split(".");
    if (fields.length === 2) {
      return element[fields[0]][fields[1]];
    }
    return element[fields[0]];
  }
}
