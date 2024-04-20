import {Routes} from '@angular/router';
import {UserDashboardComponent} from "./administration/users/dashboard/user.dashboard.component";
import {NewUserComponent} from "./administration/users/new-user/new-user.component";
import {UsersListComponent} from "./administration/users/users-list/users-list.component";

export const ADMINISTRATION_PATH: string = 'administration';
export const BASE_PATH: string = '/';
export const ADMINISTRATION_USERS_PATH: string = ADMINISTRATION_PATH + '/users';
export const NEW_USER_PATH: string = 'new';
export const USER_LIST_PATH: string = 'list';

export const routes: Routes = [
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
  }
];
