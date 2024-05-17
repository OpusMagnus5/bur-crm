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
import {UpdateServiceProviderRequestInterface} from "../model/update-service-provider-request.interface";
import {UpdateServiceProviderResponseInterface} from "../model/update-service-provider-response.interface";
import {SERVER_URL} from "../../shared/http-config";

@Injectable({ providedIn: "root" })
export class ServiceProviderHttpService {

  constructor(private http: HttpClient) {
  }

  createNew(body: ServiceProviderCreateNewRequestInterface): Observable<ServiceProviderCreateNewResponseInterface> {
    return this.http.post<ServiceProviderCreateNewResponseInterface>(
      SERVER_URL + 'api/service-provider',
      body
    )
  }

  getIsProviderExists(nip: string): Observable<ServiceProviderExistenceResponseInterface> {
    return this.http.get<UserExistsResponseInterface>(
      SERVER_URL + 'api/service-provider/exists',
      {
        params: new HttpParams()
          .append('nip', nip)
      }
    );
  }

  getProviderNameFromBur(nip: string): Observable<ServiceProviderNameResponseInterface> {
    return this.http.get<ServiceProviderNameResponseInterface>(
      SERVER_URL + 'api/service-provider/name',
      {
        params: new HttpParams()
          .append('nip', nip)
      }
    );
  }

  getProviderPage(pageNumber: number, pageSize: number) {
    return this.http.get<ServiceProviderListResponseInterface>(
      SERVER_URL + 'api/service-provider',
      {
        params: new HttpParams()
          .append('pageNumber', pageNumber)
          .append('pageSize', pageSize)
      }
    )
  };

  getDetails(id: string): Observable<ServiceProviderDetailsResponseInterface> {
    return this.http.get<ServiceProviderDetailsResponseInterface>(
      SERVER_URL + 'api/service-provider/' + id
    );
  }

  delete(id: string): Observable<DeleteServiceProviderResponseInterface> {
    return this.http.delete<DeleteServiceProviderResponseInterface>(
      SERVER_URL + 'api/service-provider/' + id
    );
  }

  update(request: UpdateServiceProviderRequestInterface): Observable<UpdateServiceProviderResponseInterface> {
    return this.http.patch<UpdateServiceProviderResponseInterface>(
      SERVER_URL + 'api/service-provider',
      request
    );
  }
}
