import {Component} from '@angular/core';
import {MatButton, MatMiniFabButton} from "@angular/material/button";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {TranslateService} from "@ngx-translate/core";

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
  protected activeLanguage: string = this.availableLanguages[0];

  constructor(private translate: TranslateService) {
    this.translate.use(this.activeLanguage);
  }

  onLanguageChange(event: string) {
    console.log(event);
    this.activeLanguage = event;
    this.translate.use(this.activeLanguage);
    //window.location.reload(); TODO zapisywać język w cookiesach
  }
}
