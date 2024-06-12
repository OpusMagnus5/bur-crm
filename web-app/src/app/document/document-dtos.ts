export interface DocumentTypeData {
  value: string,
  name: string
}

export interface DocumentData {
  id: string,
  name: string,
  extension: string,
  type: DocumentTypeData
}

export interface GetAllDocumentTypesResponse {
  types: DocumentTypeData[]
}



export interface DocumentViewData {
  value: string,
  name: string,
  opened?: boolean
  files: FileList | null
}
