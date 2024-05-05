import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {CreateNewOperatorRequestInterface} from "../model/create-new-operator-request.interface";
import {CreateNewOperatorResponseInterface} from "../model/create-new-operator-response.interface";
import {Observable} from "rxjs";
import {OperatorExistsResponseInterface} from "../model/operator-exists-response.interface";
import {OperatorPageResponseInterface} from "../model/operator-page-response.interface";

@Injectable({ providedIn: "root" })
export class OperatorHttpService {

  constructor(private http: HttpClient) {
  }

  createNew(body: CreateNewOperatorRequestInterface): Observable<CreateNewOperatorResponseInterface> {
    return this.http.post<CreateNewOperatorResponseInterface>(
      'http://localhost:8080/api/operator',
      body
    )
  }

  getIsOperatorExists(idKind: string, id: string): Observable<OperatorExistsResponseInterface> {
    return this.http.get<OperatorExistsResponseInterface>(
      'http://localhost:8080/api/operator/exists',
      {
        params: new HttpParams()
          .append('kindOfId', idKind)
          .append('id', id)
      }
    );
  }

  getOperatorPage(pageNumber: number, pageSize: number) {
    return this.http.get<OperatorPageResponseInterface>(
      'http://localhost:8080/api/operator',
      {
        params: new HttpParams()
          .append('pageNumber', pageNumber)
          .append('pageSize', pageSize)
      }
    )
  };
}
