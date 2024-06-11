export interface DocumentTypeData {
  value: string,
  name: string
  opened?: boolean
}

export interface DocumentData {
  id: string,
  name: string
  extension: string
  type: string
}

export interface GetAllDocumentTypesResponse {
  types: DocumentTypeData[]
}
