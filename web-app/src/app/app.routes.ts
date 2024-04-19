import { Routes } from '@angular/router';
import { HeaderComponent } from './header/header.component';

export const routes: Routes = [
  //wpisujemy obiekty z propertką path i który component chcemy załadować
  //trzeba pamietac tez pustej path dla strony głównej
  { path: 'user', component: HeaderComponent },
  { path: 'user/:id', component: HeaderComponent },
  {
    path: 'user',
    component: HeaderComponent,
    children: [{ path: ':id', component: HeaderComponent }], // tutaj zagniezdzone sciezki w stosunku
  }, //dynamicznie mozemy przekazywac dowolną cześć patha dalej
];
