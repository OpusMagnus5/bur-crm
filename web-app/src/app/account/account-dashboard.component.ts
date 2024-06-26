import {Component, signal, WritableSignal} from '@angular/core';
import {ACCOUNT_CHANGE_PASSWORD_PATH, ACCOUNT_DETAILS_PATH, ACCOUNT_PATH} from "../app.routes";
import {RouterService} from "../shared/service/router.service";
import {TranslateService} from "@ngx-translate/core";
import {MatTabLink, MatTabNav, MatTabNavPanel} from "@angular/material/tabs";
import {RouterLink, RouterOutlet} from "@angular/router";

@Component({
  selector: 'app-account-dashboard',
  standalone: true,
  imports: [
    MatTabLink,
    MatTabNav,
    MatTabNavPanel,
    RouterOutlet,
    RouterLink
  ],
  templateUrl: './account-dashboard.component.html',
  styles: ['@tailwind base']
})
export class AccountDashboardComponent {

  protected links: WritableSignal<{ path: string, name: string}[]> = signal([
    {
      path: ACCOUNT_DETAILS_PATH,
      name: ''
    }
  ]);
  protected hiddenLinks: WritableSignal<{ path: string, name: string}[]> = signal([
    {
      path: ACCOUNT_CHANGE_PASSWORD_PATH,
      name: ''
    }
  ]);

  constructor(
    protected routerService: RouterService,
    private translate: TranslateService
  ) {
    this.translate.get('account.details').subscribe(text => this.links()[0].name = text);
    this.translate.get('account.change-password').subscribe(text => this.hiddenLinks()[0].name = text);
  }

  protected getFullRoutePath(path: string) {
    return '/' + ACCOUNT_PATH + '/' + path;
  }
}
