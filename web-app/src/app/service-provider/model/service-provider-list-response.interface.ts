import {ServiceProviderDataInterface} from "./service-provider-data.interface";

export interface ServiceProviderListResponseInterface {

  providers: ServiceProviderDataInterface[],
  totalProviders: number
}
