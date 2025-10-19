import { UserType } from '../user-type/user-type';

export interface User {
  id?: number;
  firstName: string;
  lastName: string;
  email: string;
  userType: UserType;
}
