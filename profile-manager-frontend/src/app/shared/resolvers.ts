import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, ResolveFn, RouterStateSnapshot } from '@angular/router';
import { UserApi } from '../user/user-api';
import { UserTypeApi } from '../user-type/user-type-api';

export const userResolver: ResolveFn<User> = (
  route: ActivatedRouteSnapshot,
  _state: RouterStateSnapshot
) => {
  const userApi = inject(UserApi);
  const userId = route.paramMap.get('id')!;
  return userApi.getUser(userId);
};

export const userTypeResolver: ResolveFn<UserType> = (
  route: ActivatedRouteSnapshot,
  _state: RouterStateSnapshot
) => {
  const userTypeApi = inject(UserTypeApi);
  const userTypeId = route.paramMap.get('id')!;
  return userTypeApi.getUserType(userTypeId);
};

export const userTypesResolver: ResolveFn<UserType[]> = (
  _route: ActivatedRouteSnapshot,
  _state: RouterStateSnapshot
) => {
  const userTypeApi = inject(UserTypeApi);
  return userTypeApi.getUserTypes();
};
