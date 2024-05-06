import {Subject} from "rxjs";

export interface OnSubmitInterface {
  updateConfirmation: Subject<boolean>;
}
