import {Component, signal, WritableSignal} from '@angular/core';
import {SubscriptionManager} from "../shared/util/subscription-manager";
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
    ShowNoDataDirective
  ],
  templateUrl: './account-details.component.html',
  styles: ['@tailwind base']
})
export class AccountDetailsComponent {

  private readonly id: string;
  private subscriptions: SubscriptionManager = new SubscriptionManager();
  protected readonly userData: WritableSignal<GetUseDetailsResponseInterface | null> = signal(null);

  constructor(
    private http: UserHttpService,
    private auth: AuthService

  ) {
    this.id = this.auth.getAuthData()!.id;
    this.http.getUserDetails(this.id).subscribe(response => this.userData.set(response));
  }
}
