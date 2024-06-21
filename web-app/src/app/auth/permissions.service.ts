import {inject, Injectable} from "@angular/core";
import {AuthService} from "./auth.service";
import {
  ActivatedRouteSnapshot,
  CanActivateFn,
  GuardResult,
  MaybeAsync,
  Router,
  RouterStateSnapshot
} from "@angular/router";
import {ADMINISTRATION_PATH, BASE_PATH, LOGIN_PATH, NOT_FOUND_PATH, REGISTRY_PATH, SERVICES_PATH} from "../app.routes";

@Injectable({ providedIn: "root" })
export class PermissionsService {

  private readonly MANAGER_PATHS: string[] = [ADMINISTRATION_PATH];
  private readonly USER_PATH: string[] = [REGISTRY_PATH, SERVICES_PATH, BASE_PATH]
  private readonly GUEST_PATH: string[] = [LOGIN_PATH, NOT_FOUND_PATH]

  constructor(
    private auth: AuthService,
    private router: Router
  ) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): MaybeAsync<GuardResult> {
    if (!this.auth.isValid()) {
      return this.getRedirectUrlTree();
    }

    const managerPath = this.MANAGER_PATHS.find(m => state.url.startsWith('/' + m));
    const userPath = this.USER_PATH.find(u => state.url.startsWith('/' + u));
    const guestPath = this.GUEST_PATH.find(g => state.url.startsWith('/' + g));
    if (managerPath && !this.auth.isManager()) {
      return this.getRedirectUrlTree();
    } else if (userPath && !this.auth.isUser()) {
      return this.getRedirectUrlTree();
    } else if (guestPath) {
      return true
    }

    return true;
  }

  canSee(path: string): boolean {
    const guestPath = this.GUEST_PATH.find(g => path.startsWith(g));
    if (!this.auth.isValid() && !guestPath) {
      return false;

    }
    const userPath = this.USER_PATH.find(u => path.startsWith(u));
    const managerPath = this.MANAGER_PATHS.find(m => path.startsWith(m));
    if (managerPath && !this.auth.isManager()) {
      return false
    } else if (userPath && !this.auth.isUser()) {
      return false
    } else if (guestPath) {
      return true
    }

    return true;
  }

  private getRedirectUrlTree() {
    return this.router.createUrlTree(['/login']);
  }
}

export const canActivate: CanActivateFn = (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
  return inject(PermissionsService).canActivate(route, state);
}
