import {Component, OnDestroy, signal, WritableSignal} from '@angular/core';
import {ServiceHttp} from "./service-http";
import {BadgeMessageData, GetServiceDetailsResponse, ServiceType, ServiceTypeData} from "./service-dtos";
import {ActivatedRoute} from "@angular/router";
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {LocalizedDatePipe} from "../shared/pipe/localized-date.pipe";
import {
  BadgeMessageType,
  DocumentType,
  DocumentTypeData,
  DocumentTypeViewData,
  DocumentViewData,
  GetAllDocumentTypesResponse
} from "../document/document-dtos";
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
import {MatTooltip} from "@angular/material/tooltip";
import {SnackbarService} from "../shared/service/snackbar.service";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {DeleteRecordConfirmationComponent} from "../shared/component/delete-record-confirmation.component";

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
    MatMiniFabButton,
    MatTooltip
  ],
  templateUrl: './service-details.component.html',
  styleUrl: './service-details.component.css'
})
export class ServiceDetailsComponent implements OnDestroy {

  protected readonly DEFAULT_COACH = 'defaultCoach';
  private readonly MB_50_SIZE = 52428800;
  private readonly serviceId: string;

  protected readonly DocumentType = DocumentType;
  protected serviceDetails: WritableSignal<GetServiceDetailsResponse | null> = signal(null);
  protected documentTypeViewData: WritableSignal<DocumentTypeViewData[]> = signal([]);
  protected coachInvoiceController: FormControl<string | null> =
    new FormControl<string>(this.DEFAULT_COACH, [this.validateCoach.bind(this)]);
  protected badgeMessageForServiceStatus: WritableSignal<string> = signal('');

  private readonly subscriptions = new SubscriptionManager();

  constructor(
    private serviceHttp: ServiceHttp,
    private documentHttp: DocumentHttpService,
    private translator: TranslateService,
    private route: ActivatedRoute,
    private validationMessage: ValidationMessageService,
    private snackBarService: SnackbarService,
    private dialog: MatDialog,
  ) {
    this.serviceId = this.route.snapshot.paramMap.get('id')!;
    this.getDataFromServer();
  }

  private getDataFromServer() {
    const detailsRequest = this.serviceHttp.getDetails(this.serviceId);
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
        const typeViewData = this.buildDocumentTypeViewData(responses);
        this.documentTypeViewData.set(typeViewData);
        this.badgeMessageForServiceStatus.set(responses.details.badgeMessages.filter(statBadge =>
          statBadge.type === BadgeMessageType.NOT_COMPLETE_SERVICE
        ).map(statBadge => statBadge.message).join('\n'));
      }
    });
  }

  private buildDocumentTypeViewData(responses: { allDocumentTypes: GetAllDocumentTypesResponse; details: GetServiceDetailsResponse }) {
    return responses.allDocumentTypes.types
      .filter(docType => this.isDocumentTypeForServiceType(docType, responses.details.type))
      .map(docType => <DocumentTypeViewData>{
        opened: false,
        documents: responses.details.documents
          .filter(doc => doc.type.value === docType.value)
          .map(doc => <DocumentViewData>{...doc, checked: false}),
        ...docType,
        checkedAll: false,
        badgeMessages: responses.details.badgeMessages
          .filter(badge => this.isBadgeMessageForDocumentType(badge, docType))
          .map(badge => badge.message)
          .join('\n')
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
    documentType.filesToSend = (<HTMLInputElement>event.target).files
  }

  protected getChosenFilesLabel(documentType: DocumentTypeViewData): Observable<string> {
    const invalidCoach = this.coachInvoiceController.invalid;
    const files = documentType.filesToSend;

    if (invalidCoach && documentType.value === DocumentType.COACH_INVOICE) {
      return of(this.getValidationMessage('coach', this.coachInvoiceController));
    } else if (this.isIncorrectQuantityDocumentsToAdd(documentType, files?.length)) {
      return this.translator.get('service-details.to-much-files-for-type');
    } else if (!this.isCorrectSizeOfFiles(files)) {
      return this.translator.get('service-details.to-large-files');
    } else if (!this.validateFiles(documentType)) {
      return this.translator.get('service-details.invalid-extension');
    } else if (files && files.length > 0 && this.validateFiles(documentType)) {
      return this.translator.get('service-details.files-chosen', { filesNumber: files.length });
    }
    return this.translator.get('service-details.no-file-chosen');
  }

  protected disableSendFilesButton(documentType: DocumentTypeViewData): boolean {
    const files = documentType.filesToSend;
    return files == null || files.length < 1 || !this.validateFiles(documentType) ||
      (documentType.value === DocumentType.COACH_INVOICE && this.coachInvoiceController.invalid)
      || this.isIncorrectQuantityDocumentsToAdd(documentType, files?.length)
      || !this.isCorrectSizeOfFiles(files);
  }

  protected validateFiles(documentType: DocumentTypeViewData): boolean {
    const files = documentType.filesToSend;
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
    this.documentHttp.addNewFiles(documentType.filesToSend!, documentType.value, this.serviceDetails()?.id!, coachId)
      .subscribe(item => {
        this.snackBarService.openTopCenterSnackbar(item.message);
        this.getDataFromServer();
      });
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

  protected downloadDocument(document: DocumentViewData) {
    const documentIds: string[] = [document.id]
    this.documentHttp.getDocuments(documentIds);
  }

  protected downloadDocuments(document: DocumentTypeViewData) {
    const documentIds = document.documents
      .filter(item => item.checked)
      .map(item => item.id);
    this.documentHttp.getDocuments(documentIds);
  }

  protected deleteDocuments(document: DocumentTypeViewData) {
    const documentIds = document.documents
      .filter(item => item.checked)
      .map(item => item.id);

    this.deleteDocumentsByIds(documentIds);
  }

  protected deleteDocument(document: DocumentViewData) {
    const documentIds: string[] = [document.id]
    this.deleteDocumentsByIds(documentIds);
  }

  protected downloadAllServiceDocuments() {
    this.documentHttp.getServiceDocuments(this.serviceId);
  }

  protected isUploadFileButtonDisabled(documentType: DocumentTypeViewData) {
    if (this.serviceDetails() === undefined || this.serviceDetails() === null) {
      return false;
    }
    const numberOfParticipants = this.serviceDetails()!.numberOfParticipants;
    const coaches = this.serviceDetails()!.coaches.length;
    const documents = documentType.documents.length;

    if (documents >= 1 && documentType.value === DocumentType.REPORT) {
      return true;
    } else if (documents >= numberOfParticipants  && documentType.value === DocumentType.CONSENT) {
      return true;
    } else if (documents >= coaches && documentType.value === DocumentType.COACH_INVOICE) {
      return true;
    } else if (documents >= 1 && documentType.value === DocumentType.PROVIDER_INVOICE) {
      return true;
    } else if (documents >= 1 && documentType.value === DocumentType.INTERMEDIARY_INVOICE) {
      return true;
    } else if (documents >= numberOfParticipants && documentType.value === DocumentType.PARTICIPANT_BUR_QUESTIONNAIRE) {
      return true;
    } else if (documents >= 1 && documentType.value === DocumentType.CUSTOMER_BUR_QUESTIONNAIRE) {
      return true;
    } else if (documents >= numberOfParticipants && documentType.value === DocumentType.PARTICIPANT_PROVIDER_QUESTIONNAIRE) {
      return true;
    } else if (documents >= 1 && documentType.value === DocumentType.ATTENDANCE_LIST) {
      return true;
    }
    return false;
  }

  private deleteDocumentsByIds(documentIds: string[]) {
    const codeToTranslate: string = documentIds.length > 1 ? 'delete-files' : 'delete-file';
    const dialogRef = this.openDeleteFileConfirmation(codeToTranslate);
    this.subscriptions.add(dialogRef.componentInstance.deleteConfirmation.subscribe(value => {
      if (value) {
        dialogRef.close();
        this.documentHttp.deleteDocuments(documentIds).subscribe(response => {
          this.snackBarService.openTopCenterSnackbar(response.message);
          this.getDataFromServer();
        });
      }
    }));
  }

  private openDeleteFileConfirmation(codeForTranslation: string): MatDialogRef<DeleteRecordConfirmationComponent> {
    return this.dialog.open(
      DeleteRecordConfirmationComponent, {
        data: {
          codeForTranslation: codeForTranslation
        }
      });
  }

  private validateCoach(control: AbstractControl): ValidationErrors | null {
    if (control.value === this.DEFAULT_COACH) {
      return { 'required': true };
    }
    return null;
  }

  private isBadgeMessageForDocumentType(badgeMessage: BadgeMessageData, documentType: DocumentTypeData): boolean {
    if (badgeMessage.type === BadgeMessageType.MISSING_REPORT && documentType.value === DocumentType.REPORT) {
      return true;
    } else if (badgeMessage.type === BadgeMessageType.NOT_ENOUGH_CONSENTS && documentType.value === DocumentType.CONSENT) {
      return true;
    } else if (badgeMessage.type === BadgeMessageType.MISSING_COACH_INVOICE && documentType.value === DocumentType.COACH_INVOICE) {
      return true;
    } else if (badgeMessage.type === BadgeMessageType.MISSING_PROVIDER_INVOICE && documentType.value === DocumentType.PROVIDER_INVOICE) {
      return true;
    } else if (badgeMessage.type === BadgeMessageType.MISSING_INTERMEDIARY_INVOICE && documentType.value === DocumentType.INTERMEDIARY_INVOICE) {
      return true;
    } else if (badgeMessage.type === BadgeMessageType.NOT_ENOUGH_PARTICIPANT_BUR_QUESTIONNAIRES && documentType.value === DocumentType.PARTICIPANT_BUR_QUESTIONNAIRE) {
      return true;
    } else if (badgeMessage.type === BadgeMessageType.MISSING_CUSTOMER_BUR_QUESTIONNAIRE && documentType.value === DocumentType.CUSTOMER_BUR_QUESTIONNAIRE) {
      return true;
    } else if (badgeMessage.type === BadgeMessageType.NOT_ENOUGH_PARTICIPANT_PROVIDER_QUESTIONNAIRE && documentType.value === DocumentType.PARTICIPANT_PROVIDER_QUESTIONNAIRE) {
      return true;
    } else if (badgeMessage.type === BadgeMessageType.MISSING_ATTENDANCE_LIST && documentType.value === DocumentType.ATTENDANCE_LIST) {
      return true;
    } else if (badgeMessage.type === BadgeMessageType.MISSING_PRESENTATION && documentType.value === DocumentType.PRESENTATION) {
      return true;
    }
    return false;
  }

  private isDocumentTypeForServiceType(documentType: DocumentTypeData, serviceType: ServiceTypeData): boolean {
    if (documentType.value === DocumentType.REPORT && serviceType.value != ServiceType.CONSULTING) {
      return false;
    }
    return true;
  }

  private getValidationMessageKey(fieldName: string, validation: string): string {
    return 'service-details.validation.' + fieldName + '.' + validation;
  }

  private isIncorrectQuantityDocumentsToAdd(documentType: DocumentTypeViewData, docToAdd: number = 0) {
    if (this.serviceDetails() === undefined || this.serviceDetails() === null) {
      return false;
    }
    const numberOfParticipants = this.serviceDetails()!.numberOfParticipants;
    const coaches = this.serviceDetails()!.coaches.length;
    const documents = documentType.documents.length + docToAdd;

    if (documents > 1 && documentType.value === DocumentType.REPORT) {
      return true;
    } else if (documents > numberOfParticipants  && documentType.value === DocumentType.CONSENT) {
      return true;
    } else if (documents > coaches && documentType.value === DocumentType.COACH_INVOICE) {
      return true;
    } else if (documents > 1 && documentType.value === DocumentType.PROVIDER_INVOICE) {
      return true;
    } else if (documents > 1 && documentType.value === DocumentType.INTERMEDIARY_INVOICE) {
      return true;
    } else if (documents > numberOfParticipants && documentType.value === DocumentType.PARTICIPANT_BUR_QUESTIONNAIRE) {
      return true;
    } else if (documents > 1 && documentType.value === DocumentType.CUSTOMER_BUR_QUESTIONNAIRE) {
      return true;
    } else if (documents > numberOfParticipants && documentType.value === DocumentType.PARTICIPANT_PROVIDER_QUESTIONNAIRE) {
      return true;
    } else if (documents > 1 && documentType.value === DocumentType.ATTENDANCE_LIST) {
      return true;
    }
    return false;
  }

  private isCorrectSizeOfFiles(files: FileList | null): boolean {
    if (!files) {
      return true;
    }
    let sum = 0;
    for(let i = 0; i < files.length; i++) {
      sum += files[i].size;
    }
    return this.MB_50_SIZE > sum
  }
}
