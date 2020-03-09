import {TranslateService} from '@ngx-translate/core';
import {Injectable} from '@angular/core';
import {CookieService} from 'ngx-cookie-service';

@Injectable()
export class TranslateSettingsService {
  private language: string;

  constructor(public translateInject: TranslateService,
              private cookie: CookieService) {
    translateInject.addLangs(['en', 'ru']);
    translateInject.setDefaultLang('ru');

    const language = cookie.get('languageBlog');
    if (language == null || language === '') {
      this.switchLanguage('ru');
    } else {
      this.switchLanguage(language);
    }
  }

  switchLanguage(language: string) {
    this.language = language;
    this.translateInject.use(language);
    this.cookie.set('languageBlog', language);
  }

  getLanguage() {
    return this.language;
  }

}
