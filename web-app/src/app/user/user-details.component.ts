import {Component, signal, WritableSignal} from '@angular/core';
import {MatDialogActions, MatDialogClose, MatDialogContent, MatDialogTitle} from "@angular/material/dialog";
import {MatDivider} from "@angular/material/divider";
import {TranslateModule} from "@ngx-translate/core";
import {MatChip, MatChipSet} from "@angular/material/chips";
import {DatePipe} from "@angular/common";
import {LocalizedDatePipe} from "../shared/pipe/localized-date.pipe";
import {MatButton, MatIconButton} from "@angular/material/button";
import {ShowNoDataDirective} from "../shared/directive/show-no-data.directive";
import {ActivatedRoute} from "@angular/router";
import {UserHttpService} from "./service/user-http.service";
import {GetUseDetailsResponseInterface} from "./model/get-use-details-response.interface";
import {MatIcon} from "@angular/material/icon";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";

@Component({
  selector: 'app-user-details',
  standalone: true,
  imports: [
    MatDialogTitle,
    MatDialogContent,
    MatDivider,
    TranslateModule,
    MatChipSet,
    MatChip,
    DatePipe,
    LocalizedDatePipe,
    MatButton,
    MatDialogActions,
    MatDialogClose,
    ShowNoDataDirective,
    MatIcon,
    MatMenu,
    MatMenuItem,
    MatIconButton,
    MatMenuTrigger
  ],
  templateUrl: './user-details.component.html',
  styleUrl: './user-details.component.css'
})
export class UserDetailsComponent {

  private readonly id: string;
  protected readonly userData: WritableSignal<GetUseDetailsResponseInterface | null> = signal(null);

  constructor(
    private route: ActivatedRoute,
    private http: UserHttpService
  ) {
    this.id = this.route.snapshot.paramMap.get('id')!;
    this.http.getUserDetails(this.id).subscribe(response => this.userData.set(response));
  }

  protected onChangePassword() {

  }
}
