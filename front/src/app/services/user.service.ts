import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../interfaces/user.interface';
import { ArticleResponse } from '../features/articles/interfaces/articleResponse.interface';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private pathService = 'api/utilisateur';

  constructor(private httpClient: HttpClient) {}

  public getUserById(id: number): Observable<User> {
    return this.httpClient.get<User>(`${this.pathService}/${id}`);
  }

  public sauvegardeModifUser(user: User): Observable<ArticleResponse> {
    return this.httpClient.post<ArticleResponse>(
      `${this.pathService}/me`,
      user
    );
  }
}
