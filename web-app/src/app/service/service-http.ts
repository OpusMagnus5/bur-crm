import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {SERVER_URL} from "../shared/http-config";
import {
  CreateNewServiceResponse,
  CreateOrUpdateServiceRequest,
  GetAllServiceStatusesResponse,
  GetAllServiceTypesResponse,
  GetServiceDetailsResponse,
  GetServiceFromBurResponse,
  ServicePageResponse
} from "./service-dtos";
import {HttpQueryFiltersInterface} from "../shared/model/http-query-filters.interface";

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

  createNew(request: CreateOrUpdateServiceRequest): Observable<CreateNewServiceResponse> {
    return this.http.post<CreateNewServiceResponse>(
      SERVER_URL + 'api/service', request
    )
  }

  getServicePage(filters: HttpQueryFiltersInterface): Observable<ServicePageResponse> {
    return this.http.get<ServicePageResponse>(
      SERVER_URL + 'api/service',
      {
        params: new HttpParams().appendAll(filters)
      }
    );
  }

  getDetails(id: string): Observable<GetServiceDetailsResponse> {
    return this.http.get<GetServiceDetailsResponse>(SERVER_URL + 'api/service/' + id);
  }

  getAllStatuses(): Observable<GetAllServiceStatusesResponse> {
    return this.http.get<GetAllServiceStatusesResponse>(SERVER_URL + 'api/service/statuses')
  }
}
