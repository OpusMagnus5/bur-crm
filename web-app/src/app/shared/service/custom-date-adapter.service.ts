import {Injectable} from "@angular/core";
import {MomentDateAdapter} from "@angular/material-moment-adapter";
import {TranslateService} from "@ngx-translate/core";
import moment from 'moment'
import 'moment/locale/pl';

export const matDateFormats = {
  parse: {
    dateInput: ['L'],
  },
  display: {
    dateInput: 'L',
    monthYearLabel: 'MM YYYY',
    dateA11yLabel: 'L',
    monthYearA11yLabel: 'MM YYYY',
  },
}

@Injectable({providedIn: "root"})
export class CustomDateAdapterService extends MomentDateAdapter {

  constructor(
    private translate: TranslateService,
  ) {
    super(translate.currentLang, { useUtc: true });
  }

  public override parse(value: any, parseFormat: string | string[]): moment.Moment | null {
    return moment(value, this.getFormat(), false);
  }

  public override format(date: moment.Moment, displayFormat: string): string {
    return date.locale(this.locale).format(this.getFormat());
  }

  public getFormat(): string {
    if (this.locale === 'pl') {
      return 'DD-MM-YYYY';
    }
    return 'DD/MM/YYYY'
  }
}
