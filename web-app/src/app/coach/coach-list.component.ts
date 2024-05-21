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
import {CoachData, CoachPageResponse} from "./coach-dtos";
import {CoachListDataSource} from "./coach-list-data-source";
import {HttpQueryFiltersInterface} from "../shared/model/http-query-filters.interface";
import {CoachHttpService} from "./coach-http.service";
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
import {MatDialog} from "@angular/material/dialog";
import {SnackbarService} from "../shared/service/snackbar.service";

@Component({
  selector: 'app-coach-list',
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
  templateUrl: './coach-list.component.html'
})
export class CoachListComponent implements AfterViewInit, OnDestroy {

  private subscriptions = new SubscriptionManager();
  private readonly data: WritableSignal<CoachPageResponse> = signal({
    coaches: [],
    totalCoaches: 0
  });
  protected readonly dataSource: CoachListDataSource = new CoachListDataSource(this.data);
  protected readonly columnsDef: string[] = ['firstName', 'lastName', 'pesel'];
  protected readonly rowsDef: string[] = ['firstName', 'lastName', 'pesel', 'options'];
  protected pageDef = signal({ pageNumber: 1, pageSize: 10 });
  protected firstNameFilter: WritableSignal<string> = signal('');
  protected lastNameFilter: WritableSignal<string> = signal('');
  private filters: Signal<HttpQueryFiltersInterface> = computed(() => {
    return {
      'firstName': this.firstNameFilter().trim().toLowerCase(),
      'lastName': this.lastNameFilter().trim().toLowerCase(),
      'pageNumber': this.pageDef().pageNumber,
      'pageSize': this.pageDef().pageSize
    };
  });
  @ViewChild('firstNameFilterRef') firstNameFilterRef!: ElementRef;
  @ViewChild('lastNameFilterRef') lastNameFilterRef!: ElementRef;

  constructor(
    private coachHttp: CoachHttpService,
    private dialog: MatDialog,
    private snackbarService: SnackbarService
  ) {
    this.coachHttp.getCoachPage(this.filters()).subscribe(response => {
      this.data.set(response);
    });
  }


  ngOnDestroy(): void {
    this.subscriptions.unsubscribeAll();
  }
  ngAfterViewInit(): void {
    const firstNameEvent = fromEvent(this.firstNameFilterRef.nativeElement, 'input');
    const lastNameEvent = fromEvent(this.lastNameFilterRef.nativeElement, 'input');
    merge(firstNameEvent, lastNameEvent)
      .pipe(debounceTime(1000))
      .subscribe({
        next: () => {
          this.pageDef().pageNumber = 1;
          this.coachHttp.getCoachPage(this.filters()).subscribe(response => {
            this.data.set(response);
          });
        }
      });
  }

  protected onPageChange(event: PageEvent) {
    this.pageDef.set({ pageNumber: event.pageIndex + 1, pageSize: event.pageSize });
    this.coachHttp.getCoachPage(this.filters()).subscribe(response =>{
        this.data.set(response);
      }
    )
  }

  protected onRemove(element: CoachData) {
    const dialogRef = this.dialog.open(
      DeleteRecordConfirmationComponent, {
        data: { codeForTranslation: 'delete-coach' }
      });
    this.subscriptions.add(dialogRef.componentInstance.deleteConfirmation.subscribe(value => {
      if (value) {
        dialogRef.close();
        this.deleteCoach(element);
      }
    }));
  }

  private deleteCoach(element: CoachData) {
    this.coachHttp.delete(element.id).subscribe(response => {
      this.snackbarService.openTopCenterSnackbar(response.message);
      this.onPageChange({ pageIndex: this.pageDef().pageNumber - 1, pageSize: this.pageDef().pageSize, previousPageIndex: 1, length: 1 })
    })
  }

  protected onEdit(element: CoachData) {
  }

  protected onDetails(element: CoachData) {
  }
}
