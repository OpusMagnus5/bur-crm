import {Routes} from '@angular/router';
import {UserDashboardComponent} from "./user/user-dashboard.component";
import {NewUserComponent} from "./user/new-user.component";
import {UsersListComponent} from "./user/users-list.component";
import {NotFoundComponent} from "./not-found/not-found.component";
import {HomeComponent} from "./home/home.component";
import {ServiceProviderDashboardComponent} from "./service-provider/service-provider-dashboard.component";
import {NewServiceProviderComponent} from "./service-provider/new-service-provider.component";
import {ServiceProviderListComponent} from "./service-provider/service-provider-list.component";
import {OperatorDashboardComponent} from "./operator/operator-dashboard.component";
import {CreateNewOperatorComponent} from "./operator/create-new-operator.component";
import {OperatorListComponent} from "./operator/operator-list.component";
import {ProgramDashboardComponent} from "./program/program-dashboard.component";
import {CreateNewProgramComponent} from "./program/create-new-program.component";
import {ProgramListComponent} from "./program/program-list.component";
import {CustomerDashboardComponent} from "./customer/customer-dashboard.component";
import {CreateNewCustomerComponent} from "./customer/create-new-customer.component";
import {CustomerListComponent} from "./customer/customer-list.component";
import {CoachDashboardComponent} from "./coach/coach-dashboard.component";
import {CreateNewCoachComponent} from "./coach/create-new-coach.component";

export const ADMINISTRATION_PATH: string = 'administration';
export const BASE_PATH: string = '';
export const ADMINISTRATION_USERS_PATH: string = ADMINISTRATION_PATH + '/users';
export const NEW_USER_PATH: string = 'new';
export const USER_LIST_PATH: string = 'list';
export const REGISTRY_PATH: string = 'registry'
export const REGISTRY_SERVICE_PROVIDER_PATH: string = REGISTRY_PATH + '/service-provider'
export const NEW_SERVICE_PROVIDER_PATH: string = 'new';
export const SERVICE_PROVIDER_LIST_PATH: string = 'list';
export const REGISTRY_OPERATOR_PATH: string = REGISTRY_PATH + '/operator';
export const NEW_OPERATOR_PATH: string = 'new';
export const OPERATOR_LIST_PATH: string = 'list';
export const REGISTRY_PROGRAM_PATH: string = REGISTRY_PATH + '/program'
export const NEW_PROGRAM_PATH: string = 'new';
export const PROGRAM_LIST_PATH: string = 'list';
export const REGISTRY_CUSTOMER_PATH: string = REGISTRY_PATH + '/customer'
export const NEW_CUSTOMER_PATH: string = 'new';
export const CUSTOMER_LIST_PATH: string = 'list';
export const REGISTRY_COACH_PATH: string = REGISTRY_PATH + '/coach';
export const NEW_COACH_PATH: string = 'new';
export const COACH_LIST_PATH: string = 'list';

const NOT_FOUND_PATH = 'not-found';

export const routes: Routes = [
  {
    path: BASE_PATH, component: HomeComponent, pathMatch: "full"
  },
  {
    path: REGISTRY_COACH_PATH,
    component: CoachDashboardComponent,
    children: [
      {
        path: NEW_COACH_PATH,
        component: CreateNewCoachComponent
      }
    ]
  },
  {
    path: REGISTRY_CUSTOMER_PATH,
    component: CustomerDashboardComponent,
    children: [
      {
        path: NEW_CUSTOMER_PATH,
        component: CreateNewCustomerComponent
      },
      {
        path: CUSTOMER_LIST_PATH,
        component: CustomerListComponent
      }
    ]
  },
  {
    path: REGISTRY_OPERATOR_PATH,
    component: OperatorDashboardComponent,
    children: [
      {
        path: NEW_OPERATOR_PATH,
        component: CreateNewOperatorComponent
      },
      {
        path: OPERATOR_LIST_PATH,
        component: OperatorListComponent
      }
    ]
  },
  {
    path: REGISTRY_SERVICE_PROVIDER_PATH,
    component: ServiceProviderDashboardComponent,
    children: [
      {
        path: NEW_SERVICE_PROVIDER_PATH,
        component: NewServiceProviderComponent
      },
      {
        path: SERVICE_PROVIDER_LIST_PATH,
        component: ServiceProviderListComponent
      }
    ]
  },
  {
    path: REGISTRY_PROGRAM_PATH,
    component: ProgramDashboardComponent,
    children: [
      {
        path: NEW_PROGRAM_PATH,
        component: CreateNewProgramComponent
      },
      {
        path: PROGRAM_LIST_PATH,
        component: ProgramListComponent
      }
    ]
  },
  { path: ADMINISTRATION_USERS_PATH,
    component: UserDashboardComponent,
    children: [
      { path: NEW_USER_PATH,
        component: NewUserComponent
      },
      {
        path: USER_LIST_PATH,
        component: UsersListComponent
      }
    ]
  },
  {
    path: NOT_FOUND_PATH, component: NotFoundComponent
  },
  {
    path: '**', redirectTo: NOT_FOUND_PATH
  }
];
