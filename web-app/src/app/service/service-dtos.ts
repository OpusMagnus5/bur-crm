import {OperatorDataInterface} from "../operator/model/operator-data.interface";
import {CustomerData} from "../customer/customer-dtos";
import {ServiceProviderDataInterface} from "../service-provider/model/service-provider-data.interface";

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

export interface ServiceData {
  id: string,
  number: string,
  name: string,
  type: string,
  startDate: string,
  endDate: string,
  operator: OperatorDataInterface,
  customer: CustomerData,
  serviceProvider: ServiceProviderDataInterface
}

export interface ServicePageResponse {
  services: ServiceData[],
  totalServices: number
}
