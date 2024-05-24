import {Component, signal, WritableSignal} from '@angular/core';
import {NEW_SERVICE_PATH, SERVICES_PATH} from "../app.routes";
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
      path: NEW_SERVICE_PATH,
      name: ''
    }
  ]);

  constructor(
    protected routerService: RouterService,
    private translate: TranslateService
  ) {
    this.translate.get('service.new-service').subscribe(text => this.links()[0].name = text);
  }

  protected getFullRoutePath(path: string) {
    return '/' + SERVICES_PATH + '/' + path;
  }
}
