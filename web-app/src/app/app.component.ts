import {Component} from '@angular/core';
import {HeaderComponent} from "./header/header.component";
import {AdministrationSideMenuComponent} from './administration/side-menu/administration-side-menu.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [HeaderComponent, AdministrationSideMenuComponent],
  templateUrl: './app.component.html', //tu html
  styleUrl: './app.component.css', //tu css
})
export class AppComponent {
}
