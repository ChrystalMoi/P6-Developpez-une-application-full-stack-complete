import { Component, Input } from '@angular/core';
import { Article } from '../../interfaces/article.interface';
import { DatePipe, JsonPipe } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-carte-article',
  standalone: true,
  imports: [JsonPipe, DatePipe, RouterModule],
  templateUrl: './carte-article.component.html',
  styleUrl: './carte-article.component.scss',
})
export class CarteArticleComponent {
  constructor() {}

  @Input() article?: Article;
}
