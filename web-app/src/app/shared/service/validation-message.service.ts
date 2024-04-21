import {Injectable} from '@angular/core';
import {FormControl} from "@angular/forms";
import * as messages from '../../../assets/validation-messages.json'
import {ValidationMessages} from "../model/validation-messages.interface";

@Injectable({
  providedIn: 'root'
})
export class ValidationMessageService {

  private validationMessages: ValidationMessages = messages as ValidationMessages;

  getMessage(key: string, control: FormControl): string {
    return this.validationMessages.validationMessages.find((message) => {
      let path: string = '';
      if(message.key.includes(key)){
        path = message.key.replace(key + ".", '');
      } else {
        return false;
      }
      return control.errors?.[path];
    })?.message || key;
  }
}
