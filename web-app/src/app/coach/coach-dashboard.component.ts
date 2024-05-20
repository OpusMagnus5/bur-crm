import {Component} from '@angular/core';
import {COACH_LIST_PATH, NEW_COACH_PATH, REGISTRY_COACH_PATH} from "../app.routes";
import {RouterService} from "../shared/service/router.service";
import {TranslateService} from "@ngx-translate/core";
import {MatTabLink, MatTabNav, MatTabNavPanel} from "@angular/material/tabs";
import {RouterLink, RouterOutlet} from "@angular/router";

@Component({
  selector: 'app-coach-dashboard',
  standalone: true,
  imports: [
    MatTabLink,
    MatTabNav,
    MatTabNavPanel,
    RouterOutlet,
    RouterLink
  ],
  templateUrl: './coach-dashboard.component.html'
})
export class CoachDashboardComponent {

  protected links: {path: string, name: string}[] = [
    {
      path: COACH_LIST_PATH,
      name: ''
    },
    {
      path: NEW_COACH_PATH,
      name: ''
    }
  ];

  constructor(
    protected routerService: RouterService,
    private translate: TranslateService
  ) {
    this.translate.get('coach.new-coach').subscribe(text => {
      this.links[1].name = text;
    });
    this.translate.get('coach.coach-list').subscribe(text => {
      this.links[0].name = text;
    });
  }

  protected getFullRoutePath(path: string) {
    return '/' + REGISTRY_COACH_PATH + '/' + path;
  }
}
