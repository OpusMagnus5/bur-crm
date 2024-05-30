import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {SERVER_URL} from "../shared/http-config";
import {
  CreateNewServiceRequest,
  CreateNewServiceResponse,
  GetAllServiceTypesResponse,
  GetServiceFromBurResponse
} from "./service-dtos";

@Injectable({providedIn: "root"})
export class ServiceHttp {

  constructor(private http: HttpClient) {
  }

  getAllServiceTypes(): Observable<GetAllServiceTypesResponse> {
    return this.http.get<GetAllServiceTypesResponse>(
      SERVER_URL + 'api/service/types'
    );
  }

  getServiceFromBur(number: string): Observable<GetServiceFromBurResponse> {
    return this.http.get<GetServiceFromBurResponse>(
      SERVER_URL + 'api/service',
      {
        params: new HttpParams().append('number', number)
      }
    );
  }

  createNew(request: CreateNewServiceRequest): Observable<CreateNewServiceResponse> {
    return this.http.post<CreateNewServiceResponse>(
      SERVER_URL + 'api/service', request
    )
  }
}
