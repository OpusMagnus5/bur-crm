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
