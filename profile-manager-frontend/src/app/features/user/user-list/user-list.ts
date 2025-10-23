import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { UserApi } from '../user-api';
import { Router, RouterLink } from '@angular/router';
import { UserRow } from './user-row/user-row';
import { User } from '../user';
import { TableHeadings } from '../../../shared/table-container/table-headings';
import { PageTitle } from '../../../core/page-title/page-title';
import { TableContainer } from '../../../shared/table-container/table-container';
import { BigButton } from '../../../shared/big-button/big-button';

@Component({
  selector: 'app-user-list',
  imports: [RouterLink, UserRow, PageTitle, TableContainer, BigButton],
  templateUrl: './user-list.html',
  styleUrl: './user-list.css',
})
export class UserList implements OnInit {
  private userApi = inject(UserApi);
  private router = inject(Router);
  private destroyRef = inject(DestroyRef);
  users = signal<User[]>([]);
  headings: TableHeadings<User> = {
    sortable: {
      lastName: {
        title: 'Last Name',
        cmp: (a, b) => compare(a.lastName, b.lastName),
      },
      firstName: {
        title: 'First Name',
        cmp: (a, b) => compare(a.firstName, b.firstName),
      },
      email: {
        title: 'Email',
        cmp: (a, b) => compare(a.email, b.email),
      },
      userType: {
        title: 'User Type',
        cmp: (a, b) => compare(a.userType.name, b.userType.name),
      },
    },
    other: [{ title: 'Actions', colspan: 3 }],
    keys: ['userType', 'lastName', 'firstName', 'email'],
  };

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

  goToProfilePage(user: User) {
    this.router.navigateByUrl(`users/${user.id}`);
  }

  goToEditPage(user: User) {
    this.router.navigateByUrl(`users/edit/${user.id}`);
  }

  removeUser(id: number): void {
    const subscription = this.userApi.deleteUser(id).subscribe(() => {
      this.users.update((old) => old.filter((u) => u.id !== id));
    });

    this.destroyRef.onDestroy(() => subscription.unsubscribe());
  }
}

const compare = (a: string, b: string) => (a < b ? -1 : a > b ? 1 : 0);
