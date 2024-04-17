import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {HeaderComponent} from "./header/header.component";
import { AdministrationSideMenuComponent } from './administration/side-menu/administration-side-menu.component';

@Component({ //mozna stworzyc za pomoca CLI 'ng generate component nazwa'
  selector: 'app-root', //selektor ktory uzywamy w html, mozemy rowniez uzywac atrybutu lub klasy
  standalone: true, //jesli chcemy standalone
  imports: [RouterOutlet, HeaderComponent, AdministrationSideMenuComponent], //tu dodajemy inne Componenty zamiast @NgModule || FormModule jest potrzebny do two-way binding
  templateUrl: './app.component.html', //tu html
  styleUrl: './app.component.css' //tu css
})
export class AppComponent {
  title = 'web-app';
  isAllowed = true;
  serverNames = ['Testservet', 'Testservet2']

  onCreateClick() {
    this.isAllowed = false;
  }

  onPassLetter(event: Event) {
    console.log((<HTMLInputElement>event.target).value)
  }

  getColor() {
    return 'green';
  }
}
