import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {SpinnerHttpInterceptor} from "./spinner-http.interceptor";
import {ServerErrorHttpInterceptor} from "./server-error-http.interceptor";
import {HttpHeadersInterceptor} from "./http-headers-interceptor";

export const httpInterceptors = [
  { provide: HTTP_INTERCEPTORS, useClass: SpinnerHttpInterceptor, multi: true },
  { provide: HTTP_INTERCEPTORS, useClass: ServerErrorHttpInterceptor, multi: true },
  { provide: HTTP_INTERCEPTORS, useClass: HttpHeadersInterceptor, multi: true }
]
