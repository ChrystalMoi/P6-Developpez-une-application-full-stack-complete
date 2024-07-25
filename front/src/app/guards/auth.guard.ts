import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { SessionService } from '../services/session.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard {
  // Le constructeur injecte les dépendances Router et SessionService
  constructor(private router: Router, private sessionService: SessionService) {}

  // Méthode canActivate pour déterminer si une route peut être activée
  public canActivate(): boolean {
    // Vérifie si l'utilisateur est connecté via le service de session
    if (!this.sessionService.isLogged) {
      // Si l'utilisateur n'est pas connecté, redirige vers la page de /login
      this.router.navigate(['login']);

      // Retourne false pour empêcher l'activation de la route
      return false;
    }
    // Si l'utilisateur est connecté, retourne true pour permettre l'accès à la route
    return true;
  }
}
