import {Injectable} from "@angular/core";
import {AbstractControl, ValidationErrors} from "@angular/forms";
import {catchError, forkJoin, Observable, of} from "rxjs";
import {map} from "rxjs/operators";
import {ServiceProviderHttpService} from "./service-provider-http.service";

@Injectable()
export class ServiceProviderService {

  constructor(private httpService: ServiceProviderHttpService) {
  }

  validateNip(control: AbstractControl): ValidationErrors | null {
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

  validateNipOccupationAndGetProviderName(nipControl: AbstractControl, nameControl: AbstractControl): Observable<ValidationErrors | null> {
    const isProviderExists = this.httpService.getIsProviderExists(nipControl.value.trim());
    const providerNameFromBur = this.httpService.getProviderNameFromBur(nipControl.value.trim());

    return forkJoin([isProviderExists, providerNameFromBur]).pipe(
      map(([exists, name]) => {
        if (!exists.exists && name.name.length > 0) {
          nameControl.setValue(name.name);
        }
        return exists.exists ? { 'exists': true } : null;
      }),
      catchError(() => of(null))
    );
  }
}
