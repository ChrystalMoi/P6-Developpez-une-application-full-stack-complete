import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.scss'],
  standalone: true,
  imports: [CommonModule],
})
export class MeComponent implements OnInit {
  moreInfoVisible: boolean = false;

  constructor() {}

  ngOnInit(): void {}

  showMore() {
    this.moreInfoVisible = !this.moreInfoVisible;
  }
}
