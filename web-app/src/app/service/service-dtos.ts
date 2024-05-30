export interface ServiceTypeData {
  value: string,
  name: string
}

export interface GetAllServiceTypesResponse {
  serviceTypes: ServiceTypeData[]
}

export interface GetServiceFromBurResponse {
  number: string,
  title: string,
  serviceType: string
  startDate: Date,
  endDate: Date,
  serviceProviderName: string
}

export interface CreateNewServiceRequest {
  number: string,
  name: string,
  type: string,
  startDate: Date,
  endDate: Date,
  numberOfParticipants: number,
  serviceProviderId: string,
  programId: string,
  customerId: string,
  coachIds: string[],
  intermediaryId: string
}

export interface CreateNewServiceResponse {
  messages: string[]
}
