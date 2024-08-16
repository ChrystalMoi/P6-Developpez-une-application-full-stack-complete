import { Component, Input, OnInit } from '@angular/core';
import { MenuComponent } from '../../../../component/menu/menu.component';
import { Article } from '../../interfaces/article.interface';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { routes } from '../../../../app.routes';
import { ArticlesService } from '../../services/articles.service';
import { CommentaireArticleComponent } from '../commentaire-article/commentaire-article.component';

@Component({
  selector: 'app-detail-article',
  standalone: true,
  imports: [MenuComponent, DatePipe, CommentaireArticleComponent, RouterModule],
  templateUrl: './detail-article.component.html',
  styleUrl: './detail-article.component.scss',
})
export class DetailArticleComponent implements OnInit {
  articleId: string = '';
  detailArticle!: Article;

  constructor(
    private route: ActivatedRoute,
    private articlesService: ArticlesService
  ) {}

  ngOnInit(): void {
    // Récupération de l'id de l'url
    this.route.paramMap.subscribe((params) => {
      this.articleId = params.get('id') || '';

      this.articlesService.detailArticle(this.articleId).subscribe({
        next: (data) => {
          this.detailArticle = data; //stockage de data dans detailArticle
          console.log('Detail Article du backend: ', data);
        },
        error: (err) =>
          console.error("Erreur lors de la récupération de l'article : ", err),
      });
    });
  }
}
