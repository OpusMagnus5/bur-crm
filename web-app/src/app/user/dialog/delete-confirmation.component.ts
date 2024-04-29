import {Component} from '@angular/core';
import {MatDialogActions, MatDialogClose, MatDialogContent, MatDialogTitle} from "@angular/material/dialog";
import {TranslateModule} from "@ngx-translate/core";
import {MatButton} from "@angular/material/button";
import {Subject} from "rxjs";

@Component({
  selector: 'app-delete-confirmation',
  standalone: true,
  imports: [
    MatDialogTitle,
    TranslateModule,
    MatDialogContent,
    MatDialogActions,
    MatButton,
    MatDialogClose
  ],
  templateUrl: './delete-confirmation.component.html',
  styleUrl: './delete-confirmation.component.css'
})
export class DeleteConfirmationComponent {

  deleteConfirmation: Subject<boolean> = new Subject();

  onDelete() {
    this.deleteConfirmation.next(true);
  }
}
