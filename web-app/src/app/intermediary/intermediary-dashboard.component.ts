import {Component} from '@angular/core';
import {INTERMEDIARY_LIST_PATH, NEW_INTERMEDIARY_PATH, REGISTRY_INTERMEDIARY_PATH} from "../app.routes";
import {RouterService} from "../shared/service/router.service";
import {TranslateService} from "@ngx-translate/core";
import {MatTabLink, MatTabNav, MatTabNavPanel} from "@angular/material/tabs";
import {RouterLink, RouterOutlet} from "@angular/router";

@Component({
  selector: 'app-intermediary-dashboard',
  standalone: true,
  imports: [
    MatTabLink,
    MatTabNav,
    MatTabNavPanel,
    RouterOutlet,
    RouterLink
  ],
  templateUrl: './intermediary-dashboard.component.html'
})
export class IntermediaryDashboardComponent {

  protected links: {path: string, name: string}[] = [
    {
      path: INTERMEDIARY_LIST_PATH,
      name: ''
    },
    {
      path: NEW_INTERMEDIARY_PATH,
      name: ''
    }
  ];

  constructor(
    protected routerService: RouterService,
    private translate: TranslateService
  ) {
    this.translate.get('intermediary.new-intermediary').subscribe(text => {
      this.links[1].name = text;
    });
    this.translate.get('intermediary.intermediary-list').subscribe(text => {
      this.links[0].name = text;
    });
  }

  protected getFullRoutePath(path: string) {
    return '/' + REGISTRY_INTERMEDIARY_PATH + '/' + path;
  }
}
