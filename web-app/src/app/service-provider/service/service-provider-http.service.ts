import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {ServiceProviderCreateNewRequestInterface} from "../model/service-provider-create-new-request.interface";
import {Observable} from "rxjs";
import {ServiceProviderCreateNewResponseInterface} from "../model/service-provider-create-new-response.interface";
import {ServiceProviderExistenceResponseInterface} from "../model/service-provider-existence-response.interface";
import {UserExistsResponseInterface} from "../../user/model/user-exists-response.interface";

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
}
