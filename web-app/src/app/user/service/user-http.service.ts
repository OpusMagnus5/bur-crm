import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {CreateNewUserRequestInterface} from "../model/create-new-user-request.interface";
import {CreateNewUserResponseInterface} from "../model/create-new-user-response.interface";
import {Observable} from "rxjs";
import {GetAllRolesResponseInterface} from "../model/get-all-roles-response.interface";
import {UserExistsResponseInterface} from "../model/user-exists-response.interface";
import {UserListResponseInterface} from "../model/user-list-response.interface";
import {GetUseDetailsResponseInterface} from "../model/get-use-details-response.interface";

@Injectable({
  providedIn: 'root'
})
export class UserHttpService {

  constructor(private http: HttpClient) {
  }

  createNew(body: CreateNewUserRequestInterface): Observable<CreateNewUserResponseInterface> {
    return this.http.post<CreateNewUserResponseInterface>(
      'http://localhost:8080/api/user',
      body
    );
  }

  getAllRoles(): Observable<GetAllRolesResponseInterface> {
    return this.http.get<GetAllRolesResponseInterface>(
      'http://localhost:8080/api/user/allRoles'
    );
  }

  getIsUserExists(idKind: string, id: string): Observable<UserExistsResponseInterface> {
    return this.http.get<UserExistsResponseInterface>(
      'http://localhost:8080/api/user/exists',
      {
        params: new HttpParams()
          .append('kindOfId', idKind)
          .append('id', id)
      }
    );
  }

  getUserPage(pageNumber: number, pageSize: number): Observable<UserListResponseInterface> {
    return this.http.get<UserListResponseInterface>(
      'http://localhost:8080/api/user',
      {
        params: new HttpParams()
          .append('pageNumber', pageNumber)
          .append('pageSize', pageSize)
      }
    );
  }

  getUserDetails(userId: string): Observable<GetUseDetailsResponseInterface> {
    return this.http.get<GetUseDetailsResponseInterface>(
      'http://localhost:8080/api/user/' + userId
    );
  }
}
