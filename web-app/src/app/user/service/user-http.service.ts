import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {CreateNewUserRequestInterface} from "../model/create-new-user-request.interface";
import {CreateNewUserResponseInterface} from "../model/create-new-user-response.interface";

@Injectable({
  providedIn: 'root'
})
export class UserHttpService {

  constructor(private http: HttpClient) {
  }

  createNew(body: CreateNewUserRequestInterface): CreateNewUserResponseInterface {
    let responseBody;
    this.http.post<CreateNewUserResponseInterface>(
      'http://localhost:8080/api/user',
      body
    )
  }
}
