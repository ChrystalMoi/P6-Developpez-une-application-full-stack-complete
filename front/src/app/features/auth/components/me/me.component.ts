import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MenuComponent } from '../../../../component/menu/menu.component';
import { SessionService } from '../../../../services/session.service';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss'],
  standalone: true,
  imports: [CommonModule, MenuComponent],
})
export class MeComponent implements OnInit {
  moreInfoVisible: boolean = false;
  public onError = false;

  constructor(private sessionService: SessionService) {}

  ngOnInit(): void {}

  showMore() {
    this.moreInfoVisible = !this.moreInfoVisible;
  }

  deconnexion() {
    this.sessionService.logOut();
    console.info('Déconnecté');
  }
}
