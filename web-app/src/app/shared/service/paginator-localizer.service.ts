import {Injectable} from "@angular/core";
import {TranslateService} from "@ngx-translate/core";
import {MatPaginatorIntl} from "@angular/material/paginator";
import {Subject} from "rxjs";

@Injectable()
export class PaginatorLocalizerService implements MatPaginatorIntl {

  changes: Subject<void> = new Subject<void>();

  itemsPerPageLabel: string = '';
  nextPageLabel: string = '';
  previousPageLabel: string = '';
  firstPageLabel: string = '';
  lastPageLabel: string = '';

  constructor(private translator: TranslateService) {
    this.translator.get('paginator.items-per-page-label').subscribe(text => {
      this.itemsPerPageLabel = text
    });
    this.translator.get('paginator.next-page-label').subscribe(text => {
      this.nextPageLabel = text
    });
    this.translator.get('paginator.previous-page-label').subscribe(text => {
      this.previousPageLabel = text
    });
    this.translator.get('paginator.first-page-label').subscribe(text => {
      this.firstPageLabel = text
    });
    this.translator.get('paginator.last-page-label').subscribe(text => {
      this.lastPageLabel = text
    });
  }

  getRangeLabel(page: number, pageSize: number, length: number): string {
    if (length === 0) {
      return this.translator.instant('paginator.range-label', {first: 0, last: 0, total: 0});
    }
    const endRange = (page + 1) * pageSize > length ? length : (page + 1) * pageSize
    return this.translator.instant('paginator.range-label', {first: page * pageSize + 1, last: endRange, total: length});
  }
}
