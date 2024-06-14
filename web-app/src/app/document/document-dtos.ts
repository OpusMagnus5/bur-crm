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

export interface GetAllDocumentTypesResponse {
  types: DocumentTypeData[]
}

export interface DocumentViewData {
  value: string,
  name: string,
  opened?: boolean,
  files: FileList | null
  documents: DocumentData[]
}

export enum DocumentType {
  COACH_INVOICE = 'COACH_INVOICE'
}

export interface AddNewFilesResponse {
  message: string
}
