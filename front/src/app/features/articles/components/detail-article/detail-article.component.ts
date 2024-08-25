import { Component, Input, OnInit, signal } from '@angular/core';
import { Article } from '../../interfaces/article.interface';
import { DatePipe, JsonPipe } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { routes } from '../../../../app.routes';
import { ArticlesService } from '../../services/articles.service';
import { CommentaireArticleComponent } from '../commentaire-article/commentaire-article.component';
import { Commentaire } from '../../interfaces/commentaire.interface';
import { CommentaireService } from '../../services/commentaires.service';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { CommentaireCreateRequest } from '../../interfaces/commentaireCreateRequest.interface';

@Component({
  selector: 'app-detail-article',
  standalone: true,
  imports: [
    DatePipe,
    JsonPipe,
    CommentaireArticleComponent,
    MatFormFieldModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
  ],
  templateUrl: './detail-article.component.html',
  styleUrl: './detail-article.component.scss',
})
export class DetailArticleComponent implements OnInit {
  articleId: string = '';
  detailArticle!: Article;
  private _commentaires = signal<Commentaire[]>([]);
  public commentaireForm: FormGroup;
  readonly contenuMaxLength = 500;

  constructor(
    private route: ActivatedRoute,
    private articlesService: ArticlesService,
    private commentaireService: CommentaireService,
    private formBuilder: FormBuilder,
    private router: Router
  ) {
    this.commentaireForm = this.formBuilder.group({
      contenu: [
        '',
        [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(this.contenuMaxLength),
        ],
      ],
    });
  }

  get commentaires() {
    return this._commentaires;
  }

  ngOnInit(): void {
    // Récupération de l'id de l'url
    this.route.paramMap.subscribe((params) => {
      this.articleId = params.get('id') || '';

      this.articlesService.detailArticle(this.articleId).subscribe({
        next: (data) => {
          this.detailArticle = data; //stockage de data dans detailArticle
          console.log('Detail Article du backend: ', data);
        },
        error: () => this.router.navigate(['/error']),
      });

      this.actualiserListeCommentaires();
    });
  }

  public onSubmit(): void {
    if (this.commentaireForm.invalid) {
      console.error('Erreur - Formulaire vide');
      return; // Arrête l'exécution si le formulaire = invalide
    }

    // Récupère les valeurs du formulaire `commentaireForm`
    const formValues = this.commentaireForm.value;

    // Création d'un objet `articleRequest`avec les données nécessaires
    const commentaireCreateRequest: CommentaireCreateRequest = {
      contenu: formValues.contenu,
    };

    // Appel du service article pour enregistrer l'article
    this.commentaireService
      .envoi(this.articleId, commentaireCreateRequest)
      .subscribe({
        // Lancement du rechargement du composant pour afficher le nouveau commentaire
        next: () => {
          console.log('Envoyé !');
          this.actualiserListeCommentaires();
          this.commentaireForm.controls['contenu'].setValue('');
        },
        error: (err) =>
          console.error("Erreur lors de l'ajout d'un article : ", err),
      });
  }

  public actualiserListeCommentaires() {
    this.commentaireService.tousLesCommentaires(this.articleId).subscribe({
      next: (commentaire) => {
        this.commentaires.set(commentaire);
      },
      error: (err) => {
        console.error('Erreur de récupération des commentaires : ', err);
      },
    });
  }
}
