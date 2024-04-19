import { ApplicationConfig } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [provideRouter(routes)], //prawodpodbnie równiez tutaj musismy strzyknać jesli chcemy wstrzykiwac do innego servisu servis
};
