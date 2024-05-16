import {Injectable} from "@angular/core";
import {AbstractControl, ValidationErrors} from "@angular/forms";
import {catchError, forkJoin, Observable, of} from "rxjs";
import {map} from "rxjs/operators";
import {ServiceProviderHttpService} from "./service-provider-http.service";

@Injectable()
export class ServiceProviderService {

  constructor(private httpService: ServiceProviderHttpService) {
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
