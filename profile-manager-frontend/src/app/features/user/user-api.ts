import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { User } from './user';

@Injectable({
  providedIn: 'root',
})
export class UserApi {
  private http = inject(HttpClient);
  private usersApiUrl = `${environment.apiUrl}/users`;

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.usersApiUrl);
  }

  getUser(id: string): Observable<User> {
    return this.http.get<User>(`${this.usersApiUrl}/${id}`);
  }

  addUser(user: User): Observable<any> {
    return this.http.post(`${this.usersApiUrl}`, user);
  }

  editUser(user: User): Observable<any> {
    return this.http.put(`${this.usersApiUrl}/${user.id}`, user);
  }

  deleteUser(id: number): Observable<any> {
    return this.http.delete(`${this.usersApiUrl}/${id}`);
  }
}
