import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {SERVER_URL} from "../../shared/http-config";
import {CreateNewProgramRequestInterface} from "../model/create-new-program-request.interface";
import {CreateNewProgramResponseInterface} from "../model/create-new-program-response-interface";

@Injectable({providedIn: "root"})
export class ProgramHttpService {

  constructor(private http: HttpClient) {
  }

  createNew(body: CreateNewProgramRequestInterface): Observable<CreateNewProgramResponseInterface> {
    return this.http.post<CreateNewProgramResponseInterface>(
      SERVER_URL + 'api/program',
      body
    );
  }


}
