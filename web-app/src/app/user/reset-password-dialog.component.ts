import {Component, Inject} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle
} from "@angular/material/dialog";
import {MatButton} from "@angular/material/button";
import {TranslateModule} from "@ngx-translate/core";
import {ResetUserPasswordResponse} from "./model/user-dtos";

@Component({
  selector: 'app-reset-password-dialog',
  standalone: true,
  imports: [
    MatButton,
    MatDialogActions,
    MatDialogClose,
    MatDialogContent,
    MatDialogTitle,
    TranslateModule
  ],
  templateUrl: './reset-password-dialog.component.html'
})
export class ResetPasswordDialogComponent {

   constructor(@Inject(MAT_DIALOG_DATA) protected data: ResetUserPasswordResponse) {
   }
}
