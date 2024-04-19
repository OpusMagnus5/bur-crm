import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' }) //dodajemy jeśli chcemy coś wstrzyknąc do tego serwisu, plus mozemy chyba wskazac root zamiast providerowac appconfig
export class AppService {
  constructor(service: AppService) {
    //i jesli chcemy wstrzyknac to przez konstruktor
  }
}
