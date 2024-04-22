import {Component} from '@angular/core';
import {MatGridListModule} from '@angular/material/grid-list';
import {MatTabsModule} from '@angular/material/tabs';
import {NewUserComponent} from './new-user.component';
import {RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {ADMINISTRATION_USERS_PATH, NEW_USER_PATH, USER_LIST_PATH} from "../app.routes";
import {RouterService} from "../shared/service/router.service";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'user-dashboard',
  templateUrl: './user-dashboard.component.html',
  styleUrl: './user-dashboard.component.css',
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

  protected links: {path: string, name: string}[] = [
    {
      path: USER_LIST_PATH,
      name: ''
    },
    {
      path: NEW_USER_PATH,
      name: ''
    }
  ]

  constructor(protected routerService: RouterService, private translate: TranslateService) {
    translate.get('user-dashboard.users-list-tab').subscribe((text: string) => {
      this.links[0].name = text;
    });
    translate.get('user-dashboard.new-user-tab').subscribe((text: string) => {
      this.links[1].name = text;
    });
  }

  protected getFullRoutePath(path: string) {
    return '/' + ADMINISTRATION_USERS_PATH + '/' + path;
  }
}
