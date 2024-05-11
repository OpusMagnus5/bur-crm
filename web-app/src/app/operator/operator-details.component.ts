import {Component, Inject} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle
} from "@angular/material/dialog";
import {OperatorDetailsResponseInterface} from "./model/operator-details-response.interface";
import {LocalizedDatePipe} from "../shared/pipe/localized-date.pipe";
import {MatButton} from "@angular/material/button";
import {MatDivider} from "@angular/material/divider";
import {TranslateModule} from "@ngx-translate/core";
import {NoDataIfEmptyDirective} from "../shared/directive/no-data-if-empty.directive";
import {MatList, MatListItem} from "@angular/material/list";

@Component({
  selector: 'app-operator-details',
  standalone: true,
  imports: [
    LocalizedDatePipe,
    MatButton,
    MatDialogActions,
    MatDialogContent,
    MatDialogTitle,
    MatDivider,
    TranslateModule,
    MatDialogClose,
    NoDataIfEmptyDirective,
    MatList,
    MatListItem
  ],
  templateUrl: './operator-details.component.html',
  styleUrl: './operator-details.component.css'
})
export class OperatorDetailsComponent {

  constructor(@Inject(MAT_DIALOG_DATA) protected data: OperatorDetailsResponseInterface) {
  }
}
