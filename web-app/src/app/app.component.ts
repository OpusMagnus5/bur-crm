import {Component} from '@angular/core';
import {RouterOutlet} from '@angular/router';

@Component({ //mozna stworzyc za pomoca CLI
  selector: 'app-root', //selektor ktory uzywamy w html, mozemy rowniez uzywac atrybutu lub klasy
  standalone: true, //jesli chcemy standalone
  imports: [RouterOutlet], //tu dodajemy inne Componenty zamiast @NgModule
  templateUrl: './app.component.html', //tu html
  styleUrl: './app.component.css' //tu css
})
export class AppComponent {
  title = 'web-app';
  isAllowed = true;
}
