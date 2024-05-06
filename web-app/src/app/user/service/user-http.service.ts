import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {CreateNewUserRequestInterface} from "../model/create-new-user-request.interface";
import {CreateNewUserResponseInterface} from "../model/create-new-user-response.interface";
import {Observable} from "rxjs";
import {GetAllRolesResponseInterface} from "../model/get-all-roles-response.interface";
import {UserExistsResponseInterface} from "../model/user-exists-response.interface";
import {UserListResponseInterface} from "../model/user-list-response.interface";
import {GetUseDetailsResponseInterface} from "../model/get-use-details-response.interface";
import {DeleteUserByIdResponseInterface} from "../model/delete-user-by-id-response.interface";
import {SERVER_URL} from "../../shared/http-config";

@Injectable({
  providedIn: 'root'
})
export class UserHttpService {

  constructor(private http: HttpClient) {
  }

  createNew(body: CreateNewUserRequestInterface): Observable<CreateNewUserResponseInterface> {
    return this.http.post<CreateNewUserResponseInterface>(
      SERVER_URL + 'api/user',
      body
    );
  }

  getAllRoles(): Observable<GetAllRolesResponseInterface> {
    return this.http.get<GetAllRolesResponseInterface>(
      SERVER_URL + 'api/user/allRoles'
    );
  }

  getIsUserExists(idKind: string, id: string): Observable<UserExistsResponseInterface> {
    return this.http.get<UserExistsResponseInterface>(
      SERVER_URL + 'api/user/exists',
      {
        params: new HttpParams()
          .append('kindOfId', idKind)
          .append('id', id)
      }
    );
  }

  getUserPage(pageNumber: number, pageSize: number): Observable<UserListResponseInterface> {
    return this.http.get<UserListResponseInterface>(
      SERVER_URL + 'api/user',
      {
        params: new HttpParams()
          .append('pageNumber', pageNumber)
          .append('pageSize', pageSize)
      }
    );
  }

  getUserDetails(userId: string): Observable<GetUseDetailsResponseInterface> {
    return this.http.get<GetUseDetailsResponseInterface>(
      SERVER_URL + 'api/user/' + userId
    );
  }

  deleteUser(userId: string): Observable<DeleteUserByIdResponseInterface> {
    return this.http.delete<DeleteUserByIdResponseInterface>(
      SERVER_URL + 'api/user/' + userId
    );
  }
}
