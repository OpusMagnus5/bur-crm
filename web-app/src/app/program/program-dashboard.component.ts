import {Component, signal, WritableSignal} from '@angular/core';
import {NEW_PROGRAM_PATH, PROGRAM_LIST_PATH, REGISTRY_PROGRAM_PATH} from "../app.routes";
import {RouterService} from "../shared/service/router.service";
import {TranslateService} from "@ngx-translate/core";
import {MatTabLink, MatTabNav, MatTabNavPanel} from "@angular/material/tabs";
import {RouterLink, RouterOutlet} from "@angular/router";

@Component({
  selector: 'app-program-dashboard',
  standalone: true,
  imports: [
    MatTabLink,
    MatTabNav,
    MatTabNavPanel,
    RouterOutlet,
    RouterLink
  ],
  templateUrl: './program-dashboard.component.html',
})
export class ProgramDashboardComponent {

  protected links: WritableSignal<{ path: string, name: string}[]> = signal([
    {
      path: PROGRAM_LIST_PATH,
      name: ''
    },
    {
      path: NEW_PROGRAM_PATH,
      name:''
    }
  ]);

  constructor(
    protected routerService: RouterService,
    private translate: TranslateService
  ) {
    translate.get('program.program-list').subscribe(text => this.links()[0].name = text);
    translate.get('program.new-program').subscribe(text => this.links()[1].name = text);
  }

  protected getFullRoutePath(path: string) {
    return '/' + REGISTRY_PROGRAM_PATH + '/' + path;
  }
}
