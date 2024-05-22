import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {SERVER_URL} from "../shared/http-config";
import {
  CreateNewIntermediaryRequest,
  CreateNewIntermediaryResponse,
  DeleteIntermediaryResponse,
  IntermediaryDetailsResponse,
  IntermediaryExistsResponse,
  IntermediaryPageResponse,
  UpdateIntermediaryRequest,
  UpdateIntermediaryResponse
} from "./intermediary-dtos";
import {UserExistsResponseInterface} from "../user/model/user-exists-response.interface";
import {HttpQueryFiltersInterface} from "../shared/model/http-query-filters.interface";

@Injectable({providedIn: "root"})
export class IntermediaryHttpService {

  constructor(private http: HttpClient) {
  }

  createNew(body: CreateNewIntermediaryRequest): Observable<CreateNewIntermediaryResponse> {
    return this.http.post<CreateNewIntermediaryResponse>(
      SERVER_URL + 'api/intermediary',
      body
    );
  }

  isClientExists(nip: string): Observable<IntermediaryExistsResponse> {
    return this.http.get<UserExistsResponseInterface>(
      SERVER_URL + 'api/intermediary/exists',
      {
        params: new HttpParams()
          .append('nip', nip)
      }
    );
  }

  getIntermediaryPage(filters: HttpQueryFiltersInterface): Observable<IntermediaryPageResponse> {
    return this.http.get<IntermediaryPageResponse>(
      SERVER_URL + 'api/intermediary',
      {
        params: new HttpParams().appendAll(filters)
      }
    );
  }

  delete(id: string): Observable<DeleteIntermediaryResponse> {
    return this.http.delete<DeleteIntermediaryResponse>(
      SERVER_URL + 'api/intermediary/' + id
    );
  }

  getDetails(id: string): Observable<IntermediaryDetailsResponse> {
    return this.http.get<IntermediaryDetailsResponse>(
      SERVER_URL + 'api/intermediary/' + id
    );
  }

  update(request: UpdateIntermediaryRequest): Observable<UpdateIntermediaryResponse> {
    return this.http.patch<UpdateIntermediaryResponse>(
      SERVER_URL + 'api/intermediary',
      request
    );
  }
}
