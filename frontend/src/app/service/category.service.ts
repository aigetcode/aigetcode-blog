import {Injectable} from '@angular/core';
import {AppConfig} from '../settings/appConfig';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {Category} from '../models/category.model';
import {AuthModel} from '../models/auth.model';
import {AuthenticationService} from './authentication.service';

@Injectable()
export class CategoryService {
  private url = AppConfig.API_ENDPOINT;
  private tokenObj: AuthModel;
  private header: HttpHeaders;

  constructor(private http: HttpClient,
              private authenticationService: AuthenticationService) {
    this.tokenObj = this.authenticationService.currentTokenData;
    if (this.tokenObj) {
      this.header = new HttpHeaders({
        'Authorization': `Bearer ${this.tokenObj.token}`
      });
    }
  }

  public getTopCategories(): Observable<any> {
    return this.http.get(this.url + '/category/top')
      .pipe(map(data => {
        return data;
      }));
  }

  public getAllCategories(): Observable<any> {
    return this.http.get(this.url + '/category')
      .pipe(map(data => {
        return data;
      }));
  }

  public delete(id: number): Observable<any> {
    if (!this.header) {
      this.getHeader();
    }
    return this.http.delete(this.url + `/category/${id}`, {headers: this.header})
      .pipe(map(data => {
        return data;
      }));
  }

  public update(category: Category): Observable<any> {
    if (!this.header) {
      this.getHeader();
    }
    return this.http.put(this.url + `/category`, category, {
      headers: this.header
    })
      .pipe(map(data => {
        return data;
      }));
  }

  public create(category: Category): Observable<any> {
    if (!this.header) {
      this.getHeader();
    }
    return this.http.post(this.url + `/category`, category, {
      headers: this.header
    })
      .pipe(map(data => {
        return data;
      }));
  }

  private getHeader(): void {
    if (this.tokenObj) {
      this.header = new HttpHeaders({
        'Authorization': `Bearer ${this.tokenObj.token}`
      });
    }
  }
}
