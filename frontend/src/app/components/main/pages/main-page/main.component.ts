import {Component, OnInit} from '@angular/core';
import * as $ from 'jquery';
import {AppConfig} from '../../../../settings/appConfig';
import {BreakpointObserver} from '@angular/cdk/layout';

@Component({
  selector: 'main-page',
  templateUrl: './main.component.html'
})
export class AppComponent implements OnInit {
  public lat: number = AppConfig.LAT_POSITION;
  public lng: number = AppConfig.LNG_POSITION;

  public imgPreloadUrl: string = 'assets/image/icon/main-image.jpg';
  public imgUrl: string;

  constructor(private breakpointObserver: BreakpointObserver) {
    this.changeMainImage();
  }

  public ngOnInit(): void {
    $('.container-full-height').height($(window).height());
  }

  public changeMainImage(): void {
    this.breakpointObserver.observe(['(max-width: 767px)', '(min-width: 768px)', '(min-width: 992px)'])
      .subscribe(result => {
        if (this.breakpointObserver.isMatched('(max-width: 767px)')) {
          this.imgPreloadUrl = 'assets/image/icon/main-image-1000_x_1000.jpg';
        }
        if (this.breakpointObserver.isMatched('(min-width: 768px)')
          || this.breakpointObserver.isMatched('(min-width: 992px)')) {
          this.imgPreloadUrl = 'assets/image/icon/main-image-1920_x_1080.jpg';
        }
      });
  }
}
