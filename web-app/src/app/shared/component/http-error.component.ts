import {Component, Inject} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle
} from "@angular/material/dialog";
import {TranslateModule} from "@ngx-translate/core";
import {HttpErrorResponseInterface} from "../model/http-error-response.interface";
import {LocalizedDatePipe} from "../pipe/localized-date.pipe";
import {MatDivider} from "@angular/material/divider";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-http-error',
  standalone: true,
  imports: [
    MatDialogTitle,
    TranslateModule,
    MatDialogContent,
    LocalizedDatePipe,
    MatDivider,
    MatButton,
    MatDialogActions,
    MatDialogClose
  ],
  templateUrl: './http-error.component.html',
  styleUrl: './http-error.component.css'
})
export class HttpErrorComponent {

  constructor(@Inject(MAT_DIALOG_DATA) protected data: HttpErrorResponseInterface) {
  }
}
