import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {ServiceProviderCreateNewRequestInterface} from "../model/service-provider-create-new-request.interface";
import {Observable} from "rxjs";
import {ServiceProviderCreateNewResponseInterface} from "../model/service-provider-create-new-response.interface";
import {ServiceProviderExistenceResponseInterface} from "../model/service-provider-existence-response.interface";
import {UserExistsResponseInterface} from "../../user/model/user-exists-response.interface";
import {ServiceProviderNameResponseInterface} from "../model/service-provider-name-response.interface";
import {ServiceProviderListResponseInterface} from "../model/service-provider-list-response.interface";
import {ServiceProviderDetailsResponseInterface} from "../model/service-provider-details-response.interface";
import {DeleteServiceProviderResponseInterface} from "../model/delete-service=provider-response.interface";

@Injectable({ providedIn: "root" })
export class ServiceProviderHttpService {

  constructor(private http: HttpClient) {
  }

  createNew(body: ServiceProviderCreateNewRequestInterface): Observable<ServiceProviderCreateNewResponseInterface> {
    return this.http.post<ServiceProviderCreateNewResponseInterface>(
      'http://localhost:8080/api/service-provider',
      body
    )
  }

  getIsProviderExists(idKind: string, id: string): Observable<ServiceProviderExistenceResponseInterface> {
    return this.http.get<UserExistsResponseInterface>(
      'http://localhost:8080/api/service-provider/exists',
      {
        params: new HttpParams()
          .append('kindOfId', idKind)
          .append('id', id)
      }
    );
  }

  getProviderNameFromBur(nip: string): Observable<ServiceProviderNameResponseInterface> {
    return this.http.get<ServiceProviderNameResponseInterface>(
      'http://localhost:8080/api/service-provider/name',
      {
        params: new HttpParams()
          .append('nip', nip)
      }
    );
  }

  getProviderPage(pageNumber: number, pageSize: number) {
    return this.http.get<ServiceProviderListResponseInterface>(
      'http://localhost:8080/api/service-provider',
      {
        params: new HttpParams()
          .append('pageNumber', pageNumber)
          .append('pageSize', pageSize)
      }
    )
  };

  getDetails(id: string): Observable<ServiceProviderDetailsResponseInterface> {
    return this.http.get<ServiceProviderDetailsResponseInterface>(
      'http://localhost:8080/api/service-provider/' + id
    );
  }

  delete(id: string): Observable<DeleteServiceProviderResponseInterface> {
    return this.http.delete<DeleteServiceProviderResponseInterface>(
      'http://localhost:8080/api/service-provider/' + id
    );
  }
}
