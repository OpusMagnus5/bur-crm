export interface CreateNewCoachRequest {
  firstName: string,
  lastName: string,
  pesel: string
}

export interface CreateNewCoachResponse {
  message: string
}

export interface CoachExistsResponse {
  exists: boolean
}

export interface CoachData {
  id: string,
  firstName: string,
  lastName: string,
  pesel: string
}

export interface CoachPageResponse {
  coaches: CoachData[],
  totalCoaches: number
}

export interface DeleteCoachResponse {
  message: string
}

export interface CoachDetailsResponse {
  id: string,
  version: number,
  firstName: string,
  lastName: string,
  pesel: string,
  createdAt: Date,
  modifiedAt: Date | undefined,
  creatorFirstName: string,
  creatorLastName: string,
  modifierFirstName: string | undefined,
  modifierLastName: string | undefined
}

export interface UpdateCoachResponse {
  message: string
}

export interface UpdateCoachRequest {
  id: string,
  version: number,
  firstName: string,
  lastName: string,
  pesel: string
}
