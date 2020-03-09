import {Pipe, PipeTransform} from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import {Observable} from 'rxjs';
import {DatePipe} from '@angular/common';

@Pipe({
  name: 'localizedDate',
  pure: false
})
export class LocalizedDatePipe implements PipeTransform {
  public notice: Observable<any>;
  private observer: any;
  private translatedKey;
  private isShortName = null;
  private readonly monthName: string[];

  constructor(private translateService: TranslateService,
              private datepipe: DatePipe) {
    this.notice = new Observable(observer => {
      this.observer = observer;
    });

    this.monthName = ['january', 'february', 'march', 'april', 'may', 'june',
      'july', 'august', 'september', 'october', 'november', 'december'];
  }

  transform(value: any, pattern: string = 'MMM'): any {
    if (!!value && value !== 0) {
      const date = new Date(value);
      const indexShortMonth = pattern.indexOf('MMM');
      const indexLongMonth = pattern.indexOf('MMMM');
      const name = this.monthName[date.getMonth()];

      this.translatedKey = 'month.MMM.' + name;
      this.isShortName = indexShortMonth >= 0;

      this.translateService.get(this.translatedKey).subscribe(translatedValue => {
        if (!!this.isShortName && this.isShortName) {
          this.getDateString(pattern, 'MMM', date, translatedValue, indexShortMonth);
        } else if (!!this.isShortName && !this.isShortName) {
          this.getDateString(pattern, 'MMMM', date, translatedValue, indexLongMonth);
        }
      });
      return this.observer;
    }
  }

  private getDateString(pattern: string, subputtern: string, date: any, translatedValue, indexMonth): void {
    pattern = pattern.replace(subputtern, '');
    let dateString = this.datepipe.transform(date, pattern);
    dateString = [dateString.slice(0, indexMonth), translatedValue, dateString.slice(indexMonth)].join('');
    this.observer = dateString;
  }

}
