import {AbstractControl, ValidationErrors} from "@angular/forms";

export class NipUtils {

  public static validateNip(control: AbstractControl): ValidationErrors | null {
    if (!control.value) {
      return { 'incorrect': true };
    }
    const nip = control.value as string;

    const weight = [6, 5, 7, 2, 3, 4, 5, 6, 7];
    let sum = 0;
    const controlNumber = parseInt(nip.substring(9, 10));
    const weightCount = weight.length;
    for (let i = 0; i < weightCount; i++) {
      sum += (parseInt(nip.substring(i, i + 1)) * weight[i]);
    }

    return sum % 11 == controlNumber ? null : { 'incorrect': true };
  }
}
