import {UserListDataInterface} from "../user/model/user-list-data.interface";

export interface DocumentTypeData {
  value: string,
  name: string
}

export interface DocumentData {
  id: string,
  coachId: string,
  type: DocumentTypeData
  fileName: string,
  fileExtension: string,
  createdAt: Date,
  creator: UserListDataInterface
}

export interface DocumentViewData {
  id: string,
  coachId: string,
  type: DocumentTypeData
  fileName: string,
  fileExtension: string,
  createdAt: Date,
  creator: UserListDataInterface,
  checked?: boolean
}

export interface GetAllDocumentTypesResponse {
  types: DocumentTypeData[]
}

export interface DocumentTypeViewData {
  value: string,
  name: string,
  opened?: boolean,
  filesToSend: FileList | null
  documents: DocumentViewData[],
  checkedAll: boolean,
  badgeMessages: string
}

export enum DocumentType {
  REPORT = 'REPORT',
  CONSENT = 'CONSENT',
  COACH_INVOICE = 'COACH_INVOICE',
  PROVIDER_INVOICE = 'PROVIDER_INVOICE',
  INTERMEDIARY_INVOICE = 'INTERMEDIARY_INVOICE',
  PARTICIPANT_BUR_QUESTIONNAIRE = 'PARTICIPANT_BUR_QUESTIONNAIRE',
  CUSTOMER_BUR_QUESTIONNAIRE = 'CUSTOMER_BUR_QUESTIONNAIRE',
  PARTICIPANT_PROVIDER_QUESTIONNAIRE = 'PARTICIPANT_PROVIDER_QUESTIONNAIRE',
  ATTENDANCE_LIST = 'ATTENDANCE_LIST',
  PRESENTATION = 'PRESENTATION'
}

export enum BadgeMessageType {
  NOT_COMPLETE_SERVICE = 'NOT_COMPLETE_SERVICE',
  MISSING_REPORT = 'MISSING_REPORT',
  NOT_ENOUGH_CONSENTS = 'NOT_ENOUGH_CONSENTS',
  MISSING_COACH_INVOICE = 'MISSING_COACH_INVOICE',
  MISSING_PROVIDER_INVOICE = 'MISSING_PROVIDER_INVOICE',
  MISSING_INTERMEDIARY_INVOICE = 'MISSING_INTERMEDIARY_INVOICE',
  NOT_ENOUGH_PARTICIPANT_BUR_QUESTIONNAIRES = 'NOT_ENOUGH_PARTICIPANT_BUR_QUESTIONNAIRES',
  MISSING_CUSTOMER_BUR_QUESTIONNAIRE = 'MISSING_CUSTOMER_BUR_QUESTIONNAIRE',
  NOT_ENOUGH_PARTICIPANT_PROVIDER_QUESTIONNAIRE = 'NOT_ENOUGH_PARTICIPANT_PROVIDER_QUESTIONNAIRE',
  MISSING_ATTENDANCE_LIST = 'MISSING_ATTENDANCE_LIST',
  MISSING_PRESENTATION = 'MISSING_PRESENTATION'
}

export interface AddNewFilesResponse {
  message: string
}

export interface DeleteDocumentsResponse {
  message: string
}
