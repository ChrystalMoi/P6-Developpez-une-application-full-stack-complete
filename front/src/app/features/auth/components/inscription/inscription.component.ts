import { Component, NgZone } from '@angular/core';
import { MenuComponent } from '../../../../component/menu/menu.component';
import { AuthService } from '../../services/auth.service';
import { RouterLink } from '@angular/router';
import { Router } from '@angular/router';
import { NgIf } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
} from '@angular/forms';
import { AuthSuccess } from '../../interfaces/authSuccess.interface';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';
import { User } from '../../../../interfaces/user.interface';
import { SessionService } from '../../../../services/session.service';

@Component({
  selector: 'app-inscription',
  templateUrl: './inscription.component.html',
  styleUrls: ['./inscription.component.scss'],
  standalone: true,
  imports: [MenuComponent, RouterLink, NgIf, ReactiveFormsModule],
})
export class InscriptionComponent {
  public hide = true;
  public onError = false;
  public inscriptionForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private ngZone: NgZone,
    private sessionService: SessionService
  ) {
    this.inscriptionForm = this.formBuilder.group({
      email: [
        '',
        [Validators.required, Validators.email, Validators.maxLength(63)],
      ],
      nom: [
        '',
        [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(30),
        ],
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
    if (this.inscriptionForm.invalid) {
      return; // Arrête l'exécution si le formulaire = invalide
    }

    // Récupère les valeurs du formulaire `inscriptionForm`
    const formValues = this.inscriptionForm.value;

    // Crée un objet `registerRequest` avec les données nécessaires pour l'inscription
    const registerRequest: RegisterRequest = {
      nom: formValues.nom,
      email: formValues.email,
      motDePasse: formValues.motDePasse,
    };

    // Appelle le service d'authentification pour inscrire l'utilisateur
    this.authService.register(registerRequest).subscribe({
      // Est exécuté lorsque l'inscription ok
      next: (response: AuthSuccess) => {
        // Stocke le token d'auth renvoyer par le serveur dans le localStorage
        console.log('AuthSuccess response :', response);
        localStorage.setItem('token', response.token);
        console.log(
          'Token stored in localStorage:',
          localStorage.getItem('token')
        );

        // Récupération donnée user de /me
        this.authService.me().subscribe({
          next: (responseUser: User) => {
            this.sessionService.logIn(responseUser);

            // Assure que la redirection fonctionne après l'inscription
            this.ngZone.run(() => {
              // Redirige l'utilisateur vers la page '/me' après inscription
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

      // C'est exécutée si une erreur se produit lors de l'inscription
      error: (error) => {
        console.error("Erreur lors de l'inscription :", error);
        this.onError = true;
      },
    });
  }
}
