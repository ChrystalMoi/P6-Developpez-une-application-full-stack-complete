import { Routes } from '@angular/router';

import { HomeComponent } from './features/auth/components/home/home.component';
import { MeComponent } from './features/auth/components/me/me.component';
import { ConnexionComponent } from './features/auth/components/connexion/connexion.component';
import { InscriptionComponent } from './features/auth/components/inscription/inscription.component';
import { NotFoundComponent } from './component/not-found/not-found.component';
import { FormulaireComponent } from './features/articles/components/formulaire/formulaire.component';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', component: HomeComponent }, // Route pour la page d'accueil - Home

  { path: 'me', component: MeComponent, canActivate: [AuthGuard] }, // Route pour le composant Me

  { path: 'login', component: ConnexionComponent }, // Route pour la page de Connexion

  { path: 'register', component: InscriptionComponent }, // Route pour la page d'Inscription

  {
    path: 'article',
    component: FormulaireComponent,
    canActivate: [AuthGuard],
  }, // Route pour créer un article

  { path: '**', component: NotFoundComponent }, // Route pour la page d'erreur (TOUJOURS EN DERNIER)
];
