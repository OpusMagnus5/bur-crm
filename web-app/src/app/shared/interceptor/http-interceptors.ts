import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {SpinnerHttpInterceptor} from "./spinner-http.interceptor";
import {ServerErrorHttpInterceptor} from "./server-error-http.interceptor";

export const httpInterceptors = [
  { provide: HTTP_INTERCEPTORS, useClass: SpinnerHttpInterceptor, multi: true },
  { provide: HTTP_INTERCEPTORS, useClass: ServerErrorHttpInterceptor, multi: true }
]
