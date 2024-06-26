import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {CreateNewUserRequestInterface} from "../model/create-new-user-request.interface";
import {CreateNewUserResponseInterface} from "../model/create-new-user-response.interface";
import {Observable,} from "rxjs";
import {GetAllRolesResponseInterface} from "../model/get-all-roles-response.interface";
import {UserExistsResponseInterface} from "../model/user-exists-response.interface";
import {UserListResponseInterface} from "../model/user-list-response.interface";
import {GetUseDetailsResponseInterface} from "../model/get-use-details-response.interface";
import {DeleteUserByIdResponseInterface} from "../model/delete-user-by-id-response.interface";
import {SERVER_URL} from "../../shared/http-config";
import {
  ChangeUserPasswordRequest,
  ChangeUserPasswordResponse,
  LoginResponse,
  ResetUserPasswordRequest,
  ResetUserPasswordResponse
} from "../model/user-dtos";
import {map} from "rxjs/operators";

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

  getIsUserExists(email: string): Observable<UserExistsResponseInterface> {
    return this.http.get<UserExistsResponseInterface>(
      SERVER_URL + 'api/user/exists',
      {
        params: new HttpParams()
          .append('email', email)
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

  resetPassword(request: ResetUserPasswordRequest): Observable<ResetUserPasswordResponse> {
    return this.http.patch<ResetUserPasswordResponse>(
      SERVER_URL + 'api/user/reset-password', request
    );
  }

  login(email: string, password: string): Observable<LoginResponse | null> {
    return this.http.post<LoginResponse>(
      SERVER_URL + 'api/user/login', {}, {
        headers: new HttpHeaders({
          'Authorization': 'Basic ' + btoa(`${email}:${password}`)
        })
      }
    ).pipe(
      map(response => <LoginResponse>{ ...response ,expires: new Date(response.expires) })
    );
  }

  logout(): Observable<void> {
    return this.http.post<void>(
      SERVER_URL + 'api/user/logout', {}
    );
  }

  changePassword(request: ChangeUserPasswordRequest): Observable<ChangeUserPasswordResponse> {
    return this.http.patch<ChangeUserPasswordResponse>(SERVER_URL + 'api/user/change-password', request)
  }
}
