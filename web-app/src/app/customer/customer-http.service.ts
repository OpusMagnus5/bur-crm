import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {SERVER_URL} from "../shared/http-config";
import {
  CreateNewCustomerRequest,
  CreateNewCustomerResponse,
  CustomerDetailsResponse,
  CustomerExistsResponse,
  CustomerPageResponse,
  DeleteCustomerResponse,
  GetAllCustomersResponse,
  UpdateCustomerRequest,
  UpdateCustomerResponse
} from "./customer-dtos";
import {UserExistsResponseInterface} from "../user/model/user-exists-response.interface";
import {HttpQueryFiltersInterface} from "../shared/model/http-query-filters.interface";

@Injectable({providedIn: "root"})
export class CustomerHttpService {

  constructor(private http: HttpClient) {
  }

  createNew(body: CreateNewCustomerRequest): Observable<CreateNewCustomerResponse> {
    return this.http.post<CreateNewCustomerResponse>(
      SERVER_URL + 'api/customer',
      body
    );
  }

  isClientExists(nip: string): Observable<CustomerExistsResponse> {
    return this.http.get<UserExistsResponseInterface>(
      SERVER_URL + 'api/customer/exists',
      {
        params: new HttpParams()
          .append('nip', nip)
      }
    );
  }

  getCustomerPage(filters: HttpQueryFiltersInterface): Observable<CustomerPageResponse> {
    return this.http.get<CustomerPageResponse>(
      SERVER_URL + 'api/customer',
      {
        params: new HttpParams().appendAll(filters)
      }
    );
  }

  delete(id: string): Observable<DeleteCustomerResponse> {
    return this.http.delete<DeleteCustomerResponse>(
      SERVER_URL + 'api/customer/' + id
    );
  }

  getDetails(id: string): Observable<CustomerDetailsResponse> {
    return this.http.get<CustomerDetailsResponse>(
      SERVER_URL + 'api/customer/' + id
    );
  }

  update(request: UpdateCustomerRequest): Observable<UpdateCustomerResponse> {
    return this.http.patch<UpdateCustomerResponse>(
      SERVER_URL + 'api/customer',
      request
    );
  }

  getAll(): Observable<GetAllCustomersResponse> {
    return this.http.get<GetAllCustomersResponse>(
      SERVER_URL + 'api/customer'
    )
  }
}
