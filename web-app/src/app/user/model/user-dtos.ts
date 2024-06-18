export interface ResetUserPasswordResponse {
  newPassword: string
}

export interface ResetUserPasswordRequest {
  id: string,
  userVersion: number
}
