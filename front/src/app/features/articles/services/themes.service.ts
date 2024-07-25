import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, take } from 'rxjs';
import { Theme } from '../interfaces/theme.interface';

@Injectable({
  providedIn: 'root',
})
export class ThemeService {
  private pathService = 'api/theme';

  constructor(private httpClient: HttpClient) {}

  public tousLesThemes(): Observable<Theme[]> {
    return this.httpClient.get<Theme[]>(this.pathService);
  }

  public inscriptionTheme(id: number): void {
    this.httpClient
      .post(`${this.pathService}/${id}/subscription`, '', {
        responseType: 'text',
      })
      .pipe(take(1))
      .subscribe();
  }

  public desabonnementTheme(id: number): void {
    this.httpClient
      .delete(`${this.pathService}/${id}/subscription`, {
        responseType: 'text',
      })
      .pipe(take(1))
      .subscribe();
  }
}
