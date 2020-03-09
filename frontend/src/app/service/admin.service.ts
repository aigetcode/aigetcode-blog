import {Injectable} from '@angular/core';
import {AppConfig} from '../settings/appConfig';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

@Injectable()
export class AdminService {
  private url = AppConfig.API_ENDPOINT;

  constructor(private http: HttpClient) {
  }

  public getStatisticData(token: string): Observable<any> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.get(this.url + '/statistics', {
      headers
    }).pipe(map(data => data));
  }

  public addVisitor(): void {
    this.http.get(this.url + '/statistics/visit')
      .pipe(map(data => data)).subscribe();
  }
}
