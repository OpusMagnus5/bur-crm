import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {HttpResponse} from "@angular/common/http";

@Injectable({ providedIn: "root" })
export class FileDownloadService {

  public downloadDocuments(blob: Observable<HttpResponse<Blob>>): void {
    blob.subscribe(response => {
      const contentDisposition = response.headers.get('Content-Disposition');
      let fileName = 'downloaded-file.zip';
      if (contentDisposition) {
        const fileNameMatch = contentDisposition.match('filename=(.+)');
        if (fileNameMatch && fileNameMatch.length > 1) {
          fileName = fileNameMatch[1];
        }
      }

      const url = window.URL.createObjectURL(response.body!);
      const a = document.createElement('a');
      a.href = url;
      a.download = fileName;
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      window.URL.revokeObjectURL(url);
    });
  }
}
