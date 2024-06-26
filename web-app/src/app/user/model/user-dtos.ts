export interface ResetUserPasswordResponse {
  newPassword: string
}

export interface ResetUserPasswordRequest {
  id: string,
  userVersion: number
}

export interface LoginResponse {
  id: string,
  email: string,
  expires: Date,
  roles: UserRole[]
}

export enum UserRole {
  BLOCKED_USER = 'BLOCKED_USER',
  USER = 'USER',
  MANAGER = 'MANAGER',
  ADMIN = 'ADMIN'
}

export interface ChangeUserPasswordRequest {
  userId: string,
  version: number,
  newPassword: string,
  oldPassword: string
}

export interface ChangeUserPasswordResponse {
  message: string
}
