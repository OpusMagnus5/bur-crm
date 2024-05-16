import {Component} from '@angular/core';
import {CUSTOMER_LIST_PATH, NEW_CUSTOMER_PATH, REGISTRY_CUSTOMER_PATH} from "../app.routes";
import {RouterService} from "../shared/service/router.service";
import {TranslateService} from "@ngx-translate/core";
import {MatTabLink, MatTabNav, MatTabNavPanel} from "@angular/material/tabs";
import {RouterLink, RouterOutlet} from "@angular/router";

@Component({
  selector: 'app-customer-dashboard',
  standalone: true,
  imports: [
    MatTabLink,
    MatTabNav,
    MatTabNavPanel,
    RouterOutlet,
    RouterLink
  ],
  templateUrl: './customer-dashboard.component.html'
})
export class CustomerDashboardComponent {

  protected links: {path: string, name: string}[] = [
    {
      path: CUSTOMER_LIST_PATH,
      name: ''
    },
    {
      path: NEW_CUSTOMER_PATH,
      name: ''
    }
  ];

  constructor(
    protected routerService: RouterService,
    private translate: TranslateService
  ) {
    translate.get('customer.new-customer').subscribe(text => {
      this.links[1].name = text;
    });
    translate.get('customer.customer-list').subscribe(text => {
      this.links[0].name = text;
    });
  }

  protected getFullRoutePath(path: string) {
    return '/' + REGISTRY_CUSTOMER_PATH + '/' + path;
  }
}
