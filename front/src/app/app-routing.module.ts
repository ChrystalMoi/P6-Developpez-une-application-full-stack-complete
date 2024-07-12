/* Gestion des routes (navigation) */

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './features/auth/home/home.component';
import { MeComponent } from './component/me/me.component';
import { ConnexionComponent } from './features/auth/connexion/connexion.component';
import { InscriptionComponent } from './features/auth/inscription/inscription.component';
import { NotFoundComponent } from './component/not-found/not-found.component';

const routes: Routes = [
  { path: '', component: HomeComponent }, // Route pour la page d'accueil - Home
  { path: 'me', component: MeComponent }, // Route pour le composant Me
  { path: 'login', component: ConnexionComponent }, // Route pour la page de Connexion
  { path: 'register', component: InscriptionComponent }, // Route pour la page d'Inscription
  { path: '**', component: NotFoundComponent } // Route pour la page d'erreur
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
