import {Injectable} from '@angular/core';
import {Post} from '../models/post.model';
import {AppConfig} from '../settings/appConfig';

import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {AuthenticationService} from './authentication.service';
import {AuthModel} from "../models/auth.model";


@Injectable()
export class PostService {
  private url = AppConfig.API_ENDPOINT;

  constructor(private http: HttpClient,
              private authenticationService: AuthenticationService) {}


  public createPost(formData: FormData, token: string): Observable<Post> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.post(this.url + '/post', formData, {
      headers
    })
      .pipe(map(data => <Post> data));
  }

  public updatePost(formData: FormData, token: string): Observable<Post> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.post(this.url + '/post/update', formData, {
      headers
    })
      .pipe(map(data => <Post> data));
  }

  public getNews(pageNum: number, keyword?: string, category?: number): Observable<any> {
    if (!keyword) {
      keyword = '';
    }

    const params: any = {
      page: String(pageNum),
      searchParam: String(keyword),
    };

    if (!!category) {
      params.category = String(category);
    }

    return this.http.get(this.url + '/post', {
      params: params,
    }).pipe(map(data => data));
  }

  public getRecentNews(): Observable<any> {
    return this.http.get(this.url + '/post/recent')
      .pipe(map(data => data));
  }

  public getNewsById(id: number): Observable<any> {
    return this.http.get(this.url + `/post/${id}`)
      .pipe(map(data => data));
  }

  public sendEmail(name: string, toEmail: string, subject: string, text: string): Observable<any> {
    const objectSend = {
      name,
      text,
      email: toEmail,
      subject
    };

    return this.http.post(this.url + '/post/email', objectSend)
      .pipe(response => response);
  }

  public delete(id: number): Observable<any> {
    const authData: AuthModel = this.authenticationService.currentTokenData;
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${authData.token}`
    });
    return this.http.delete(this.url + `/post/${id}`, {headers})
      .pipe(map(data => data));
  }

  public addComment(postId: number, name: string, comment: string): Observable<any> {
    return this.http.post(this.url + `/post/comment/${postId}`, {
      name,
      comment,
    }).pipe(map(data => data));
  }
}
