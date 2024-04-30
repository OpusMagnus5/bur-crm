import {Routes} from '@angular/router';
import {UserDashboardComponent} from "./user/user-dashboard.component";
import {NewUserComponent} from "./user/new-user.component";
import {UsersListComponent} from "./user/users-list.component";
import {NotFoundComponent} from "./not-found/not-found.component";
import {HomeComponent} from "./home/home.component";
import {ServiceProviderDashboardComponent} from "./service-provider/service-provider-dashboard.component";
import {NewServiceProviderComponent} from "./service-provider/new-service-provider.component";

export const ADMINISTRATION_PATH: string = 'administration';
export const BASE_PATH: string = '';
export const ADMINISTRATION_USERS_PATH: string = ADMINISTRATION_PATH + '/users';
export const NEW_USER_PATH: string = 'new';
export const USER_LIST_PATH: string = 'list';
export const REGISTRY_PATH: string = 'registry'
export const REGISTRY_SERVICE_PROVIDER_PATH: string = REGISTRY_PATH + '/service-provider'
export const NEW_SERVICE_PROVIDER_PATH: string = 'new';

const NOT_FOUND_PATH = 'not-found';

export const routes: Routes = [
  {
    path: BASE_PATH, component: HomeComponent, pathMatch: "full"
  },
  {
    path: REGISTRY_SERVICE_PROVIDER_PATH,
    component: ServiceProviderDashboardComponent,
    children: [
      {
        path: NEW_SERVICE_PROVIDER_PATH,
        component: NewServiceProviderComponent
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
