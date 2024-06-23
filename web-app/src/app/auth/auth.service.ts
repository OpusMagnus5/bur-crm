import {Injectable, signal, WritableSignal} from "@angular/core";
import {LoginResponse, UserRole} from "../user/model/user-dtos";
import {CookieService} from "ngx-cookie-service";
import {UserHttpService} from "../user/service/user-http.service";
import {Observable, tap} from "rxjs";

@Injectable({ providedIn: "root" })
export class AuthService {

  private authData: LoginResponse | null = null;
  private roles: UserRole[] = [];
  public readonly valid: WritableSignal<boolean> = signal(false);

  constructor(
    private cookie: CookieService,
    private userHttp: UserHttpService
  ) {
    const auth = this.cookie.get('auth');
    if (auth) {
      const parsedAuth: LoginResponse = <LoginResponse> JSON.parse(auth);
      const authData: LoginResponse = <LoginResponse>{ ...parsedAuth, expires: new Date(parsedAuth.expires) };
      if (this.isAuthValid(authData)) {
        this.authData = authData;
        this.valid.set(true);
      } else {
        this.authData = null;
        this.valid.set(false);
        this.deleteAuthCookie();
      }
    }
  }

  logout(): Observable<void> {
    return this.userHttp.logout().pipe(tap(() => {
      this.authData = null;
      this.valid.set(false);
      this.roles = [];
    }));
  }

  login(data: LoginResponse | null): void {
    this.authData = data;
    this.roles = data?.roles ? data.roles : [];
    if (this.isAuthValid(data)) {
      this.valid.set(true);
      const authJson = JSON.stringify(data);
      this.cookie.set('auth', authJson, { expires: data!.expires, secure: false, sameSite: 'Strict', path: '/'});
    } else {
      this.valid.set(false);
      this.deleteAuthCookie();
    }
  }

  isManager(): boolean {
    return this.roles.includes(UserRole.MANAGER);
  }

  isUser(): boolean {
    return this.roles.includes(UserRole.USER);
  }

  getAuthData(): LoginResponse | null {
    return this.authData
  }

  isAuthValid(authData?: LoginResponse | null): boolean {
    if (authData === undefined) {
      return !!this.authData && this.authData.expires > new Date();
    }
    return !!authData && authData.expires > new Date();
  }

  private deleteAuthCookie(): void {
    this.cookie.delete('auth', '/', undefined, false, 'Strict');
  }
}
