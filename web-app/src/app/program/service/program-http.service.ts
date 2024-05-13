import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {SERVER_URL} from "../../shared/http-config";
import {CreateNewProgramRequestInterface} from "../model/create-new-program-request.interface";
import {CreateNewProgramResponseInterface} from "../model/create-new-program-response-interface";
import {ProgramExistsResponseInterface} from "../model/program-exists-response.interface";
import {HttpQueryFiltersInterface} from "../../shared/model/http-query-filters.interface";
import {ProgramPageResponseInterface} from "../model/program-page-response.interface";
import {DeleteOperatorResponseInterface} from "../../operator/model/delete-operator-response.interface";

@Injectable({providedIn: "root"})
export class ProgramHttpService {

  constructor(private http: HttpClient) {
  }

  createNew(body: CreateNewProgramRequestInterface): Observable<CreateNewProgramResponseInterface> {
    return this.http.post<CreateNewProgramResponseInterface>(
      SERVER_URL + 'api/program',
      body
    );
  }

  getIsOperatorExists(idKind: string, id: string): Observable<ProgramExistsResponseInterface> {
    return this.http.get<ProgramExistsResponseInterface>(
      SERVER_URL + 'api/program/exists',
      {
        params: new HttpParams()
          .append('kindOfId', idKind)
          .append('id', id)
      }
    );
  }

  getProgramPage(filters: HttpQueryFiltersInterface): Observable<ProgramPageResponseInterface> {
    return this.http.get<ProgramPageResponseInterface>(
      SERVER_URL + 'api/program',
      {
        params: new HttpParams().appendAll(filters)
      }
    )
  };

  delete(id: string): Observable<DeleteOperatorResponseInterface> {
    return this.http.delete<DeleteOperatorResponseInterface>(
      SERVER_URL + 'api/program/' + id
    );
  }
}
