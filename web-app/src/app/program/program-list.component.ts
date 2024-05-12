import {AfterViewInit, Component, computed, ElementRef, Signal, signal, ViewChild, WritableSignal} from '@angular/core';
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
export class ProgramListComponent implements AfterViewInit {

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

  onPageChange(event: PageEvent) {
    this.pageDef.set({ pageNumber: event.pageIndex + 1, pageSize: event.pageSize });
    this.http.getProgramPage(this.filters()).subscribe(response =>{
        this.data.set(response);
      }
    )
  }

  onRemove(element: any) {

  }

  onEdit(element: any) {

  }

  onDetails(element: any) {

  }

  protected getColumnData(element: any, column: string): any {
    const fields: string[] = column.split(".");
    if (fields.length === 2) {
      return element[fields[0]][fields[1]];
    }
    return element[fields[0]];
  }
}
