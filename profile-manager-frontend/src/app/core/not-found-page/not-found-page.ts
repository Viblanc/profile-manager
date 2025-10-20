import { Component, inject, OnInit } from '@angular/core';
import { PageTitle } from '../page-title/page-title';
import { Router } from '@angular/router';

@Component({
  selector: 'app-not-found-page',
  imports: [PageTitle],
  templateUrl: './not-found-page.html',
  styleUrl: './not-found-page.css',
})
export class NotFoundPage implements OnInit {
  private router = inject(Router);

  ngOnInit(): void {
    setTimeout(() => {
      this.router.navigateByUrl('users');
    }, 5000);
  }
}
