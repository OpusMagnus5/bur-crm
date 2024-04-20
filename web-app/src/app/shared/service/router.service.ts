import {NavigationEnd, Router} from "@angular/router";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";

@Injectable({providedIn: "root"})
export class RouterService {

  activeUrl: string = '';
  private routerEventObserver: Observable<any>;

  constructor(private router: Router) {
    this.routerEventObserver = router.events;

    this.routerEventObserver.subscribe((event: any) => {
      if (event instanceof NavigationEnd) {
        this.activeUrl = event.url;
      }
      if (this.activeUrl === '/') {
        this.activeUrl = '';
      }
    });
  }
}
