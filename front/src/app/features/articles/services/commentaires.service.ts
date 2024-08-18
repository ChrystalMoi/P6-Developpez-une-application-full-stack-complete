import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ArticleResponse } from '../interfaces/articleResponse.interface';
import { Commentaire } from '../interfaces/commentaire.interface';
import { CommentaireCreateRequest } from '../interfaces/commentaireCreateRequest.interface';

@Injectable({
  providedIn: 'root',
})
export class CommentaireService {
  // Définition du chemin de base pour la requête à l'API
  private pathService = 'api/article';

  // Le constructeur injecte HttpClient comme dépendance
  constructor(private httpClient: HttpClient) {}

  // Méthode pour envoyer un commentaire a un article
  public envoi(
    articleId: string,
    messageRequest: CommentaireCreateRequest
  ): Observable<ArticleResponse> {
    // Envoie une requête POST à l'API avec l'identifiant de l'article et le commentaire
    return this.httpClient.post<ArticleResponse>(
      `${this.pathService}/${articleId}/commentaire`,
      messageRequest
    );
  }

  public tousLesCommentaires(articleId: string): Observable<Commentaire[]> {
    const comm = this.httpClient.get<Commentaire[]>(
      `${this.pathService}/${articleId}/commentaire`
    );
    return comm;
  }
}
