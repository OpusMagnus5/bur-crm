import {Component, signal, WritableSignal} from '@angular/core';
import {
  EDIT_SERVICE_PATH,
  NEW_SERVICE_PATH,
  SERVICE_DETAILS_PATH,
  SERVICES_LIST_PATH,
  SERVICES_PATH
} from "../app.routes";
import {RouterService} from "../shared/service/router.service";
import {TranslateService} from "@ngx-translate/core";
import {MatTabLink, MatTabNav, MatTabNavPanel} from "@angular/material/tabs";
import {RouterLink, RouterOutlet} from "@angular/router";

@Component({
  selector: 'app-service-dashboard',
  standalone: true,
  imports: [
    MatTabLink,
    MatTabNav,
    MatTabNavPanel,
    RouterOutlet,
    RouterLink
  ],
  templateUrl: './service-dashboard.component.html'
})
export class ServiceDashboardComponent {
  protected links: WritableSignal<{ path: string, name: string}[]> = signal([
    {
      path: SERVICES_LIST_PATH,
      name: ''
    },
    {
      path: NEW_SERVICE_PATH,
      name: ''
    }
  ]);

  protected hiddenLinks: WritableSignal<{ path: string, name: string}[]> = signal([
    {
      path: EDIT_SERVICE_PATH,
      name: ''
    },
    {
      path: SERVICE_DETAILS_PATH,
      name: ''
    }
  ]);

  constructor(
    protected routerService: RouterService,
    private translate: TranslateService
  ) {
    this.translate.get('service.service-list').subscribe(text => this.links()[0].name = text);
    this.translate.get('service.new-service').subscribe(text => this.links()[1].name = text);
    this.translate.get('service.edit-service').subscribe(text => this.hiddenLinks()[0].name = text);
    this.translate.get('service.service-details').subscribe(text => this.hiddenLinks()[1].name = text);
  }

  protected getFullRoutePath(path: string) {
    return '/' + SERVICES_PATH + '/' + path;
  }
}
