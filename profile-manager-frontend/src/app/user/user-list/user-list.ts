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
      const compareFn: (a: any, b: any) => 1 | -1 =
        this.sortedColumn() === 'userType' ? compareUserTypes : compare;
      if (this.sortAscending()) {
        return compareFn(a[this.sortedColumn()], b[this.sortedColumn()]);
      } else {
        return compareFn(b[this.sortedColumn()], a[this.sortedColumn()]);
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

const compare = (a: string, b: string) => (a < b ? -1 : 1);
const compareUserTypes = (a: UserType, b: UserType) => compare(a.name, b.name);

const sortedColumns = ['userType', 'lastName', 'firstName', 'email'] as const;
type Column = (typeof sortedColumns)[number];

interface TableHeader {
  id: Column;
  title: string;
}
