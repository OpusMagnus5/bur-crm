import {Component, OnDestroy, signal, WritableSignal} from '@angular/core';
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
  ACCOUNT_DETAILS_PATH,
  ACCOUNT_PATH,
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
import {SubscriptionManager} from "../shared/util/subscription-manager";
import {LocalizedDatePipe} from "../shared/pipe/localized-date.pipe";
import {toObservable} from "@angular/core/rxjs-interop";

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
    ChangeLanguageComponent,
    LocalizedDatePipe
  ]
})
export class NavigationComponent implements OnDestroy {

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
  protected readonly ACCOUNT_DETAILS_PATH = ACCOUNT_PATH + '/' + ACCOUNT_DETAILS_PATH;


  protected isHandset$: Observable<boolean>;
  private readonly subscriptions: SubscriptionManager = new SubscriptionManager();
  protected timeToLogout: WritableSignal<Date | null> = signal(null)
  private countdown: any;

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

    this.subscriptions.add(toObservable(this.auth.valid).subscribe(valid => {
      if (!valid) {
        this.timeToLogout.set(null);
        clearInterval(this.countdown);
      } else {
        this.countdown = this.setCountdown();
      }
    }));
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribeAll();
  }

  protected onLogout(): void {
    this.auth.logout().subscribe(() => this.router.navigate(['/', LOGIN_PATH]));
  }

  private setCountdown() {
    return setInterval(() => {
      const now: number = new Date().getTime();
      const expires: number = this.auth.getAuthData()!.expires.getTime();
      const diff: Date = new Date(expires - now);
      this.timeToLogout.set(diff);
      if (now >= expires) {
        this.onLogout();
      }
    }, 1000);
  }
}
