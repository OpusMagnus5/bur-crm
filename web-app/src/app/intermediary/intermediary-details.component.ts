import {Component, Inject} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle
} from "@angular/material/dialog";
import {LocalizedDatePipe} from "../shared/pipe/localized-date.pipe";
import {MatButton} from "@angular/material/button";
import {MatDivider} from "@angular/material/divider";
import {NoDataIfEmptyDirective} from "../shared/directive/no-data-if-empty.directive";
import {TranslateModule} from "@ngx-translate/core";
import {IntermediaryDetailsResponse} from "./intermediary-dtos";

@Component({
  selector: 'app-intermediary-details',
  standalone: true,
  imports: [
    LocalizedDatePipe,
    MatButton,
    MatDialogActions,
    MatDialogContent,
    MatDialogTitle,
    MatDivider,
    NoDataIfEmptyDirective,
    TranslateModule,
    MatDialogClose
  ],
  templateUrl: './intermediary-details.component.html'
})
export class IntermediaryDetailsComponent {

  constructor(
    @Inject(MAT_DIALOG_DATA) protected data: IntermediaryDetailsResponse
  ) {
  }
}
