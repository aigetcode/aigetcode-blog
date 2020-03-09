import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {AppConfig} from '../settings/appConfig';
import {map} from 'rxjs/operators';
import {AuthModel} from '../models/auth.model';

@Injectable()
export class AuthenticationService {
  private tokenSubject: BehaviorSubject<AuthModel>;
  public token: Observable<AuthModel>;

  private url = AppConfig.API_ENDPOINT;

  constructor(private http: HttpClient) {
    this.tokenSubject = new BehaviorSubject<AuthModel>(JSON.parse(localStorage.getItem('token')));
    this.token = this.tokenSubject.asObservable();
  }

  public get currentTokenData(): AuthModel {
    return this.tokenSubject.value;
  }

  public isAuthenticated(): Boolean {
    const data: AuthModel = this.tokenSubject.value;

    if (!!data) {
      const expression: Boolean = !!data.expiredIn && Date.now() <= data.expiredIn;
      return expression && !!data.token;
    }
    return false;
  }

  login(email: string, password: string): Observable<AuthModel> {
    return this.http.post<any>(this.url + '/token/generate', { email, password })
      .pipe(map(tokenData => {
        // store tokenData details and jwt token in local storage to keep tokenData logged in between page refreshes
        localStorage.setItem('token', JSON.stringify(tokenData));
        this.tokenSubject.next(tokenData);
        return <AuthModel> tokenData;
      }));
  }

  logout() {
    // remove user from local storage to log user out
    localStorage.removeItem('token');
    this.tokenSubject.next(null);
  }
}
