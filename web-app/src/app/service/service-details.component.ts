import {Component, signal, WritableSignal} from '@angular/core';
import {ServiceHttp} from "./service-http";
import {GetServiceDetailsResponse} from "./service-dtos";
import {ActivatedRoute} from "@angular/router";
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {LocalizedDatePipe} from "../shared/pipe/localized-date.pipe";
import {DocumentType, DocumentViewData} from "../document/document-dtos";
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
import {AsyncPipe, NgClass} from "@angular/common";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatOption, MatSelect} from "@angular/material/select";
import {FormControl, ReactiveFormsModule, Validators} from "@angular/forms";
import {ValidationMessageService} from "../shared/service/validation-message.service";

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
    AsyncPipe,
    NgClass,
    MatFormField,
    MatSelect,
    MatOption,
    ReactiveFormsModule,
    MatLabel,
    MatError
  ],
  templateUrl: './service-details.component.html',
  styleUrl: './service-details.component.css'
})
export class ServiceDetailsComponent {

  protected readonly DocumentType = DocumentType;
  protected serviceDetails: WritableSignal<GetServiceDetailsResponse | null> = signal(null);
  protected documentTypes: WritableSignal<DocumentViewData[]> = signal([]);
  protected coachInvoiceController: FormControl<string | null> = new FormControl<string| null>(null, [Validators.required])

  constructor(
    private serviceHttp: ServiceHttp,
    private documentHttp: DocumentHttpService,
    private translator: TranslateService,
    private route: ActivatedRoute,
    private validationMessage: ValidationMessageService
  ) {
    const id: string = this.route.snapshot.paramMap.get('id')!;
    this.serviceHttp.getDetails(id).subscribe(item => {
      this.serviceDetails = signal(item);
    });
    this.documentHttp.getAllDocumentTypes().subscribe(item => {
      const documentViewData = item.types.map(item => <DocumentViewData>{
        opened: false,
        ...item
      });
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
    if (files && files.length > 0 && this.validateFiles(documentType)) {
      return this.translator.get('service-details.files-chosen', { filesNumber: files.length });
    } else if (!this.validateFiles(documentType)) {
      return this.translator.get('service-details.invalid-extension');
    }
    return this.translator.get('service-details.no-file-chosen');
  }

  protected disableSendFilesButton(documentType: DocumentViewData): boolean {
    const files = documentType.files;
    return files == null || files.length < 1 || !this.validateFiles(documentType) ||
      (documentType.value === DocumentType.COACH_INVOICE && !this.coachInvoiceController.value);
  }

  protected validateFiles(documentType: DocumentViewData): boolean {
    const files = documentType.files;
    if (!files) {
      return true;
    }
    for (let i = 0; i < files.length; i++) {
      if (!files.item(i)?.name?.includes('.pdf')){
        return false;
      }
    }
    return true;
  }

  protected uploadFiles(documentType: DocumentViewData) {
    this.documentHttp.addNewFiles(documentType.files!, documentType.value, this.serviceDetails()?.id!, this.coachInvoiceController.value).subscribe()
  }

  protected getValidationMessage(fieldName: string, control: FormControl): string {
    return this.validationMessage.getMessage(control, this.getValidationMessageKey, fieldName);
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'service-details.validation.' + fieldName + '.' + validation;
  }
}
