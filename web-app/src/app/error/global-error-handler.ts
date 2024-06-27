import {ErrorHandler, Injectable} from "@angular/core";
import {ErrorHttp} from "./error-http";
import {HttpErrorResponse} from "@angular/common/http";
import {SaveWebErrorRequest} from "./error-dtos";

@Injectable()
export class GlobalErrorHandler implements ErrorHandler {

  constructor(
    private http: ErrorHttp
  ) {
  }

  handleError(error: any): void {
    if (error instanceof HttpErrorResponse) {
      return;
    }
    const request: SaveWebErrorRequest = {
      clazz: error?.name || 'Undefined client error name',
      message: error?.message || 'Undefined client error message',
      stacktrace: error?.stack || 'Undefined client error stacktrace'
    }
    this.http.saveError(request).subscribe();
  }
}
