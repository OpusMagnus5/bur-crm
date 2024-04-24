import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {SpinnerHttpInterceptor} from "./spinner-http.interceptor";

export const httpInterceptors = [
  { provide: HTTP_INTERCEPTORS, useClass: SpinnerHttpInterceptor, multi: true }
]
