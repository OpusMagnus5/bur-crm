import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {SERVER_URL} from "../shared/http-config";
import {CoachExistsResponse, CoachPageResponse, CreateNewCoachRequest, CreateNewCoachResponse} from "./coach-dtos";
import {HttpQueryFiltersInterface} from "../shared/model/http-query-filters.interface";

@Injectable({providedIn: "root"})
export class CoachHttpService {

  constructor(private http: HttpClient) {
  }

  createNew(body: CreateNewCoachRequest): Observable<CreateNewCoachResponse> {
    return this.http.post<CreateNewCoachResponse>(
      SERVER_URL + 'api/coach',
      body
    );
  }

  checkOperatorExistence(pesel: string): Observable<CoachExistsResponse> {
    return this.http.get<CoachExistsResponse>(
      SERVER_URL + 'api/coach/exists',
      {
        params: new HttpParams()
          .append('pesel', pesel)
      }
    );
  }

  getCoachPage(filters: HttpQueryFiltersInterface): Observable<CoachPageResponse> {
    return this.http.get<CoachPageResponse>(
      SERVER_URL + 'api/coach',
      {
        params: new HttpParams().appendAll(filters)
      }
    )
  };
}
