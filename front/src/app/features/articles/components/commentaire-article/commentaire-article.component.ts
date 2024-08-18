import { Component, Input } from '@angular/core';
import { Commentaire } from '../../interfaces/commentaire.interface';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-commentaire-article',
  standalone: true,
  imports: [DatePipe],
  templateUrl: './commentaire-article.component.html',
  styleUrl: './commentaire-article.component.scss',
})
export class CommentaireArticleComponent {
  @Input() commentaire?: Commentaire;
}
