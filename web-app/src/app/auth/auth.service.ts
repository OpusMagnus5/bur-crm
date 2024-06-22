import {computed, Injectable, Signal, signal, WritableSignal} from "@angular/core";
import {LoginResponse, UserRole} from "../user/model/user-dtos";
import {CookieService} from "ngx-cookie-service";
import {toObservable} from "@angular/core/rxjs-interop";
import {UserHttpService} from "../user/service/user-http.service";
import {Observable, tap} from "rxjs";
import {Router} from "@angular/router";
import {LOGIN_PATH} from "../app.routes";

@Injectable({ providedIn: "root" })
export class AuthService {

  public readonly authData: WritableSignal<LoginResponse | null> = signal(null);
  public readonly roles: Signal<UserRole[]> = computed(() => {
    const authData = this.authData();
    return authData != null ? authData.roles : []
  });
  public readonly observable = toObservable(this.authData);
  public readonly isValid: Signal<boolean> = computed(() => {
    const authData = this.authData();
    return this.isAuthValid(authData);
  });

  constructor(
    private cookie: CookieService,
    private userHttp: UserHttpService,
    private router: Router,
  ) {
    const auth = this.cookie.get('auth');
    if (auth) {
      const parsedAuth: LoginResponse = <LoginResponse> JSON.parse(auth);
      const authData: LoginResponse = <LoginResponse>{ ...parsedAuth, expires: new Date(parsedAuth.expires) };
      if (this.isAuthValid(authData)) {
        this.authData.set(authData);
      }
    }

    this.observable.subscribe({
      next: value => {
        if (this.isAuthValid(value)) {
          const authJson = JSON.stringify(value);
          this.cookie.set('auth', authJson, { expires: value!.expires, secure: false, sameSite: 'Strict', path: '/'});
        } else if (value === null) {
          this.cookie.delete('auth', '/', undefined, false, 'Strict');
        }
      }
    })
  }

  logout(): Observable<void> {
    return this.userHttp.logout().pipe(tap(() => this.authData.set(null)));
  }

  isManager(): boolean {
    return this.roles().includes(UserRole.MANAGER);
  }

  isUser(): boolean {
    return this.roles().includes(UserRole.USER);
  }

  private isAuthValid(authData: LoginResponse | null): boolean {
    if (!!authData && authData.expires > new Date()) {
      return true;
    } else {
      this.logout().subscribe(() => this.router.navigate(['/', LOGIN_PATH]));
      return false;
    }
  }
}
