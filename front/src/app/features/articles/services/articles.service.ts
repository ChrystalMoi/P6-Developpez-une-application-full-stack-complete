import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Article } from '../interfaces/article.interface';
import { ArticleResponse } from '../interfaces/articleResponse.interface';
import { MessageDto } from '../interfaces/message-dto';
import { ArticleCreateRequest } from '../interfaces/articleCreateRequest.interface';

@Injectable({
  providedIn: 'root',
})
export class ArticlesService {
  private pathService = 'api';

  constructor(private httpClient: HttpClient) {}

  public tousLesArticlesDeLutilisateur(
    auteurId: number
  ): Observable<Article[]> {
    return this.httpClient.get<Article[]>(
      `${this.pathService}/utilisateur/${auteurId}/articles`
    );
  }

  public tousLesArticles(): Observable<Article[]> {
    const test = this.httpClient.get<Article[]>(`${this.pathService}/article`);
    return test;
  }

  public detailArticle(id: string): Observable<Article> {
    return this.httpClient.get<Article>(`${this.pathService}/article/${id}`);
  }

  public creationCommentaire(id: string): Observable<Array<MessageDto>> {
    return this.httpClient.get<Array<MessageDto>>(
      `${this.pathService}/article/${id}/commentaire`
    );
  }

  public creationArticle(
    id_theme: string,
    article: ArticleCreateRequest
  ): Observable<ArticleResponse> {
    return this.httpClient.post<ArticleResponse>(
      `${this.pathService}/theme/${id_theme}/articles`,
      article
    );
  }
}
