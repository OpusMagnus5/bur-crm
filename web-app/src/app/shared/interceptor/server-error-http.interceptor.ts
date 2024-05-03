import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {catchError, Observable} from "rxjs";
import {Injectable} from "@angular/core";
import {MatDialog} from "@angular/material/dialog";
import {HttpErrorComponent} from "../component/http-error.component";
import {HttpErrorResponseInterface} from "../model/http-error-response.interface";

@Injectable({providedIn: "root"})
export class ServerErrorHttpInterceptor implements HttpInterceptor {

  private readonly excludedUrl: string[] = [
    "api/service-provider/exists",
    "api/service-provider/name",
    "api/user/exists"
  ];

  constructor(private dialog: MatDialog) {
  }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(req).pipe(
          catchError(error => {
            if (error instanceof HttpErrorResponse && !this.isExcluded(req)){
              const response = (<HttpErrorResponse> error).error as HttpErrorResponseInterface
              const statusCode: number = error.status
              const dialogRef = this.dialog.open(HttpErrorComponent,
                {
                  data: response,
                  disableClose: true,
                  maxWidth: '50em'
                });
              if (statusCode.toString().startsWith('5') && response.errorId) {
                dialogRef.componentInstance.serverError = true;
              } else if (statusCode.toString().startsWith('4') && response.errorId) {
                dialogRef.componentInstance.clientError = true;
              } else {
                dialogRef.componentInstance.unknownError = true;
              }
            }
            throw error;
          })
        )
    }

  private isExcluded(req: HttpRequest<any>) {
    return this.excludedUrl.some(str => req.url.includes(str));
  }
}
