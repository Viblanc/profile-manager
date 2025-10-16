import { Component, DestroyRef, inject, signal } from '@angular/core';
import { UserTypeApi } from '../user-type-api';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-user-type-list',
  imports: [RouterLink],
  templateUrl: './user-type-list.html',
  styleUrl: './user-type-list.css',
})
export class UserTypeList {
  private userTypeApi = inject(UserTypeApi);
  private destroyRef = inject(DestroyRef);
  userTypes = signal<UserType[]>([]);
  errorMessage = signal<string>('');

  ngOnInit(): void {
    const subscription = this.userTypeApi.getUserTypes().subscribe({
      next: (userTypes) => {
        this.userTypes.set(userTypes);
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
