import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserApi {
  private http = inject(HttpClient);
  private usersApiUrl = `${environment.apiUrl}/users`;

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.usersApiUrl);
  }

  addUser(user: User): Observable<any> {
    return this.http.post(`${this.usersApiUrl}/add`, user);
  }
}
