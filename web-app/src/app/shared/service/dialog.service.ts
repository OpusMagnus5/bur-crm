import {Injectable, OnDestroy} from "@angular/core";
import {MatDialog} from "@angular/material/dialog";
import {DeleteRecordConfirmationComponent} from "../component/delete-record-confirmation.component";
import {DeleteConfirmationDataInterface} from "../model/delete-confirmation-data.interface";
import {SubscriptionManager} from "../util/subscription-manager";
import {DialogDataInterface} from "../model/dialog-data.interface";

@Injectable()
export class DialogService implements OnDestroy {

  private subscriptions: SubscriptionManager = new SubscriptionManager();

  constructor(
    private dialog: MatDialog
  ) {
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubcribeAll();
  }

  public openDeleteConfirmation(data: DeleteConfirmationDataInterface): void {
    const dialogRef = this.dialog.open(DeleteRecordConfirmationComponent, {data: data});
    this.subscriptions.add(dialogRef.componentInstance.deleteConfirmation.subscribe(doRemove => {
      if (doRemove) {
        dialogRef.close();
        data.removeCallback(data.callbackArgument);
      }
    }));
  }

  public openDetailsDialog(data: DialogDataInterface): void {
    const dialogRef = this.dialog.open(data.component, data.config);
    this.subscriptions.add(dialogRef.componentInstance.updateConfirmation.subscribe(doUpdate => {
      if (doUpdate) {
        dialogRef.close();
        data.callback(data.callbackArguments);
      }
    }));
  }
}
