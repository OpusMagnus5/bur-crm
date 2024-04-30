import {Component} from '@angular/core';
import {NEW_SERVICE_PROVIDER_PATH, REGISTRY_SERVICE_PROVIDER_PATH} from "../app.routes";
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
      path: NEW_SERVICE_PROVIDER_PATH,
      name: ''
    }
  ]

  constructor(protected routerService: RouterService, private translate: TranslateService) {
    translate.get('service-provider.new-service-provider').subscribe((text: string) =>
      this.links[0].name = text
    )
  }

  protected getFullRoutePath(path: string) {
    return '/' + REGISTRY_SERVICE_PROVIDER_PATH + '/' + path;
  }
}
