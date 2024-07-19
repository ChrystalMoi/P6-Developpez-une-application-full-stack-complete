import { Component, OnInit } from '@angular/core';
import { MenuComponent } from '../../../component/menu/menu.component';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-connexion',
  templateUrl: './connexion.component.html',
  styleUrls: ['./connexion.component.scss'],
  standalone: true,
  imports: [MenuComponent, RouterLink],
})
export class ConnexionComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
}
