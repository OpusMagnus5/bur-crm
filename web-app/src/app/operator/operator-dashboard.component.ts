import {Component} from '@angular/core';
import {NEW_OPERATOR_PATH, OPERATOR_LIST_PATH, REGISTRY_OPERATOR_PATH} from "../app.routes";
import {RouterService} from "../shared/service/router.service";
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {FormsModule} from "@angular/forms";
import {MatButton} from "@angular/material/button";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatTabLink, MatTabNav, MatTabNavPanel} from "@angular/material/tabs";
import {RouterLink, RouterOutlet} from "@angular/router";

@Component({
  selector: 'app-operator-dashboard',
  standalone: true,
  imports: [
    FormsModule,
    MatButton,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    TranslateModule,
    MatTabLink,
    MatTabNav,
    MatTabNavPanel,
    RouterOutlet,
    RouterLink
  ],
  templateUrl: './operator-dashboard.component.html',
  styleUrl: './operator-dashboard.component.css'
})
export class OperatorDashboardComponent {

  protected links: { path: string, name: string }[] = [
    {
      path: OPERATOR_LIST_PATH,
      name: ''
    },
    {
      path: NEW_OPERATOR_PATH,
      name: ''
    }
  ];

  constructor(protected routerService: RouterService, private translate: TranslateService) {
    translate.get('operator.operator-list').subscribe(text => this.links[0].name = text);
    translate.get('operator.new-operator').subscribe(text => this.links[1].name = text);
  }

  protected getFullRoutePath(path: string) {
    return '/' + REGISTRY_OPERATOR_PATH + '/' + path;
  }
}
