import {Injectable} from "@angular/core";
import {TranslateService} from "@ngx-translate/core";
import {MatSnackBar} from "@angular/material/snack-bar";

@Injectable({providedIn: "root"})
export class SnackbarService {

  constructor(
    private translator: TranslateService,
    private snackBar: MatSnackBar
  ) {
  }

  public openTopCenterSnackbar(message: string): void {
    this.translator.get('common.close-button').subscribe(buttonLabel => {
      this.snackBar.open(message, buttonLabel, {
        horizontalPosition: "center",
        verticalPosition: "top",
        duration: 3000
      });
    });
  }
}
