import { Component, NgZone } from '@angular/core';
import { MenuComponent } from '../../../../component/menu/menu.component';
import { Router, RouterLink } from '@angular/router';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { LoginRequest } from '../../interfaces/loginRequest.interface';
import { AuthSuccess } from '../../interfaces/authSuccess.interface';
import { NgIf } from '@angular/common';
import { AuthService } from '../../services/auth.service';
import { SessionService } from '../../../../services/session.service';
import { User } from '../../../../interfaces/user.interface';

@Component({
  selector: 'app-connexion',
  templateUrl: './connexion.component.html',
  styleUrls: ['./connexion.component.scss'],
  standalone: true,
  imports: [MenuComponent, RouterLink, NgIf, ReactiveFormsModule],
})
export class ConnexionComponent {
  public hide = true;
  public onError = false;
  public connexionForm: FormGroup;

  constructor(
    private router: Router,
    private ngZone: NgZone,
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private sessionService: SessionService
  ) {
    this.connexionForm = this.formBuilder.group({
      email: [
        '',
        [Validators.required, Validators.email, Validators.maxLength(63)],
      ],
      motDePasse: [
        '',
        [
          Validators.required,
          Validators.pattern('^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,60}$'),
        ],
      ],
    });
  }

  public submit(): void {
    if (this.connexionForm.invalid) {
      return; // Arrête l'exécution si le formulaire = invalide
    }

    // Récupère les valeurs du formulaire `connexionForm`
    const formValues = this.connexionForm.value;

    // Crée un objet `loginRequest` avec les données nécessaires pour la connexion
    const loginRequest: LoginRequest = {
      email: formValues.email,
      motDePasse: formValues.motDePasse,
    };

    // Appelle le service d'authentification pour connecter l'utilisateur
    this.authService.login(loginRequest).subscribe({
      // Est exécuté lorsque la connexion est ok
      next: (response: AuthSuccess) => {
        // Stocke le token d'auth renvoyer par le serveur dans le localStorage
        console.log('AuthSuccess response:', response);
        localStorage.setItem('token', response.token);
        console.log(
          'Token stored in localStorage:',
          localStorage.getItem('token')
        );

        // Récupération donnée user de /me
        this.authService.me().subscribe({
          next: (responseUser: User) => {
            this.sessionService.logIn(responseUser);

            // Assure que la redirection fonctionne après la connexion
            this.ngZone.run(() => {
              // Redirige l'utilisateur vers la page '/me' après connexion
              this.router.navigate(['/me']);
            });
          },
          error(err) {
            console.error(
              'Une erreur est survenue lors de la récupréation des données utilisateur : ',
              err
            );
          },
        });
      },

      // C'est exécutée si une erreur se produit lors de la connexion
      error: () => {
        this.onError = true;
      },
    });
  }
}
