import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Article } from '../interfaces/article.interface';
import { ArticleResponse } from '../interfaces/articleResponse.interface';
import { MessageDto } from '../interfaces/message-dto';

@Injectable({
  providedIn: 'root',
})
export class ArticlesService {
  private pathService = 'api';

  constructor(private httpClient: HttpClient) {}

  public tousLesArticles(utilisateur_id: number): Observable<Article[]> {
    return this.httpClient.get<Article[]>(
      `${this.pathService}/utilisateur/${utilisateur_id}/articles`
    );
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
    article: Article
  ): Observable<ArticleResponse> {
    return this.httpClient.post<ArticleResponse>(
      `${this.pathService}/theme/${id_theme}/articles`,
      article
    );
  }
}
