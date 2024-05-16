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
