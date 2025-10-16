import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { UserApi } from '../user-api';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-user-list',
  imports: [RouterLink],
  templateUrl: './user-list.html',
  styleUrl: './user-list.css',
})
export class UserList implements OnInit {
  private userApi = inject(UserApi);
  private destroyRef = inject(DestroyRef);
  users = signal<User[]>([]);
  errorMessage = signal<string>('');

  ngOnInit(): void {
    const subscription = this.userApi.getUsers().subscribe({
      next: (users) => {
        this.users.set(users);
      },
      error: (err) => {
        console.error(err);
      },
    });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }
}
