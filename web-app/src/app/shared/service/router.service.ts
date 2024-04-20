import {NavigationEnd, Router} from "@angular/router";
import {Injectable} from "@angular/core";

@Injectable({providedIn: "root"})
export class RouterService {

  activeUrl: string = '';

  constructor(private router: Router) {
    router.events.subscribe((event: any) => {
      if (event instanceof NavigationEnd) {
        this.activeUrl = event.url;
        console.log(event.url);
      }
    });
  }
}
