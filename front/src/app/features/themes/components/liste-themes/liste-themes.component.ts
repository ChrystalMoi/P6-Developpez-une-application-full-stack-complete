import { Component, NgZone, OnInit, signal } from '@angular/core';
import { CarteThemeComponent } from '../carte-theme/carte-theme.component';
import { AsyncPipe, JsonPipe } from '@angular/common';
import { Theme } from '../../../articles/interfaces/theme.interface';
import { Router } from '@angular/router';
import { ThemeService } from '../../../articles/services/themes.service';

@Component({
  selector: 'app-liste-themes',
  standalone: true,
  imports: [CarteThemeComponent, AsyncPipe, JsonPipe],
  templateUrl: './liste-themes.component.html',
  styleUrl: './liste-themes.component.scss',
})
export class ListeThemesComponent implements OnInit {
  private _themes = signal<Theme[]>([]);

  constructor(
    private router: Router,
    private ngZone: NgZone,
    private themeService: ThemeService
  ) {}

  get themes() {
    return this._themes;
  }

  ngOnInit(): void {
    this.themeService.tousLesThemes().subscribe({
      next: (theme) => {
        this.themes.set(theme);
      },
      error: (error) => {
        console.error('Erreur de récupération des themes : ', error);
      },
    });
  }
}
