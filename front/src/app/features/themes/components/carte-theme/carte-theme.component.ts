import { JsonPipe, DatePipe } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { Theme } from '../../../articles/interfaces/theme.interface';
import { UserService } from '../../../../services/user.service';
import { ThemeService } from '../../../articles/services/themes.service';

@Component({
  selector: 'app-carte-theme',
  standalone: true,
  imports: [JsonPipe, DatePipe, RouterModule],
  templateUrl: './carte-theme.component.html',
  styleUrl: './carte-theme.component.scss',
})
export class CarteThemeComponent implements OnInit {
  constructor(private themeService: ThemeService) {}

  @Input() theme?: Theme;

  estAbonne: boolean = false;

  ngOnInit(): void {
    this.verificationAbonnement();
  }

  public inscriptionTheme(): void {
    if (this.theme != null || this.theme != undefined) {
      this.themeService.inscriptionTheme(this.theme?.id).subscribe({
        next: () => (this.estAbonne = true),
        error: (err) => console.error('Erreur: ', err),
      });
    }
  }

  public desinscriptionTheme(): void {
    if (this.theme != null || this.theme != undefined) {
      this.themeService.desabonnementTheme(this.theme.id).subscribe({
        next: () => (this.estAbonne = false),
        error: (err) => console.error('Erreur: ', err),
      });
    }
  }

  public gestionAbonnementTheme(): void {
    if (this.estAbonne == true) {
      this.desinscriptionTheme();
    } else {
      this.inscriptionTheme();
    }
  }

  public verificationAbonnement(): void {
    if (this.theme) {
      this.themeService.verificationAbonnement(this.theme?.id).subscribe({
        next: (result) => {
          this.estAbonne = result.inscrit;
        },
        error: (err) => console.error('Erreur : ', err),
      });
    }
  }
}
