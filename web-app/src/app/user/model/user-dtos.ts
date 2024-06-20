export interface ResetUserPasswordResponse {
  newPassword: string
}

export interface ResetUserPasswordRequest {
  id: string,
  userVersion: number
}

export interface LoginResponse {
  email: string
  expires: Date,
  roles: UserRole[]
}

export enum UserRole {
  BLOCKED_USER = 'BLOCKED_USER',
  USER = 'USER',
  MANAGER = 'MANAGER',
  ADMIN = 'ADMIN'
}
