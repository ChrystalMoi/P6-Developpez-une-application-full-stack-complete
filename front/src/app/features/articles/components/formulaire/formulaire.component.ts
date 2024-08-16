import { Component, inject } from '@angular/core';
import { MenuComponent } from '../../../../component/menu/menu.component';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ArticlesService } from '../../services/articles.service';
import { ThemeService } from '../../services/themes.service';
import { AsyncPipe } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { ArticleCreateRequest } from '../../interfaces/articleCreateRequest.interface';
import { TestBed } from '@angular/core/testing';

@Component({
  selector: 'app-formulaire',
  standalone: true,
  imports: [
    MenuComponent,
    FormsModule,
    AsyncPipe,
    MatFormFieldModule,
    MatSelectModule,
    MatProgressSpinnerModule,
    ReactiveFormsModule,
    RouterLink,
  ],
  templateUrl: './formulaire.component.html',
  styleUrl: './formulaire.component.scss',
})
export class FormulaireComponent {
  public articleForm: FormGroup;
  readonly descriptionMaxLength = 500;

  themes$ = inject(ThemeService).tousLesThemes();

  constructor(
    private articlesService: ArticlesService,
    private router: Router,
    private route: ActivatedRoute,
    public themeService: ThemeService,
    private formBuilder: FormBuilder
  ) {
    this.articleForm = this.formBuilder.group({
      theme: ['0', [Validators.required]],
      titre: [
        '',
        [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(50),
        ],
      ],
      contenu: [
        '',
        [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(this.descriptionMaxLength),
        ],
      ],
    });
  }

  public onSubmit(): void {
    console.log('formulaire : ', this.articleForm.value);

    if (this.articleForm.invalid) {
      console.error('Erreur - Formulaire invalide');
      return; // Arrête l'exécution si le formulaire = invalide
    }

    if (this.articleForm.get('theme')?.value == 0) {
      console.error('Le thème ne peut pas être vide');
      return;
    }

    // Récupère les valeurs du formulaire `articleForm`
    const formValues = this.articleForm.value;

    // Création d'un objet `articleRequest`avec les données nécessaires
    const articleCreateRequest: ArticleCreateRequest = {
      titre: formValues.titre,
      contenu: formValues.contenu,
    };

    // Appel du service article pour enregistrer l'article
    this.articlesService
      .creationArticle(formValues.theme, articleCreateRequest)
      .subscribe({
        next: () => this.router.navigate(['/articles']),
        error: (err) =>
          console.error("Erreur lors de l'ajout d'un article : ", err),
      });
  }
}
