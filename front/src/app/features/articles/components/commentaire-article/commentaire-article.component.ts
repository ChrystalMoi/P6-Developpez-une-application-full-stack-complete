import { Component, Input } from '@angular/core';
import { Commentaire } from '../../interfaces/commentaire.interface';

@Component({
  selector: 'app-commentaire-article',
  standalone: true,
  imports: [],
  templateUrl: './commentaire-article.component.html',
  styleUrl: './commentaire-article.component.scss',
})
export class CommentaireArticleComponent {
  @Input() commentaire?: Commentaire;
}
