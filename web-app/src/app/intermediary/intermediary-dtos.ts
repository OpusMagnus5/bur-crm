export interface CreateNewIntermediaryRequest {
  name: string,
  nip: string
}

export interface CreateNewIntermediaryResponse {
  message: string
}

export interface IntermediaryExistsResponse {
  exists: boolean
}

export interface IntermediaryData {
  id: string,
  nip: string,
  name: string
}

export interface IntermediaryPageResponse {
  intermediaries: IntermediaryData[],
  totalIntermediaries: number
}

export interface DeleteIntermediaryResponse {
  message: string
}

export interface IntermediaryDetailsResponse {
  id: string,
  version: number,
  name: string,
  nip: string,
  createdAt: Date,
  modifiedAt: Date,
  creatorFirstName: string,
  creatorLastName: string,
  modifierFirstName: string,
  modifierLastName: string
}

export interface UpdateIntermediaryRequest {
  id: string,
  version: number,
  name: string,
  nip: string
}

export interface UpdateIntermediaryResponse {
  message: string
}
