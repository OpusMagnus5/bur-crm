import {Component, Inject} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle
} from "@angular/material/dialog";
import {CoachDetailsResponse} from "./coach-dtos";
import {TranslateModule} from "@ngx-translate/core";
import {MatDivider} from "@angular/material/divider";
import {LocalizedDatePipe} from "../shared/pipe/localized-date.pipe";
import {NoDataIfEmptyDirective} from "../shared/directive/no-data-if-empty.directive";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-coach-details',
  standalone: true,
  imports: [
    TranslateModule,
    MatDialogTitle,
    MatDialogContent,
    MatDivider,
    LocalizedDatePipe,
    NoDataIfEmptyDirective,
    MatDialogActions,
    MatButton,
    MatDialogClose
  ],
  templateUrl: './coach-details.component.html'
})
export class CoachDetailsComponent {

  constructor(
    @Inject(MAT_DIALOG_DATA) protected data: CoachDetailsResponse
  ) {
  }
}
