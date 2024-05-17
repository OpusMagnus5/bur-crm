export interface CreateNewCustomerRequest {
  name: string,
  nip: string
}

export interface CreateNewCustomerResponse {
  message: string
}

export interface CustomerExistsResponse {
  exists: boolean
}

export interface CustomerData {
  id: string,
  nip: string,
  name: string
}

export interface CustomerPageResponse {
  customers: CustomerData[],
  totalCustomers: number
}

export interface DeleteCustomerResponse {
  message: string
}

export interface CustomerDetailsResponse {
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
