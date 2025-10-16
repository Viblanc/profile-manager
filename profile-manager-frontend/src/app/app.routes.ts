import { Routes } from '@angular/router';
import { UserList } from './user/user-list/user-list';
import { NewUser } from './user/new-user/new-user';
import { UserTypeList } from './user-type/user-type-list/user-type-list';
import { NewUserType } from './user-type/new-user-type/new-user-type';

export const routes: Routes = [
  {
    path: 'users',
    component: UserList,
  },
  {
    path: 'users/add',
    component: NewUser,
  },
  {
    path: 'user_types',
    component: UserTypeList,
  },
  {
    path: 'user_types/add',
    component: NewUserType,
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'users',
  },
];
