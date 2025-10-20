import { Component, DestroyRef, inject, signal } from '@angular/core';
import { UserTypeApi } from '../user-type-api';
import { Router, RouterLink } from '@angular/router';
import { UserTypeRow } from '../user-type-row/user-type-row';
import { UserType } from '../user-type';
import { User } from '../../user/user';
import { TableContainer } from '../../../shared/table-container/table-container';
import { TableHeadings } from '../../../shared/table-container/heading';
import { PageTitle } from '../../../core/page-title/page-title';
import { BigButton } from '../../../shared/big-button/big-button';

@Component({
  selector: 'app-user-type-list',
  imports: [RouterLink, UserTypeRow, TableContainer, PageTitle, BigButton],
  templateUrl: './user-type-list.html',
  styleUrl: './user-type-list.css',
})
export class UserTypeList {
  private userTypeApi = inject(UserTypeApi);
  private router = inject(Router);
  private destroyRef = inject(DestroyRef);
  userTypes = signal<UserType[]>([]);
  headings: TableHeadings<UserType> = {
    sortable: {
      id: {
        title: 'Id',
        cmp: (a, b) => compare(a.id!, b.id!),
      },
      name: {
        title: 'User Type',
        cmp: (a, b) => compare(a.name, b.name),
      },
    },
    other: ['Actions'],
    keys: ['id', 'name'],
  };

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

  goToEditPage(user: User) {
    this.router.navigateByUrl(`user_types/edit/${user.id}`);
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

const compare = (a: string | number, b: string | number) => (a < b ? -1 : a > b ? 1 : 0);
