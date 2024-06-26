import {Component, signal, WritableSignal} from '@angular/core';
import {GetUseDetailsResponseInterface} from "../user/model/get-use-details-response.interface";
import {UserHttpService} from "../user/service/user-http.service";
import {AuthService} from "../auth/auth.service";
import {LocalizedDatePipe} from "../shared/pipe/localized-date.pipe";
import {MatChip, MatChipSet} from "@angular/material/chips";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {TranslateModule} from "@ngx-translate/core";
import {ShowNoDataDirective} from "../shared/directive/show-no-data.directive";
import {RouterLink} from "@angular/router";
import {ACCOUNT_CHANGE_PASSWORD_PATH} from "../app.routes";

@Component({
  selector: 'app-account-details',
  standalone: true,
  imports: [
    LocalizedDatePipe,
    MatChip,
    MatChipSet,
    MatIcon,
    MatIconButton,
    MatMenu,
    MatMenuItem,
    TranslateModule,
    MatMenuTrigger,
    ShowNoDataDirective,
    RouterLink
  ],
  templateUrl: './account-details.component.html',
  styles: ['@tailwind base']
})
export class AccountDetailsComponent {

  protected readonly ACCOUNT_CHANGE_PASSWORD_PATH = ACCOUNT_CHANGE_PASSWORD_PATH;
  private readonly id: string;

  protected readonly userData: WritableSignal<GetUseDetailsResponseInterface | null> = signal(null);
  constructor(
    private http: UserHttpService,
    private auth: AuthService

  ) {
    this.id = this.auth.getAuthData()!.id;
    this.http.getUserDetails(this.id).subscribe(response => this.userData.set(response));
  }
}
