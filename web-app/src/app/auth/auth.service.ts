import {computed, Injectable, Signal, signal, WritableSignal} from "@angular/core";
import {LoginResponse, UserRole} from "../user/model/user-dtos";
import {CookieService} from "ngx-cookie-service";
import {toObservable} from "@angular/core/rxjs-interop";

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
    console.log(this.isAuthValid(authData));
    return this.isAuthValid(authData);
  });

  constructor(
    private cookie: CookieService
  ) {
    const auth = this.cookie.get('auth');
    console.log('authcookie' + auth)
    if (auth) {
      const authData: LoginResponse = JSON.parse(auth);
      if (this.isAuthValid(authData)) {
        this.authData.set(authData);
      }
    }

    this.observable.subscribe({
      next: value => {
        console.log('observable'+ value);
        if (this.isAuthValid(value)) {
          const authJson = JSON.stringify(value);
          this.cookie.set('auth', authJson, { expires: value!.expires, secure: false, sameSite: 'Lax', path: '/'});
        }
      }
    })
  }

  private isAuthValid(authData: LoginResponse | null): boolean {
    return !!authData && authData.expires > new Date();
  }
}
