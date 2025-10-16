import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserTypeApi {
  private http = inject(HttpClient);
  private userTypeApiUrl = `${environment.apiUrl}/user_types`;

  getUserTypes(): Observable<UserType[]> {
    return this.http.get<UserType[]>(this.userTypeApiUrl);
  }

  getUserType(id: string): Observable<UserType> {
    return this.http.get<UserType>(`${this.userTypeApiUrl}/${id}`);
  }

  addUserType(name: string): Observable<any> {
    return this.http.post(this.userTypeApiUrl, { name });
  }

  editUserType(userType: UserType): Observable<any> {
    return this.http.put(`${this.userTypeApiUrl}/${userType.id}`, userType);
  }

  removeUserType(id: number): Observable<any> {
    return this.http.delete(`${this.userTypeApiUrl}/${id}`);
  }
}
