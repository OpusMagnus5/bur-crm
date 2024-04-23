import {Component} from '@angular/core';
import {MatButton, MatMiniFabButton} from "@angular/material/button";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'change-langauage',
  standalone: true,
  imports: [
    MatButton,
    MatMenuTrigger,
    MatMenu,
    MatMenuItem,
    MatMiniFabButton
  ],
  templateUrl: './change-langauage.component.html',
  styleUrl: './change-langauage.component.css'
})
export class ChangeLangauageComponent {

  protected availableLanguages: string[] = ['pl', 'en'];
  protected activeLanguage: string = this.availableLanguages[0];

  constructor(private trasnlate: TranslateService) {
  }

  onLanguageChange(event: string) {
    console.log(event);
    this.activeLanguage = event;
    this.trasnlate.use(this.activeLanguage);
    //window.location.reload(); TODO zapisywać język w cookiesach
  }
}
