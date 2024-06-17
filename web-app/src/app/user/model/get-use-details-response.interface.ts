export interface GetUseDetailsResponseInterface {

  id: string,
  version: number,
  email: string,
  firstName: string,
  lastName: string,
  roles: {role :string, name: string}[],
  lastLogin: Date | undefined,
  createdAt: Date,
  modifiedAt: Date | undefined,
  creatorFirstName: string,
  creatorLastName: string,
  modifierFirstName: string | undefined,
  modifierLastName: string | undefined
}
