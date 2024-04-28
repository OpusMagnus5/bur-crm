import {Component} from '@angular/core';
import {MatButton, MatMiniFabButton} from "@angular/material/button";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {TranslateService} from "@ngx-translate/core";
import {CookieService} from "ngx-cookie-service";

@Component({
  selector: 'change-language',
  standalone: true,
  imports: [
    MatButton,
    MatMenuTrigger,
    MatMenu,
    MatMenuItem,
    MatMiniFabButton
  ],
  templateUrl: './change-language.component.html',
  styleUrl: './change-language.component.css'
})
export class ChangeLanguageComponent {

  protected availableLanguages: string[] = ['pl', 'en'];
  protected activeLanguage: string = this.getLanguage();

  constructor(
    private translate: TranslateService,
    private cookie: CookieService
  ) {
    this.translate.use(this.activeLanguage);
  }

  onLanguageChange(lang: string) {
    this.activeLanguage = lang;
    this.cookie.set('client-language', lang, {expires: new Date(2300, 12), secure: false, sameSite: 'Lax', path: '/'})
    this.translate.use(this.activeLanguage);
    window.location.reload();
  }

  private getLanguage(): string {
    const lang: string = this.cookie.get('client-language');
    if (lang) {
      return this.activeLanguage = lang;
    }
    this.cookie.set('client-language', this.availableLanguages[0], {expires: new Date(2300, 12), secure: false, sameSite: 'Lax', path: '/'})
    return this.availableLanguages[0];
  }
}
