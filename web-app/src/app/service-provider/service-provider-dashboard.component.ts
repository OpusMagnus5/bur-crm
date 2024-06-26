import {Component} from '@angular/core';
import {NEW_SERVICE_PROVIDER_PATH, REGISTRY_SERVICE_PROVIDER_PATH, SERVICE_PROVIDER_LIST_PATH} from "../app.routes";
import {RouterService} from "../shared/service/router.service";
import {TranslateService} from "@ngx-translate/core";
import {MatTabLink, MatTabNav, MatTabNavPanel} from "@angular/material/tabs";
import {RouterLink, RouterOutlet} from "@angular/router";

@Component({
  selector: 'app-service-provider-dashboard',
  standalone: true,
  imports: [
    MatTabNav,
    MatTabLink,
    RouterLink,
    MatTabNavPanel,
    RouterOutlet
  ],
  templateUrl: './service-provider-dashboard.component.html',
  styleUrl: './service-provider-dashboard.component.css'
})
export class ServiceProviderDashboardComponent {

  protected links: {path: string, name: string}[] = [
    {
      path: SERVICE_PROVIDER_LIST_PATH,
      name: ''
    },
    {
      path: NEW_SERVICE_PROVIDER_PATH,
      name: ''
    }
  ];

  constructor(protected routerService: RouterService, private translate: TranslateService) {
    translate.get('service-provider.new-service-provider').subscribe(text => {
       this.links[1].name = text;
    });
    translate.get('service-provider.service-provider-list').subscribe(text => {
      this.links[0].name = text;
    });
  }

  protected getFullRoutePath(path: string) {
    return '/' + REGISTRY_SERVICE_PROVIDER_PATH + '/' + path;
  }
}
