import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {SERVER_URL} from "../shared/http-config";
import {GetAllDocumentTypesResponse} from "./document-dtos";

@Injectable({providedIn: "root"})
export class DocumentHttpService {

  constructor(
    private http: HttpClient
  ) {
  }

  getAllDocumentTypes(): Observable<GetAllDocumentTypesResponse> {
    return this.http.get<GetAllDocumentTypesResponse>(
      SERVER_URL + 'api/document/types'
    );
  }
}
