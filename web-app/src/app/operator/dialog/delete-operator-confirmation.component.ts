import {Component} from '@angular/core';
import {Subject} from "rxjs";
import {TranslateModule} from "@ngx-translate/core";
import {MatDialogActions, MatDialogClose, MatDialogContent, MatDialogTitle} from "@angular/material/dialog";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-delete-operator-confirmation',
  standalone: true,
  imports: [
    TranslateModule,
    MatDialogContent,
    MatButton,
    MatDialogActions,
    MatDialogTitle,
    MatDialogClose
  ],
  templateUrl: './delete-operator-confirmation.component.html',
  styleUrl: './delete-operator-confirmation.component.css'
})
export class DeleteOperatorConfirmationComponent {

  deleteConfirmation: Subject<boolean> = new Subject();

  onDelete() {
    this.deleteConfirmation.next(true);
  }
}
