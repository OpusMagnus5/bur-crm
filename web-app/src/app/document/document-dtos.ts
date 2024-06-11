export interface DocumentTypeData {
  value: string,
  name: string
  opened?: boolean
}

export interface DocumentData {
  id: string,
  name: string
  extension: string
  type: DocumentTypeData
}

export interface GetAllDocumentTypesResponse {
  types: DocumentTypeData[]
}
