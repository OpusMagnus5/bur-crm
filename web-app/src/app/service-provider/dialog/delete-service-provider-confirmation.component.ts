import {Component} from '@angular/core';
import {Subject} from "rxjs";
import {TranslateModule} from "@ngx-translate/core";
import {MatDialogActions, MatDialogClose, MatDialogContent, MatDialogTitle} from "@angular/material/dialog";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-delete-service-provider-confirmation',
  standalone: true,
  imports: [
    TranslateModule,
    MatDialogContent,
    MatDialogTitle,
    MatDialogActions,
    MatButton,
    MatDialogClose
  ],
  templateUrl: './delete-service-provider-confirmation.component.html',
  styleUrl: './delete-service-provider-confirmation.component.css'
})
export class DeleteServiceProviderConfirmationComponent {

  deleteConfirmation: Subject<boolean> = new Subject();

  onDelete() {
    this.deleteConfirmation.next(true);
  }
}
