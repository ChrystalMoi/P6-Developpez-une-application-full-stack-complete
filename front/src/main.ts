import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import * as fr from '@angular/common/locales/fr';
import { AppComponent } from './app/app.component';
import { registerLocaleData } from '@angular/common';

registerLocaleData(fr.default);

bootstrapApplication(AppComponent, appConfig).catch((err) =>
  console.error(err)
);
