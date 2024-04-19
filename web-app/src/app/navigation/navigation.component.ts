import {Component, inject} from '@angular/core';
import {BreakpointObserver, Breakpoints} from '@angular/cdk/layout';
import {AsyncPipe} from '@angular/common';
import {MatToolbarModule} from '@angular/material/toolbar';
import {MatButtonModule} from '@angular/material/button';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatListModule} from '@angular/material/list';
import {MatIconModule} from '@angular/material/icon';
import {MatMenuModule} from '@angular/material/menu';
import {Observable} from 'rxjs';
import {map, shareReplay} from 'rxjs/operators';
import {UserDashboardComponent} from '../administration/users/dashboard/user.dashboard.component';
import {RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {ADMINISTRATION_USERS_PATH, BASE_PATH} from "../app.routes";

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrl: './navigation.component.css',
  standalone: true,
  imports: [
    MatToolbarModule,
    MatButtonModule,
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    AsyncPipe,
    MatMenuModule,
    UserDashboardComponent,
    RouterOutlet,
    RouterLink,
    RouterLinkActive
  ]
})
export class NavigationComponent {
  private breakpointObserver = inject(BreakpointObserver);

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );
  protected readonly BASE_PATH = BASE_PATH;
  protected readonly ADMINISTRATION_USERS_PATH = ADMINISTRATION_USERS_PATH;
}
