/* Configuration de l'application (déclaration de composants et modules) */

import { NgModule } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './features/auth/home/home.component';
import { MeComponent } from './component/me/me.component';
import { NotFoundComponent } from './component/not-found/not-found.component';
import { ConnexionComponent } from './features/auth/connexion/connexion.component';
import { InscriptionComponent } from './features/auth/inscription/inscription.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    MeComponent,
    NotFoundComponent,
    ConnexionComponent,
    InscriptionComponent
  ],

  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatButtonModule,
  ],

  providers: [],

  bootstrap: [AppComponent],
})
export class AppModule {}
