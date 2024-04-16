import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {FormsModule} from '@angular/forms'
import {NgClass, NgForOf, NgIf, NgStyle} from "@angular/common";

@Component({ //mozna stworzyc za pomoca CLI 'ng generate component nazwa'
  selector: 'app-root', //selektor ktory uzywamy w html, mozemy rowniez uzywac atrybutu lub klasy
  standalone: true, //jesli chcemy standalone
  imports: [RouterOutlet, FormsModule, NgForOf, NgClass, NgStyle, NgIf], //tu dodajemy inne Componenty zamiast @NgModule || FormModule jest potrzebny do two-way binding
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
