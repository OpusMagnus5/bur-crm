import {Component} from '@angular/core';
import {MatGridListModule} from '@angular/material/grid-list';
import {MatTabsModule} from '@angular/material/tabs';
import {NewUserComponent} from '../new-user/new-user.component';
import {Route, RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {ADMINISTRATION_USERS_PATH, routes} from "../../../app.routes";

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
  links: (string | undefined)[] = routes
    .filter((route: Route) => route.path === ADMINISTRATION_USERS_PATH)
    .flatMap((route: Route) => route.children)
    .filter((routes: Route | undefined) => routes !== undefined)
    .map((route: Route | undefined) => route?.path);

  activeLink: string | undefined = '';
}
