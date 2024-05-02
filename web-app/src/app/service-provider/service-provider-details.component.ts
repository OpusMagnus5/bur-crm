import {Component, Inject} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogTitle
} from "@angular/material/dialog";
import {ServiceProviderDetailsResponseInterface} from "./model/service-provider-details-response.interface";
import {TranslateModule} from "@ngx-translate/core";
import {MatDivider} from "@angular/material/divider";
import {LocalizedDatePipe} from "../shared/pipe/localized-date.pipe";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-service-provider-details',
  standalone: true,
  imports: [
    MatDialogTitle,
    MatDialogContent,
    TranslateModule,
    MatDivider,
    LocalizedDatePipe,
    MatButton,
    MatDialogActions,
    MatDialogClose
  ],
  templateUrl: './service-provider-details.component.html',
  styleUrl: './service-provider-details.component.css'
})
export class ServiceProviderDetailsComponent {

  constructor(@Inject(MAT_DIALOG_DATA) protected data: ServiceProviderDetailsResponseInterface) {
  }
}
