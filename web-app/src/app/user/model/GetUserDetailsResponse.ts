export interface GetUserDetailsResponse {

  id: string,
  email: string,
  firstName: string,
  lastName: string,
  roles: string[],
  lastLogin: Date,
  createdAt: Date,
  modifiedAt: Date,
  creatorFirstName: string,
  creatorLastName: string,
  modifierFirstName: string,
  modifierLastName: string
}
