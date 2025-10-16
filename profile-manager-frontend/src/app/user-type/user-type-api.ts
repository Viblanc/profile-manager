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
    return this.http
      .get<GetUserTypeResponse>(this.userTypeApiUrl)
      .pipe(map((res) => res._embedded.user_types));
  }

  addUserType(name: string): Observable<any> {
    return this.http.post(this.userTypeApiUrl, { name });
  }
}

interface GetUserTypeResponse {
  _embedded: {
    user_types: UserType[];
  };
}
