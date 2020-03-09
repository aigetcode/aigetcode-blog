import {Injectable} from '@angular/core';
import {AppConfig} from '../settings/appConfig';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {User} from '../models/user.model';
import {map} from 'rxjs/operators';

@Injectable()
export class UserService {

  private url = AppConfig.API_ENDPOINT;

  constructor(private http: HttpClient) {}

  getUserByToken(token: string): Observable<User> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.get(this.url + '/user', {
      headers
    }).pipe(map(data => data));
  }

}
