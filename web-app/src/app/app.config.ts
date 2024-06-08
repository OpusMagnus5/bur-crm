import {ApplicationConfig, importProvidersFrom} from '@angular/core';
import {provideRouter} from '@angular/router';

import {routes} from './app.routes';
import {provideAnimationsAsync} from '@angular/platform-browser/animations/async';
import {HttpClient, HttpClientModule} from "@angular/common/http";
import {TranslateLoader, TranslateModule} from "@ngx-translate/core";
import {TranslateHttpLoader} from "@ngx-translate/http-loader";
import {registerLocaleData} from "@angular/common";
import localePl from '@angular/common/locales/pl';
import localeEn from '@angular/common/locales/en'
import {httpInterceptors} from "./shared/interceptor/http-interceptors";
import {DateAdapter, MAT_DATE_FORMATS} from "@angular/material/core";
import {CustomDateAdapterService, matDateFormats} from "./shared/service/custom-date-adapter.service";
import {MatPaginatorIntl} from "@angular/material/paginator";
import {PaginatorLocalizerService} from "./shared/service/paginator-localizer.service";

registerLocaleData(localePl, 'pl');
registerLocaleData(localeEn, 'en');

export function HttpLoaderFactory(http: HttpClient) {
  return new TranslateHttpLoader(http);
}

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideAnimationsAsync(),
    importProvidersFrom(
      HttpClientModule,
      TranslateModule.forRoot({
        loader: {
          provide: TranslateLoader,
          useFactory: HttpLoaderFactory,
          deps: [HttpClient]
        },
        defaultLanguage: 'pl'
      })
    ),
    httpInterceptors,
    { provide: DateAdapter, useClass: CustomDateAdapterService },
    { provide: MAT_DATE_FORMATS, useValue: matDateFormats },
    { provide: MatPaginatorIntl, useClass: PaginatorLocalizerService }
  ]
};
