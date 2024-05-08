import {Component, Inject} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle
} from "@angular/material/dialog";
import {GetUseDetailsResponseInterface} from "./model/get-use-details-response.interface";
import {MatDivider} from "@angular/material/divider";
import {TranslateModule} from "@ngx-translate/core";
import {MatChip, MatChipSet} from "@angular/material/chips";
import {DatePipe} from "@angular/common";
import {LocalizedDatePipe} from "../shared/pipe/localized-date.pipe";
import {MatButton} from "@angular/material/button";
import {NoDataIfEmptyDirective} from "../shared/directive/no-data-if-empty.directive";

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
    NoDataIfEmptyDirective
  ],
  templateUrl: './user-details.component.html',
  styleUrl: './user-details.component.css'
})
export class UserDetailsComponent {

  constructor(@Inject(MAT_DIALOG_DATA) protected data: GetUseDetailsResponseInterface) {
  }

}
