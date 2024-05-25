export interface ServiceTypeData {
  value: string,
  name: string
}

export interface GetAllServiceTypesResponse {
  serviceTypes: ServiceTypeData[]
}
