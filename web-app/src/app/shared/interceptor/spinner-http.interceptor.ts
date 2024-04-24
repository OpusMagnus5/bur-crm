import {Injectable} from "@angular/core";
import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {finalize, Observable} from "rxjs";
import {SpinnerService} from "../service/spinner.service";

@Injectable({providedIn: "root"})
export class SpinnerHttpInterceptor implements HttpInterceptor {

  constructor(private spinner: SpinnerService) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.spinner.spin.next(true);

    return next.handle(req).pipe(
      finalize(() => {
        this.spinner.spin.next(false);
      })
    )
  }

}
