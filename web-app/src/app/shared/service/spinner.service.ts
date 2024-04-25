import {Injectable, OnDestroy} from "@angular/core";
import {Overlay, OverlayRef} from "@angular/cdk/overlay";
import {ComponentPortal} from "@angular/cdk/portal";
import {scan, Subject, Subscription} from "rxjs";
import {map} from "rxjs/operators";
import {SpinnerComponent} from "../component/spinner.component";

@Injectable({providedIn: "root"})
export class SpinnerService implements OnDestroy {

  private overlayRef: OverlayRef = this.createOverlayForSpinner();
  spin: Subject<boolean> = new Subject();
  private spinSubscription: Subscription;

  constructor(private overlay: Overlay) {
    this.spinSubscription = this.spin.asObservable()
      .pipe(
        map(val => val ? 1 : -1),
        scan((acc, val) => acc + val >= 0 ? acc + val : 0, 0)
      ).subscribe(result => {
        if (result === 1) {
          !this.overlayRef.hasAttached() ? this.showSpinner() : null
        } else if (result === 0) {
          this.overlayRef.hasAttached() ? this.stopSpinner() : null
        }
    })
  }

  ngOnDestroy(): void {
        this.spinSubscription.unsubscribe();
    }

  private createOverlayForSpinner(): OverlayRef {
    return this.overlay.create({
      hasBackdrop: true,
      positionStrategy: this.overlay.position()
        .global()
        .centerHorizontally()
        .centerVertically()
    })
  }

  private showSpinner() {
    this.overlayRef.attach(new ComponentPortal(SpinnerComponent));
  }

  private stopSpinner() {
      this.overlayRef.detach();
  }
}
