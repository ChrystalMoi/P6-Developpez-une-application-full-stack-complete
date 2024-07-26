import { Component, NgZone } from '@angular/core';
import { Router } from '@angular/router';
import { MenuComponent } from '../../../../component/menu/menu.component';

@Component({
  selector: 'app-liste-articles',
  standalone: true,
  imports: [MenuComponent],
  templateUrl: './liste-articles.component.html',
  styleUrl: './liste-articles.component.scss',
})
export class ListeArticlesComponent {
  constructor(private router: Router, private ngZone: NgZone) {}

  creationArticle() {
    this.ngZone.run(() => {
      this.router.navigate(['/creation-article']);
    });
  }
}
