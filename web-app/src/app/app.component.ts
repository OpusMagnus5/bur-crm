import {Component} from '@angular/core';
import {HeaderComponent} from "./header/header.component";
import {AdministrationSideMenuComponent} from './administration/side-menu/administration-side-menu.component';
import {AdministrationUsersMenuComponent} from "./administration/users/menu/administration-users-menu.component";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [HeaderComponent, AdministrationSideMenuComponent, AdministrationUsersMenuComponent],
  templateUrl: './app.component.html', //tu html
  styleUrl: './app.component.css', //tu css
})
export class AppComponent {
}
