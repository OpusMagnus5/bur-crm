import {Component} from '@angular/core';
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
import {UserDashboardComponent} from '../user/user-dashboard.component';
import {Router, RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {
  ADMINISTRATION_PATH,
  ADMINISTRATION_USERS_PATH,
  BASE_PATH,
  COACH_LIST_PATH,
  CUSTOMER_LIST_PATH,
  INTERMEDIARY_LIST_PATH,
  LOGIN_PATH,
  OPERATOR_LIST_PATH,
  PROGRAM_LIST_PATH,
  REGISTRY_COACH_PATH,
  REGISTRY_CUSTOMER_PATH,
  REGISTRY_INTERMEDIARY_PATH,
  REGISTRY_OPERATOR_PATH,
  REGISTRY_PATH,
  REGISTRY_PROGRAM_PATH,
  REGISTRY_SERVICE_PROVIDER_PATH,
  SERVICE_PROVIDER_LIST_PATH,
  SERVICES_LIST_PATH,
  SERVICES_PATH,
  USER_LIST_PATH
} from "../app.routes";
import {RouterService} from "../shared/service/router.service";
import {TranslateModule} from "@ngx-translate/core";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatOption, MatSelect} from "@angular/material/select";
import {ChangeLanguageComponent} from "../change-langauage/change-language.component";
import {AuthService} from "../auth/auth.service";

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
    RouterLinkActive,
    TranslateModule,
    MatFormField,
    MatSelect,
    MatOption,
    MatLabel,
    ChangeLanguageComponent
  ]
})
export class NavigationComponent {

  protected readonly BASE_PATH = BASE_PATH;
  protected readonly ADMINISTRATION_PATH = ADMINISTRATION_PATH;
  protected readonly ADMINISTRATION_USERS_PATH = ADMINISTRATION_USERS_PATH + '/' + USER_LIST_PATH;
  protected readonly REGISTRY_PATH = REGISTRY_PATH;
  protected readonly REGISTRY_SERVICE_PROVIDER_PATH = REGISTRY_SERVICE_PROVIDER_PATH + '/' + SERVICE_PROVIDER_LIST_PATH;
  protected readonly REGISTRY_OPERATOR_PATH = REGISTRY_OPERATOR_PATH + '/' + OPERATOR_LIST_PATH;
  protected readonly REGISTRY_PROGRAM_PATH = REGISTRY_PROGRAM_PATH + '/' + PROGRAM_LIST_PATH;
  protected readonly REGISTRY_CUSTOMER_PATH = REGISTRY_CUSTOMER_PATH + '/' + CUSTOMER_LIST_PATH;
  protected readonly REGISTRY_COACH_PATH = REGISTRY_COACH_PATH + '/' + COACH_LIST_PATH;
  protected readonly REGISTRY_INTERMEDIARY_PATH = REGISTRY_INTERMEDIARY_PATH + '/' + INTERMEDIARY_LIST_PATH;
  protected readonly SERVICES_PATH = SERVICES_PATH + '/' + SERVICES_LIST_PATH;
  protected readonly LOGIN_PATH = LOGIN_PATH;


  protected isHandset$: Observable<boolean>;

  constructor(
    private breakpointObserver: BreakpointObserver,
    protected routerService: RouterService,
    private router: Router,
    protected auth: AuthService
  ) {
    this.isHandset$ = this.breakpointObserver.observe(Breakpoints.XSmall)
      .pipe(
        map(result => result.matches),
        shareReplay()
      );
  }

  protected onLogout(): void {
    this.auth.logout().subscribe(() => this.router.navigate(['/', LOGIN_PATH]));
  }
}
