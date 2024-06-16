import {OperatorDataInterface} from "../operator/model/operator-data.interface";
import {CustomerData} from "../customer/customer-dtos";
import {ServiceProviderDataInterface} from "../service-provider/model/service-provider-data.interface";
import {UserListDataInterface} from "../user/model/user-list-data.interface";
import {ProgramDataInterface} from "../program/model/program-data-interface";
import {CoachData} from "../coach/coach-dtos";
import {IntermediaryData} from "../intermediary/intermediary-dtos";
import {DocumentData} from "../document/document-dtos";

export interface ServiceTypeData {
  value: string,
  name: string
}

export interface ServiceStatusData {
  value: string,
  name: string
}

export interface BadgeMessageData {
  type: string,
  message: string
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
  status: ServiceStatusData
}

export interface CreateOrUpdateServiceRequest {
  id: string | null,
  version: number | null,
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
  intermediaryId: string,
  status: string
}

export interface CreateNewServiceResponse {
  messages: string[]
}

export interface ServiceData {
  id: string,
  number: string,
  name: string,
  type: string,
  startDate: Date,
  endDate: Date,
  operator: OperatorDataInterface,
  customer: CustomerData,
  serviceProvider: ServiceProviderDataInterface,
  badgeMessages: BadgeMessageData[]
}

export interface ServicePageResponse {
  services: ServiceData[],
  totalServices: number
}

export interface GetServiceDetailsResponse {
  id: string,
  version: number
  number: string,
  name: string,
  type: ServiceTypeData,
  startDate: Date,
  endDate: Date,
  numberOfParticipants: number,
  creator: UserListDataInterface
  modifier: UserListDataInterface
  operator: OperatorDataInterface,
  customer: CustomerData,
  serviceProvider: ServiceProviderDataInterface,
  program: ProgramDataInterface,
  intermediary: IntermediaryData,
  coaches: CoachData[],
  status: ServiceStatusData,
  documents: DocumentData[],
  badgeMessages: BadgeMessageData[]
}

export interface GetAllServiceStatusesResponse {
  statuses: ServiceStatusData[]
}

export enum ServiceType {
  TRAINING = 'TRAINING',
  CONSULTING = 'CONSULTING'
}

export interface DeleteServiceResponse {
  message: string
}
