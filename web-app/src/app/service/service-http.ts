import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {SERVER_URL} from "../shared/http-config";
import {GetAllServiceTypesResponse} from "./service-dtos";

@Injectable({providedIn: "root"})
export class ServiceHttp {

  constructor(private http: HttpClient) {
  }

  getAllServiceTypes(): Observable<GetAllServiceTypesResponse> {
    return this.http.get<GetAllServiceTypesResponse>(
      SERVER_URL + 'api/service/types'
    );
  }
}
