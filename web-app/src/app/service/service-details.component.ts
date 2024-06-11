import {Component, signal, WritableSignal} from '@angular/core';
import {ServiceHttp} from "./service-http";
import {GetServiceDetailsResponse} from "./service-dtos";
import {ActivatedRoute} from "@angular/router";
import {TranslateModule} from "@ngx-translate/core";
import {LocalizedDatePipe} from "../shared/pipe/localized-date.pipe";
import {DocumentData, DocumentTypeData} from "../document/document-dtos";
import {DocumentHttpService} from "../document/document-http.service";
import {
  MatAccordion,
  MatExpansionPanel,
  MatExpansionPanelDescription,
  MatExpansionPanelHeader,
  MatExpansionPanelTitle
} from "@angular/material/expansion";
import {MatIcon} from "@angular/material/icon";

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
    MatExpansionPanelDescription
  ],
  templateUrl: './service-details.component.html',
  styleUrl: './service-details.component.css'
})
export class ServiceDetailsComponent {

  protected serviceDetails: WritableSignal<GetServiceDetailsResponse> = signal({} as any);
  protected documentTypes: WritableSignal<DocumentTypeData[]> = signal([]);
  protected documents: WritableSignal<{ [type: string]: DocumentData[] }> = signal({})

  constructor(
    private serviceHttp: ServiceHttp,
    private documentHttp: DocumentHttpService,
    private route: ActivatedRoute
  ) {
    const id: string = this.route.snapshot.paramMap.get('id')!;
    this.serviceHttp.getDetails(id).subscribe(item =>
      this.serviceDetails = signal(item)
    );
    this.documentHttp.getAllDocumentTypes().subscribe(item =>
      this.documentTypes.set(item.types)
    );
  }

  protected getCoachesNames(): string {
    return this.serviceDetails().coaches?.map(item => item.firstName + ' ' + item.lastName)
      .join(', ')
  }
}
