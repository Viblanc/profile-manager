import { Component, DestroyRef, inject, input } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { UserApi } from '../user-api';
import { User } from '../user';

@Component({
  selector: 'app-user-profile',
  imports: [RouterLink],
  templateUrl: './user-profile.html',
  styleUrl: './user-profile.css',
})
export class UserProfile {
  user = input.required<User>();
  private userApi = inject(UserApi);
  private router = inject(Router);
  private destroyRef = inject(DestroyRef);

  deleteUser(): void {
    const subscription = this.userApi.deleteUser(this.user().id!).subscribe(() => {
      this.router.navigateByUrl('users');
    });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }
}
