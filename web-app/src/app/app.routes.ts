import {Routes} from '@angular/router';
import {UserDashboardComponent} from "./administration/users/dashboard/user.dashboard.component";
import {NewUserComponent} from "./administration/users/new-user/new-user.component";
import {UsersListComponent} from "./administration/users/users-list/users-list.component";
import {NotFoundComponent} from "./not-found/not-found.component";
import {HomeComponent} from "./home/home.component";

export const ADMINISTRATION_PATH: string = 'administration';
export const BASE_PATH: string = '';
export const ADMINISTRATION_USERS_PATH: string = ADMINISTRATION_PATH + '/users';
export const NEW_USER_PATH: string = 'new';
export const USER_LIST_PATH: string = 'list';

const NOT_FOUND_PATH = 'not-found';

export const routes: Routes = [
  {
    path: BASE_PATH, component: HomeComponent, pathMatch: "full"
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
