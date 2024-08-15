import { Component, inject, NgZone } from '@angular/core';
import { Router } from '@angular/router';
import { MenuComponent } from '../../../../component/menu/menu.component';
import { CarteArticleComponent } from '../carte-article/carte-article.component';
import { AsyncPipe, JsonPipe } from '@angular/common';
import { ArticlesService } from '../../services/articles.service';

@Component({
  selector: 'app-liste-articles',
  standalone: true,
  imports: [MenuComponent, CarteArticleComponent, AsyncPipe, JsonPipe],
  templateUrl: './liste-articles.component.html',
  styleUrl: './liste-articles.component.scss',
})
export class ListeArticlesComponent {
  articles$ = inject(ArticlesService).tousLesArticles();

  constructor(private router: Router, private ngZone: NgZone) {}

  creationArticle() {
    this.ngZone.run(() => {
      this.router.navigate(['/creation-article']);
    });
  }
}
