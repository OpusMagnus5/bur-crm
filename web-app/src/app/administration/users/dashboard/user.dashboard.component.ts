import {Component} from '@angular/core';
import {MatGridListModule} from '@angular/material/grid-list';
import {MatTabsModule} from '@angular/material/tabs';
import {NewUserComponent} from '../new-user/new-user.component';
import {RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {ADMINISTRATION_USERS_PATH, NEW_USER_PATH, USER_LIST_PATH} from "../../../app.routes";
import {RouterService} from "../../../shared/service/router.service";

@Component({
  selector: 'user-dashboard',
  templateUrl: './user.dashboard.component.html',
  styleUrl: './user.dashboard.component.css',
  standalone: true,
  imports: [
    MatGridListModule,
    MatTabsModule,
    NewUserComponent,
    RouterOutlet,
    RouterLink,
    RouterLinkActive
  ]
})
export class UserDashboardComponent {

  protected readonly ADMINISTRATION_USERS_PATH = ADMINISTRATION_USERS_PATH;

  protected links: {path: string, name: string}[] = [
    {
      path: USER_LIST_PATH,
      name: 'Users List'
    },
    {
      path: NEW_USER_PATH,
      name: 'Add New'
    }
  ]

  constructor(protected routerService: RouterService) {
  }

  protected getFullRoutePath(path: string) {
    return '/' + ADMINISTRATION_USERS_PATH + '/' + path;
  }
}
