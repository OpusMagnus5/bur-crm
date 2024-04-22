import {Injectable} from '@angular/core';
import {FormControl} from "@angular/forms";
import {TranslateService} from "@ngx-translate/core";

@Injectable({
  providedIn: 'root'
})
export class ValidationMessageService {

  constructor(private translate: TranslateService) {
  }

  getMessage(control: FormControl,
             messageKey: (fieldName: string, validation: string) => string,
             fieldName: string): string {
    if (control.errors) {
      const error: string = Object.getOwnPropertyNames(control.errors)[0];
      const key: string = messageKey(fieldName, error);
      return this.translate.instant(key);
    }
    return 'Translation error';
  }
}
