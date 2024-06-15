import {Component, OnDestroy, signal, WritableSignal} from '@angular/core';
import {ServiceHttp} from "./service-http";
import {GetServiceDetailsResponse} from "./service-dtos";
import {ActivatedRoute} from "@angular/router";
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {LocalizedDatePipe} from "../shared/pipe/localized-date.pipe";
import {DocumentType, DocumentTypeViewData, DocumentViewData} from "../document/document-dtos";
import {DocumentHttpService} from "../document/document-http.service";
import {
  MatAccordion,
  MatExpansionPanel,
  MatExpansionPanelDescription,
  MatExpansionPanelHeader,
  MatExpansionPanelTitle
} from "@angular/material/expansion";
import {MatIcon} from "@angular/material/icon";
import {MatListOption, MatSelectionList} from "@angular/material/list";
import {MatButton, MatMiniFabButton} from "@angular/material/button";
import {forkJoin, Observable, of} from "rxjs";
import {AsyncPipe, NgClass} from "@angular/common";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatOption, MatSelect} from "@angular/material/select";
import {AbstractControl, FormControl, FormsModule, ReactiveFormsModule, ValidationErrors} from "@angular/forms";
import {ValidationMessageService} from "../shared/service/validation-message.service";
import {SubscriptionManager} from "../shared/util/subscription-manager";
import {map} from "rxjs/operators";
import {MatCheckbox, MatCheckboxChange} from "@angular/material/checkbox";
import {MatDivider} from "@angular/material/divider";

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
    MatError,
    MatListOption,
    MatCheckbox,
    FormsModule,
    MatDivider,
    MatMiniFabButton
  ],
  templateUrl: './service-details.component.html',
  styleUrl: './service-details.component.css'
})
export class ServiceDetailsComponent implements OnDestroy {

  protected readonly DEFAULT_COACH = 'defaultCoach';

  protected readonly DocumentType = DocumentType;
  protected serviceDetails: WritableSignal<GetServiceDetailsResponse | null> = signal(null);
  protected documentTypes: WritableSignal<DocumentTypeViewData[]> = signal([]);
  protected coachInvoiceController: FormControl<string | null> =
    new FormControl<string>(this.DEFAULT_COACH, [this.validateCoach.bind(this)]);

  private readonly subscriptions = new SubscriptionManager();

  constructor(
    private serviceHttp: ServiceHttp,
    private documentHttp: DocumentHttpService,
    private translator: TranslateService,
    private route: ActivatedRoute,
    private validationMessage: ValidationMessageService
  ) {
    const id: string = this.route.snapshot.paramMap.get('id')!;

    const detailsRequest = this.serviceHttp.getDetails(id);
    const allDocumentTypesRequest = this.documentHttp.getAllDocumentTypes();

    forkJoin(([
      detailsRequest,
      allDocumentTypesRequest
    ])).pipe(
      map(([
        detailsResponse,
        allDocumentTypesResponse
      ]) => {
        return {
          details: detailsResponse,
          allDocumentTypes: allDocumentTypesResponse
        }
      })
    ).subscribe({
      next: responses => {
        this.serviceDetails = signal(responses.details);
        const typeViewData = responses.allDocumentTypes.types.map(item => <DocumentTypeViewData>{
          opened: false,
          documents: responses.details.documents
            .filter(doc => doc.type.value === item.value)
            .map(doc => <DocumentViewData>{...doc, checked: false}),
          ...item,
          checkedAll: false
        });
        this.documentTypes.set(typeViewData);
      }
    });
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribeAll();
  }

  protected getCoachesNames(): string | undefined {
    return this.serviceDetails()?.coaches?.map(item => item.firstName + ' ' + item.lastName)
      .join(', ')
  }

  protected onFilesSelected(documentType: DocumentTypeViewData, event: Event): void {
    documentType.files = (<HTMLInputElement>event.target).files
  }

  protected getChosenFilesLabel(documentType: DocumentTypeViewData): Observable<string> {
    const invalidCoach = this.coachInvoiceController.invalid;
    const files = documentType.files;

    if (invalidCoach && documentType.value === DocumentType.COACH_INVOICE) {
      return of(this.getValidationMessage('coach', this.coachInvoiceController));
    } else if (!this.validateFiles(documentType)) {
      return this.translator.get('service-details.invalid-extension');
    } else if (files && files.length > 0 && this.validateFiles(documentType)) {
      return this.translator.get('service-details.files-chosen', { filesNumber: files.length });
    }

    return this.translator.get('service-details.no-file-chosen');
  }

  protected disableSendFilesButton(documentType: DocumentTypeViewData): boolean {
    const files = documentType.files;
    return files == null || files.length < 1 || !this.validateFiles(documentType) ||
      (documentType.value === DocumentType.COACH_INVOICE && this.coachInvoiceController.invalid);
  }

  protected validateFiles(documentType: DocumentTypeViewData): boolean {
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

  protected uploadFiles(documentType: DocumentTypeViewData) {
    const coachId = this.coachInvoiceController.value === this.DEFAULT_COACH ? null : this.coachInvoiceController.value;
    this.documentHttp.addNewFiles(documentType.files!, documentType.value, this.serviceDetails()?.id!, coachId).subscribe()
  }

  protected getValidationMessage(fieldName: string, control: FormControl): string {
    return this.validationMessage.getMessage(control, this.getValidationMessageKey, fieldName);
  }

  protected onCheckAllDocuments(documentType: DocumentTypeViewData, event: MatCheckboxChange): void {
    documentType.checkedAll = event.checked;
    documentType.documents.forEach(item => item.checked = event.checked);
  }

  protected isAllFileNotChecked(documentType: DocumentTypeViewData): boolean {
    return documentType?.documents?.every(item => !item.checked);
  }

  private validateCoach(control: AbstractControl): ValidationErrors | null {
    if (control.value === this.DEFAULT_COACH) {
      return { 'required': true };
    }
    return null;
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'service-details.validation.' + fieldName + '.' + validation;
  }
}
