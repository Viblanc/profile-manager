import { Routes } from '@angular/router';
import { UserList } from './user/user-list/user-list';
import { NewUser } from './user/new-user/new-user';
import { UserTypeList } from './user-type/user-type-list/user-type-list';
import { NewUserType } from './user-type/new-user-type/new-user-type';
import { userResolver, userTypeResolver, userTypesResolver } from './shared/resolvers';
import { UserProfile } from './user/user-profile/user-profile';

export const routes: Routes = [
  {
    path: 'users',
    component: UserList,
  },
  {
    path: 'users/add',
    component: NewUser,
    resolve: {
      userTypes: userTypesResolver,
    },
  },
  {
    path: 'users/:id',
    component: UserProfile,
    resolve: {
      user: userResolver,
    },
  },
  {
    path: 'users/edit/:id',
    component: NewUser,
    resolve: {
      user: userResolver,
      userTypes: userTypesResolver,
    },
  },
  {
    path: 'user_types',
    component: UserTypeList,
  },
  {
    path: 'user_types/add',
    component: NewUserType,
    pathMatch: 'full',
  },
  {
    path: 'user_types/edit/:id',
    component: NewUserType,
    resolve: {
      userType: userTypeResolver,
    },
  },
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'users',
  },
];
