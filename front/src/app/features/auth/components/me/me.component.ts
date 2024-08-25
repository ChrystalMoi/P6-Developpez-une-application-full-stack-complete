import { Component, NgZone, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SessionService } from '../../../../services/session.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss'],
  standalone: true,
  imports: [CommonModule],
})
export class MeComponent implements OnInit {
  moreInfoVisible: boolean = false;
  public onError = false;

  constructor(
    private sessionService: SessionService,
    private router: Router,
    private ngZone: NgZone
  ) {}

  ngOnInit(): void {}

  showMore() {
    this.moreInfoVisible = !this.moreInfoVisible;
  }

  deconnexion() {
    this.sessionService.logOut();
    console.info('Déconnecté');

    this.ngZone.run(() => {
      // Redirige l'utilisateur vers la page '/login' après connexion
      this.router.navigate(['/login']);
    });
  }
}
