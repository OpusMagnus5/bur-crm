import {Component, signal, WritableSignal} from '@angular/core';
import {
  MatDialog,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";
import {MatDivider} from "@angular/material/divider";
import {TranslateModule} from "@ngx-translate/core";
import {MatChip, MatChipSet} from "@angular/material/chips";
import {DatePipe} from "@angular/common";
import {LocalizedDatePipe} from "../shared/pipe/localized-date.pipe";
import {MatButton, MatIconButton} from "@angular/material/button";
import {ShowNoDataDirective} from "../shared/directive/show-no-data.directive";
import {ActivatedRoute} from "@angular/router";
import {UserHttpService} from "./service/user-http.service";
import {GetUseDetailsResponseInterface} from "./model/get-use-details-response.interface";
import {MatIcon} from "@angular/material/icon";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {ResetUserPasswordRequest, ResetUserPasswordResponse} from "./model/user-dtos";
import {ConfirmationDialogComponent} from "../shared/component/confirmation-dialog.component";
import {SubscriptionManager} from "../shared/util/subscription-manager";
import {ResetPasswordDialogComponent} from "./reset-password-dialog.component";

@Component({
  selector: 'app-user-details',
  standalone: true,
  imports: [
    MatDialogTitle,
    MatDialogContent,
    MatDivider,
    TranslateModule,
    MatChipSet,
    MatChip,
    DatePipe,
    LocalizedDatePipe,
    MatButton,
    MatDialogActions,
    MatDialogClose,
    ShowNoDataDirective,
    MatIcon,
    MatMenu,
    MatMenuItem,
    MatIconButton,
    MatMenuTrigger
  ],
  templateUrl: './user-details.component.html',
  styleUrl: './user-details.component.css'
})
export class UserDetailsComponent {

  private readonly id: string;
  private subscriptions: SubscriptionManager = new SubscriptionManager();
  protected readonly userData: WritableSignal<GetUseDetailsResponseInterface | null> = signal(null);

  constructor(
    private route: ActivatedRoute,
    private http: UserHttpService,
    private dialog: MatDialog
  ) {
    this.id = this.route.snapshot.paramMap.get('id')!;
    this.http.getUserDetails(this.id).subscribe(response => this.userData.set(response));
  }

  protected onResetPassword() {
    const dialogRef = this.openConfirmationDialog();
    this.subscriptions.add(dialogRef.componentInstance.deleteConfirmation.subscribe(value => {
      this.resetPasswordOnConfirmation(value, dialogRef);
    }));
  }

  private openConfirmationDialog() {
    return this.dialog.open(ConfirmationDialogComponent, {
        data: { codeForTranslation: 'reset-user-password' }
      }
    );
  }

  private resetPasswordOnConfirmation(value: boolean, dialogRef: MatDialogRef<ConfirmationDialogComponent>) {
    if (value) {
      dialogRef.close();
      const request: ResetUserPasswordRequest = {
        id: this.id,
        userVersion: this.userData()!.version
      };

      this.http.resetPassword(request).subscribe(response => {
        this.openResetPasswordDialog(response);
      });
    }
  }

  private openResetPasswordDialog(response: ResetUserPasswordResponse) {
    this.dialog.open(ResetPasswordDialogComponent, {
      data: response,
      disableClose: true
    })
  }
}
