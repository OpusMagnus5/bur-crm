import {ComponentType} from "@angular/cdk/overlay";
import {MatDialogConfig} from "@angular/material/dialog";
import {OnSubmitInterface} from "./on-submit.interface";

export interface DialogDataInterface {
  component: ComponentType<OnSubmitInterface>,
  config: MatDialogConfig,
  callbackArguments: any[],
  callback: (...args: any[]) => void
}
