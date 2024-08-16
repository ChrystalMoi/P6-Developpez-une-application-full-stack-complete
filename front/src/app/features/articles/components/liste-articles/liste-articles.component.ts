import { Component, inject, NgZone, OnInit, signal } from '@angular/core';
import { Router } from '@angular/router';
import { MenuComponent } from '../../../../component/menu/menu.component';
import { CarteArticleComponent } from '../carte-article/carte-article.component';
import { AsyncPipe, JsonPipe } from '@angular/common';
import { ArticlesService } from '../../services/articles.service';
import { Article } from '../../interfaces/article.interface';

@Component({
  selector: 'app-liste-articles',
  standalone: true,
  imports: [MenuComponent, CarteArticleComponent, AsyncPipe, JsonPipe],
  templateUrl: './liste-articles.component.html',
  styleUrl: './liste-articles.component.scss',
})
export class ListeArticlesComponent implements OnInit {
  private _articles = signal<Article[]>([]);

  constructor(
    private router: Router,
    private ngZone: NgZone,
    private articleService: ArticlesService
  ) {}

  get articles() {
    return this._articles;
  }

  /* Se lance dès l'initialisation du composant */
  ngOnInit(): void {
    this.articleService.tousLesArticles().subscribe({
      next: (article) => {
        this.articles.set(article);
      },
      error: (error) => {
        console.error('Erreur de récupération des articles : ', error);
      },
    });
  }

  creationArticle() {
    this.ngZone.run(() => {
      this.router.navigate(['/creation-article']);
    });
  }

  sortArticles(articles: Article[], filter: string): Article[] {
    switch (filter) {
      case 'titre-asc':
        return articles.sort((a, b) => a.titre.localeCompare(b.titre));
      case 'titre-desc':
        return articles.sort((a, b) => b.titre.localeCompare(a.titre));
      default:
        return articles;
    }
  }

  onFilterChange(event: Event) {
    const target = event.target as HTMLSelectElement;
    const filter = target.value;

    if (filter) {
      const sortedArticles = this.sortArticles(this.articles(), filter);
      this.articles.set(sortedArticles);
    }
  }
}

/*
1. Récupérer la liste des articles du service (compliqué ++++)
2. Stocker la liste dans une variable dans le liste-articles.component.ts (++++)
3. Boucler dans la liste for pour afficher les articles (++)
4. Garder le select (+-)
5. Retirer les parenthèses de this.articles() (ligne 49) (pas compliqué -)
 */
