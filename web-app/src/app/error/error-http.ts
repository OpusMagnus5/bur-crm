import {HttpClient} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {SaveWebErrorRequest} from "./error-dtos";
import {Observable} from "rxjs";
import {SERVER_URL} from "../shared/http-config";

@Injectable({ providedIn: "root"})
export class ErrorHttp {

  constructor(
    private http: HttpClient
  ) {
  }

  saveError(request: SaveWebErrorRequest): Observable<void> {
    return this.http.post<void>(SERVER_URL + 'api/error', request);
  }
}
