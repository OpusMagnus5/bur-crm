import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {SERVER_URL} from "../shared/http-config";
import {AddNewFilesResponse, GetAllDocumentTypesResponse} from "./document-dtos";

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

  addNewFiles(files: FileList, fileType: string, serviceId: string, coachId: string | null): Observable<AddNewFilesResponse> {
    const formData = new FormData();
    for (let i = 0; i < files.length; i++) {
      formData.append('files', files[i]);
    }
    formData.append('fileType', fileType);
    formData.append('serviceId', serviceId);
    if (coachId) {
      formData.append('coachId', coachId);
    }
    return this.http.post<AddNewFilesResponse>(
      SERVER_URL + 'api/document', formData
    )
  }
}
