import {Component, Inject} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle
} from "@angular/material/dialog";
import {ProgramDetailsResponseInterface} from "./model/program-details-response.interface";
import {TranslateModule} from "@ngx-translate/core";
import {MatDivider} from "@angular/material/divider";
import {LocalizedDatePipe} from "../shared/pipe/localized-date.pipe";
import {ShowNoDataDirective} from "../shared/directive/show-no-data.directive";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-operator-details',
  standalone: true,
  imports: [
    TranslateModule,
    MatDialogContent,
    MatDialogTitle,
    MatDivider,
    LocalizedDatePipe,
    ShowNoDataDirective,
    MatDialogActions,
    MatButton,
    MatDialogClose
  ],
  templateUrl: './program-details.component.html'
})
export class ProgramDetailsComponent {

  constructor(@Inject(MAT_DIALOG_DATA) protected data: ProgramDetailsResponseInterface) {
  }
}
