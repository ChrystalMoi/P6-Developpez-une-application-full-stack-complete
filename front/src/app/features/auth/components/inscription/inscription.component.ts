import { Component, OnInit } from '@angular/core';
import { MenuComponent } from '../../../component/menu/menu.component';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-inscription',
  templateUrl: './inscription.component.html',
  styleUrls: ['./inscription.component.scss'],
  standalone: true,
  imports: [MenuComponent, RouterLink],
})
export class InscriptionComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
}
