import {Component, Inject} from '@angular/core';
import {Subject} from "rxjs";
import {MatButton} from "@angular/material/button";
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle
} from "@angular/material/dialog";
import {TranslateModule} from "@ngx-translate/core";
import {DeleteConfirmationDataInterface} from "../model/delete-confirmation-data.interface";

@Component({
  selector: 'app-delete-record-confirmation',
  standalone: true,
  imports: [
    MatButton,
    MatDialogActions,
    MatDialogClose,
    MatDialogContent,
    MatDialogTitle,
    TranslateModule
  ],
  templateUrl: './confirmation-dialog.component.html',
  styleUrl: './confirmation-dialog.component.css'
})
export class ConfirmationDialogComponent {

  deleteConfirmation: Subject<boolean> = new Subject();

  constructor(@Inject(MAT_DIALOG_DATA) protected data: DeleteConfirmationDataInterface) {
  }

  protected onDelete() {
    this.deleteConfirmation.next(true);
  }
}
