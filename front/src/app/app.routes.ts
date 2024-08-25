import { Routes } from '@angular/router';

import { HomeComponent } from './features/auth/components/home/home.component';
import { MeComponent } from './features/auth/components/me/me.component';
import { ConnexionComponent } from './features/auth/components/connexion/connexion.component';
import { InscriptionComponent } from './features/auth/components/inscription/inscription.component';
import { NotFoundComponent } from './component/not-found/not-found.component';
import { FormulaireComponent } from './features/articles/components/formulaire/formulaire.component';
import { AuthGuard } from './guards/auth.guard';
import { ListeArticlesComponent } from './features/articles/components/liste-articles/liste-articles.component';
import { DetailArticleComponent } from './features/articles/components/detail-article/detail-article.component';
import { LayoutAuthComponent } from './component/layout-auth/layout-auth.component';
import { LayoutConnectedComponent } from './component/layout-connected/layout-connected.component';

export const routes: Routes = [
  { path: '', component: HomeComponent }, // Route pour la page d'accueil - Home

  {
    path: '',
    component: LayoutAuthComponent,
    children: [
      { path: 'login', component: ConnexionComponent }, // Route pour la page de Connexion
      { path: 'register', component: InscriptionComponent }, // Route pour la page d'Inscription
    ],
  },

  {
    path: '',
    component: LayoutConnectedComponent,
    /*canActivate: [AuthGuard]*/
    children: [
      {
        path: 'me',
        component: MeComponent,
        pathMatch: 'full',
      }, // Route pour le composant Me
      {
        path: 'articles',
        component: ListeArticlesComponent,
        pathMatch: 'full',
      }, // Routes pour afficher tous les articles
      {
        path: 'creation-article',
        component: FormulaireComponent,
        pathMatch: 'full',
      }, // Route pour créer un article

      {
        path: 'detail-article/:id',
        component: DetailArticleComponent,
        pathMatch: 'full',
      }, // Routes pour afficher l'articles sélectionner
    ],
  },

  { path: '**', component: NotFoundComponent }, // Route pour la page d'erreur (TOUJOURS EN DERNIER)
];
