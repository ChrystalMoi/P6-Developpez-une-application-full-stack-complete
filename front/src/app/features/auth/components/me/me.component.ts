import { Component, NgZone, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SessionService } from '../../../../services/session.service';
import { Router } from '@angular/router';
import { User } from '../../../../interfaces/user.interface';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { RegisterRequest } from '../../interfaces/registerRequest.interface';
import { AuthService } from '../../services/auth.service';
import { AuthSuccess } from '../../interfaces/authSuccess.interface';
import { Theme } from '../../../articles/interfaces/theme.interface';
import { CarteThemeComponent } from '../../../themes/components/carte-theme/carte-theme.component';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss'],
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    CarteThemeComponent,
  ],
})
export class MeComponent implements OnInit {
  moreInfoVisible: boolean = false;
  public onError = false;
  public user: User | undefined;
  public meForm: FormGroup;
  private _themes = signal<Theme[]>([]);

  constructor(
    private sessionService: SessionService,
    private router: Router,
    private ngZone: NgZone,
    private formBuilder: FormBuilder,
    private authService: AuthService
  ) {
    this.meForm = this.formBuilder.group({
      nom: ['', [Validators.minLength(3), Validators.maxLength(20)]],
      email: ['', [Validators.email, Validators.maxLength(50)]],
    });
  }

  get themes() {
    return this._themes;
  }

  ngOnInit(): void {
    // Actualise les données utilisateur
    this.authService.me().subscribe({
      next: (responseUser: User) => {
        // Actualiser le user (responseUser) dans sessionService
        this.sessionService.user = responseUser;

        // Actualise le user dans la component
        this.user = this.sessionService.user;

        // MAJ Form avec les données de l'utilisateur
        const recuperationControleNom = this.meForm.get('nom');
        recuperationControleNom?.setValue(this.user?.nom);

        const recuperationControleEmail = this.meForm.get('email');
        recuperationControleEmail?.setValue(this.user?.email);

        //
      },
      error(err) {
        console.error('Une erreur est survenue : ', err);
      },
    });
  }

  public submit(): void {
    const registerRequest = this.meForm.value as RegisterRequest;
    this.authService.update(registerRequest).subscribe({
      next: (response: AuthSuccess) => {
        localStorage.setItem('token', response.token);
        this.authService.me().subscribe((user: User) => {
          this.sessionService.logIn(user);
          this.router.navigate(['/login']).then(() => {
            localStorage.removeItem('token');
          });
        });
      },
      error: (err) => {
        console.error(
          "Erreur lors de la modification de l'utilisateur : ",
          err
        );
      },
    });
  }

  deconnexion() {
    this.sessionService.logOut();
    console.info('Déconnecté');

    this.ngZone.run(() => {
      // Redirige l'utilisateur vers la page '/login' après deconnexion et supprime le token du localstorage
      this.router.navigate(['/login']).then(() => {
        localStorage.removeItem('token');
      });
    });
  }
}
