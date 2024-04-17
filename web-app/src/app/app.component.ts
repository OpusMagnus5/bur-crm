import {Component, EventEmitter, Input, Output, ViewEncapsulation} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {HeaderComponent} from "./header/header.component";
import {AdministrationSideMenuComponent} from './administration/side-menu/administration-side-menu.component';

@Component({ //mozna stworzyc za pomoca CLI 'ng generate component nazwa'
  selector: 'app-root', //selektor ktory uzywamy w html, mozemy rowniez uzywac atrybutu lub klasy
  standalone: true, //jesli chcemy standalone
  imports: [RouterOutlet, HeaderComponent, AdministrationSideMenuComponent], //tu dodajemy inne Componenty zamiast @NgModule || FormModule jest potrzebny do two-way binding
  templateUrl: './app.component.html', //tu html
  styleUrl: './app.component.css', //tu css
  encapsulation: ViewEncapsulation.None //wyłącza enkapsulacje css, stają sie globalne, ShadowDom robi to co none ale za pomocą przeglądarki, nie wszystkie to obslugują
})
export class AppComponent {
  title = 'web-app';
  isAllowed = true;
  serverNames = ['Testservet', 'Testservet2'];
  @Input() bindBesideComponent = 'bindBesideComponent'; //domyślnie zmienne w komponencie nie są dostępne w innych componentach nawet jesli są publiczne
  //Aby upublicznić taką zmienną w górę do rodziców używamy @Input() to znaczy ze oczekujemy zmiany tej wartosci, możemy podać inną nazwę w nawiasach, wtedy musimy się odwoływać po tej nazwie

  @Output() nameOfEvent = new EventEmitter<{name: String, count: Number}>(); //Służy do emitowania eventów z okreslonymi danymi
  //musimy dodać równiez @Output dlatego że wysyłamy coś na zewnatrz, możemy również podać alias w nawiasie

  onCreateClick() {
    this.isAllowed = false;
  }

  onPassLetter(event: Event) {
    console.log((<HTMLInputElement>event.target).value)
  }

  getColor() {
    return 'green';
  }

  emitEvent() {
    this.nameOfEvent.emit({name: 'app-root', count: 1}) // tutaj wywolujemy event  COMPONENT CUSTOM EVENT BINDING
  }

  setDataFromReference(reference: HTMLParagraphElement) { //LOCAL REFERENCE
    console.log(reference.innerText)
  }
}
