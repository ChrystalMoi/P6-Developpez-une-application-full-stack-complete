import { Component, OnInit } from '@angular/core';
import { MenuComponent } from '../../../../component/menu/menu.component';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  standalone: true,
  imports: [MenuComponent, RouterLink],
})
export class HomeComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
}
