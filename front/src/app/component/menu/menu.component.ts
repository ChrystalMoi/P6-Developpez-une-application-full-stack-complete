import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { NgIf, NgTemplateOutlet } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatDividerModule } from '@angular/material/divider';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatListModule } from '@angular/material/list';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss'],
  standalone: true,
  imports: [
    RouterLink,
    NgIf,
    MatIconModule,
    MatSidenavModule,
    MatDividerModule,
    NgTemplateOutlet,
    MatToolbarModule,
    MatListModule,
    RouterModule,
  ],
})
export class MenuComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
}
