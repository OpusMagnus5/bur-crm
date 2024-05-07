import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {CreateNewOperatorRequestInterface} from "../model/create-new-operator-request.interface";
import {CreateNewOperatorResponseInterface} from "../model/create-new-operator-response.interface";
import {Observable} from "rxjs";
import {OperatorExistsResponseInterface} from "../model/operator-exists-response.interface";
import {OperatorPageResponseInterface} from "../model/operator-page-response.interface";
import {DeleteOperatorResponseInterface} from "../model/delete-operator-response.interface";
import {SERVER_URL} from "../../shared/http-config";
import {HttpQueryFiltersInterface} from "../../shared/model/http-query-filters.interface";

@Injectable({ providedIn: "root" })
export class OperatorHttpService {

  constructor(private http: HttpClient) {
  }

  createNew(body: CreateNewOperatorRequestInterface): Observable<CreateNewOperatorResponseInterface> {
    return this.http.post<CreateNewOperatorResponseInterface>(
      SERVER_URL + 'api/operator',
      body
    )
  }

  getIsOperatorExists(idKind: string, id: string): Observable<OperatorExistsResponseInterface> {
    return this.http.get<OperatorExistsResponseInterface>(
      SERVER_URL + 'api/operator/exists',
      {
        params: new HttpParams()
          .append('kindOfId', idKind)
          .append('id', id)
      }
    );
  }

  getOperatorPage(filters: HttpQueryFiltersInterface) {
    return this.http.get<OperatorPageResponseInterface>(
      SERVER_URL + 'api/operator',
      {
        params: new HttpParams().appendAll(filters)
      }
    )
  };

  delete(id: string): Observable<DeleteOperatorResponseInterface> {
    return this.http.delete<DeleteOperatorResponseInterface>(
      SERVER_URL + 'api/operator/' + id
    );
  }
}
