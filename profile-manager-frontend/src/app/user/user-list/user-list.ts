import { Component, computed, DestroyRef, inject, OnInit, signal } from '@angular/core';
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
  sortedUsers = computed(() =>
    this.users().sort((a, b) => {
      if (this.sortAscending()) {
        return a[this.sortedColumn()] < b[this.sortedColumn()] ? -1 : 1;
      } else {
        return a[this.sortedColumn()] < b[this.sortedColumn()] ? 1 : -1;
      }
    })
  );
  sortAscending = signal<boolean>(true);
  sortedColumn = signal<Column>('userType');
  tableHeaders: TableHeader[] = [
    {
      id: 'userType',
      title: 'User Type',
    },
    {
      id: 'lastName',
      title: 'Last Name',
    },
    {
      id: 'firstName',
      title: 'First Name',
    },
    {
      id: 'email',
      title: 'Email',
    },
  ];

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

  sortUsers(column: Column) {
    if (column === this.sortedColumn()) {
      this.sortAscending.set(!this.sortAscending());
    } else {
      this.sortAscending.set(true);
      this.sortedColumn.set(column);
    }
  }

  removeUser(id: number): void {
    const subscription = this.userApi.deleteUser(id).subscribe(() => {
      this.users.update((old) => old.filter((u) => u.id !== id));
    });

    this.destroyRef.onDestroy(() => subscription.unsubscribe());
  }
}

type Column = 'userType' | 'lastName' | 'firstName' | 'email';

interface TableHeader {
  id: Column;
  title: string;
}
