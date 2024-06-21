import {NavigationEnd, Router} from "@angular/router";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {PermissionsService} from "../../auth/permissions.service";

@Injectable({providedIn: "root"})
export class RouterService {

  private activeUrl: string = '';
  private routerEventObserver: Observable<any>;

  constructor(
    private router: Router,
    private permissions: PermissionsService
  ) {
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

  isActivePath(path: string): boolean {
    return this.activeUrl === path;
  }

  isPartOfActivePath(path: string): boolean {
    return this.activeUrl.includes(path);
  }

  isVisiblePath(path: string): boolean {
    return this.permissions.canSee(path);
  }
}
