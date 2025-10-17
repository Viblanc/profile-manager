import { Component, computed, DestroyRef, inject, signal } from '@angular/core';
import { UserTypeApi } from '../user-type-api';
import { RouterLink } from '@angular/router';
import { TitleCasePipe } from '@angular/common';

const sortedColumns = ['id', 'name'] as const;
type SortedColumn = (typeof sortedColumns)[number];

@Component({
  selector: 'app-user-type-list',
  imports: [RouterLink, TitleCasePipe],
  templateUrl: './user-type-list.html',
  styleUrl: './user-type-list.css',
})
export class UserTypeList {
  private userTypeApi = inject(UserTypeApi);
  private destroyRef = inject(DestroyRef);
  sortedColumns = sortedColumns;
  userTypes = signal<UserType[]>([]);
  sortedUserTypes = computed(() => {
    const sortedCol = this.sortedColumn();
    return this.userTypes().sort((a, b) => {
      if (this.sortAscending()) {
        return a[sortedCol]! > b[sortedCol]! ? 1 : -1;
      } else {
        return a[sortedCol]! > b[sortedCol]! ? -1 : 1;
      }
    });
  });
  sortedColumn = signal<SortedColumn>('id');
  sortAscending = signal<boolean>(true);

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

  sortUserTypes(column: SortedColumn) {
    if (column === this.sortedColumn()) {
      this.sortAscending.set(!this.sortAscending());
    } else {
      this.sortedColumn.set(column);
    }
  }

  removeUserType(id: number) {
    const subscription = this.userTypeApi.removeUserType(id).subscribe(() => {
      this.userTypes.update((old) => old.filter((u) => u.id !== id));
    });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }
}
