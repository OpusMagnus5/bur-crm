import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {catchError, Observable} from "rxjs";
import {Injectable} from "@angular/core";
import {MatDialog} from "@angular/material/dialog";
import {HttpErrorComponent} from "../component/http-error.component";
import {HttpErrorResponseInterface} from "../model/http-error-response.interface";

@Injectable({providedIn: "root"})
export class ServerErrorHttpInterceptor implements HttpInterceptor {

  constructor(private dialog: MatDialog) {
  }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(req).pipe(
          catchError(error => {
            if (error instanceof HttpErrorResponse){
              const response = (<HttpErrorResponse> error).error as HttpErrorResponseInterface
              this.dialog.open(HttpErrorComponent, {data: response})
            }
            throw error;
          })
        )
    }

}
