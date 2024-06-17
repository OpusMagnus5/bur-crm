export interface CreateNewUserRequestInterface {
  id: string | null,
  version: number | null,
  email: string,
  firstName: string,
  lastName: string,
  role: string
}
