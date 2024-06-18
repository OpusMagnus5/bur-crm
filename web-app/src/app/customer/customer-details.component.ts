import {Component, Inject} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle
} from "@angular/material/dialog";
import {CustomerDetailsResponse} from "./customer-dtos";
import {LocalizedDatePipe} from "../shared/pipe/localized-date.pipe";
import {MatButton} from "@angular/material/button";
import {MatDivider} from "@angular/material/divider";
import {ShowNoDataDirective} from "../shared/directive/show-no-data.directive";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-customer-details',
  standalone: true,
  imports: [
    LocalizedDatePipe,
    MatButton,
    MatDialogActions,
    MatDialogContent,
    MatDialogTitle,
    MatDivider,
    ShowNoDataDirective,
    TranslateModule,
    MatDialogClose
  ],
  templateUrl: './customer-details.component.html'
})
export class CustomerDetailsComponent {

  constructor(
    @Inject(MAT_DIALOG_DATA) protected data: CustomerDetailsResponse
  ) {
  }
}
