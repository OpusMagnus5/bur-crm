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
import {RouterLink, RouterLinkActive, RouterOutlet} from "@angular/router";
import {
  ADMINISTRATION_PATH,
  ADMINISTRATION_USERS_PATH,
  BASE_PATH,
  OPERATOR_LIST_PATH,
  REGISTRY_OPERATOR_PATH,
  REGISTRY_PATH,
  REGISTRY_SERVICE_PROVIDER_PATH,
  SERVICE_PROVIDER_LIST_PATH,
  USER_LIST_PATH
} from "../app.routes";
import {RouterService} from "../shared/service/router.service";
import {TranslateModule} from "@ngx-translate/core";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatOption, MatSelect} from "@angular/material/select";
import {ChangeLanguageComponent} from "../change-langauage/change-language.component";

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

  protected isHandset$: Observable<boolean>;

  constructor(private breakpointObserver: BreakpointObserver, protected routerService: RouterService) {
    this.isHandset$ = breakpointObserver.observe(Breakpoints.XSmall)
      .pipe(
        map(result => result.matches),
        shareReplay()
      );
  }
}
