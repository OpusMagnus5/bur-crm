import {Component, signal, WritableSignal} from '@angular/core';
import {ServiceHttp} from "./service-http";
import {GetServiceDetailsResponse} from "./service-dtos";
import {ActivatedRoute} from "@angular/router";
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {LocalizedDatePipe} from "../shared/pipe/localized-date.pipe";
import {DocumentViewData} from "../document/document-dtos";
import {DocumentHttpService} from "../document/document-http.service";
import {
  MatAccordion,
  MatExpansionPanel,
  MatExpansionPanelDescription,
  MatExpansionPanelHeader,
  MatExpansionPanelTitle
} from "@angular/material/expansion";
import {MatIcon} from "@angular/material/icon";
import {MatSelectionList} from "@angular/material/list";
import {MatButton} from "@angular/material/button";
import {Observable} from "rxjs";
import {AsyncPipe} from "@angular/common";

@Component({
  selector: 'app-service-details',
  standalone: true,
  imports: [
    TranslateModule,
    LocalizedDatePipe,
    MatAccordion,
    MatExpansionPanel,
    MatExpansionPanelTitle,
    MatExpansionPanelHeader,
    MatIcon,
    MatExpansionPanelDescription,
    MatSelectionList,
    MatButton,
    AsyncPipe
  ],
  templateUrl: './service-details.component.html',
  styleUrl: './service-details.component.css'
})
export class ServiceDetailsComponent {

  protected serviceDetails: WritableSignal<GetServiceDetailsResponse | null> = signal(null);
  protected documentTypes: WritableSignal<DocumentViewData[]> = signal([]);

  constructor(
    private serviceHttp: ServiceHttp,
    private documentHttp: DocumentHttpService,
    private translator: TranslateService,
    private route: ActivatedRoute
  ) {
    const id: string = this.route.snapshot.paramMap.get('id')!;
    this.serviceHttp.getDetails(id).subscribe(item => {
      this.serviceDetails = signal(item);
    });
    this.documentHttp.getAllDocumentTypes().subscribe(item => {
      const documentViewData = item.types.map(item => <DocumentViewData>{opened: false, ...item});
      this.documentTypes.set(documentViewData);
    });
  }

  protected getCoachesNames(): string | undefined {
    return this.serviceDetails()?.coaches?.map(item => item.firstName + ' ' + item.lastName)
      .join(', ')
  }

  protected onFilesSelected(documentType: DocumentViewData, event: Event): void {
    documentType.files = (<HTMLInputElement>event.target).files
  }

  protected getChosenFilesLabel(documentType: DocumentViewData): Observable<string> {
    const files = documentType.files;
    if (files && files.length > 0) {
      return this.translator.get('service-details.files-chosen', { filesNumber: files.length});
    }
    return this.translator.get('service-details.no-file-chosen');
  }

  protected disableSendFilesButton(documentType: DocumentViewData): boolean {
    const files = documentType.files;
    return files == null || files.length < 0;
  }
}
