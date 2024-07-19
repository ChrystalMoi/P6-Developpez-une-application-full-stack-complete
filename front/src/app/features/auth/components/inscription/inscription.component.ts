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
import { SessionService } from '../../../../services/session.service';
import { User } from '../../../../interfaces/user.interface';

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
    private sessionService: SessionService,
    private router: Router,
    private ngZone: NgZone
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

    const formValues = this.inscriptionForm.value;

    const registerRequest: RegisterRequest = {
      nom: formValues.nom,
      email: formValues.email,
      motDePasse: formValues.motDePasse,
    };

    this.authService.register(registerRequest).subscribe({
      next: (response: AuthSuccess) => {
        localStorage.setItem('token', response.token);
        this.authService.me().subscribe((user: User) => {
          this.sessionService.logIn(user);
          this.ngZone.run(() => {
            this.router.navigate(['/']);
          });
        });
      },
      error: () => {
        this.onError = true;
      },
    });
  }
}
