import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {Observable} from "rxjs";
import {SERVER_URL} from "../shared/http-config";
import {AddNewFilesResponse, DeleteDocumentsResponse, GetAllDocumentTypesResponse} from "./document-dtos";
import {FileDownloadService} from "../shared/service/file-download.service";

@Injectable({providedIn: "root"})
export class DocumentHttpService {

  constructor(
    private http: HttpClient,
    private fileDownloadService: FileDownloadService
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

  getDocuments(documentIds: string[]): void {
    let params = new HttpParams();
    documentIds.forEach(item => params = params.append('ids', item));

    const observable = this.http.get<Blob>(SERVER_URL + 'api/document', {
      params: params,
      observe: 'response',
      responseType: 'blob' as 'json'
    });

    this.fileDownloadService.downloadDocuments(observable);
  }

  deleteDocuments(documentIds: string[]): Observable<DeleteDocumentsResponse> {
    let params = new HttpParams();
    documentIds.forEach(item => params = params.append('ids', item));

    return this.http.delete<DeleteDocumentsResponse>(SERVER_URL + 'api/document', { params: params });
  }
}
