import { Component, inject, OnInit } from '@angular/core';
import { MenuComponent } from '../../../../component/menu/menu.component';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ArticlesService } from '../../services/articles.service';
import { Article } from '..//../interfaces/article.interface';
import { ThemeService } from '../../services/themes.service';
import { AsyncPipe } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  selector: 'app-formulaire',
  standalone: true,
  imports: [
    MenuComponent,
    FormsModule,
    AsyncPipe,
    MatFormFieldModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    RouterLink,
  ],
  templateUrl: './formulaire.component.html',
  styleUrl: './formulaire.component.scss',
})
export class FormulaireComponent implements OnInit {
  article: Article = {
    id: 0,
    titre: '',
    contenu: '',
    utilisateur_id: 0,
    nomUtilisateur: '',
    theme_id: 0,
    theme: '',
    dateCreation: new Date(),
  };

  themeId: number = 0;
  themes$ = inject(ThemeService).tousLesThemes();

  constructor(
    private articlesService: ArticlesService,
    private router: Router,
    private route: ActivatedRoute,
    public themeService: ThemeService
  ) {}

  ngOnInit(): void {
    // Récupère l'id du thème depuis les paramètres de la route
    /*this.route.paramMap.subscribe((params) => {
      const id = params.get('id');
      if (id) {
        this.themeId = +id; // Convertit le paramètre en nombre si id n'est pas null
      } else {
        console.error('Paramètre "id" manquant dans l\'URL.');
        this.router.navigate(['/articles']); // Redirection
      }
    });*/
  }

  onSubmit(): void {
    // Convertit themeId en string pour passer à creationArticle si nécessaire
    const idThemeEnString = this.themeId.toString();

    this.articlesService
      .creationArticle(idThemeEnString, this.article) // Passe l'id du thème en tant que chaîne
      .subscribe(() => {
        this.router.navigate(['/articles']); // Redirection après création
      });
  }
}
