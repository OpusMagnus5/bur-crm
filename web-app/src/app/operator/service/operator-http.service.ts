import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {CreateNewOperatorRequestInterface} from "../model/create-new-operator-request.interface";
import {CreateNewOperatorResponseInterface} from "../model/create-new-operator-response.interface";
import {Observable} from "rxjs";

@Injectable({ providedIn: "root" })
export class OperatorHttpService {

  constructor(private http: HttpClient) {
  }

  createNew(body: CreateNewOperatorRequestInterface): Observable<CreateNewOperatorResponseInterface> {
    return this.http.post<CreateNewOperatorResponseInterface>(
      'http://localhost:8080/api/operator',
      body
    )
  }
}
