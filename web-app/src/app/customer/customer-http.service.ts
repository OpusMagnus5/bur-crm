import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {SERVER_URL} from "../shared/http-config";
import {CreateNewCustomerRequest, CreateNewCustomerResponse, CustomerExistsResponse} from "./customer-dtos";
import {UserExistsResponseInterface} from "../user/model/user-exists-response.interface";

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
}
