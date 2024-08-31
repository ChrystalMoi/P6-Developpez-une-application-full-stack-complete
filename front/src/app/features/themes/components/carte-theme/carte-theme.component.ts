import { JsonPipe, DatePipe } from '@angular/common';
import { Component, Input } from '@angular/core';
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
export class CarteThemeComponent {
  constructor(private themeService: ThemeService) {}

  @Input() theme?: Theme;

  estAbonne: boolean = false;

  toggleAbonnement() {
    this.estAbonne = !this.estAbonne;
  }

  public inscriptionTheme(): void {
    if (this.theme != null || this.theme != undefined) {
      this.themeService.inscriptionTheme(this.theme?.id).subscribe({
        next: () => this.toggleAbonnement(),
        error: (err) => console.error('Erreur:', err),
      });
    }
  }
}
