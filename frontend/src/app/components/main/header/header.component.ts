import {Component} from '@angular/core';
import {
  Event as RouterEvent,
  NavigationCancel,
  NavigationEnd,
  NavigationError,
  NavigationStart,
  Router
} from '@angular/router';
import {PreloaderService} from '../../../service/preloader.service';
import {TranslateSettingsService} from '../../../service/translate.service';
import * as $ from 'jquery';
import {AdminService} from '../../../service/admin.service';
import {BreakpointObserver} from '@angular/cdk/layout';

@Component({
  selector: 'header-site',
  templateUrl: './header.component.html'
})
export class HeaderComponent {
  public loading: number;
  public srcImg: string;

  constructor(private router: Router,
              private loader: PreloaderService,
              private translate: TranslateSettingsService,
              private adminService: AdminService,
              private breakpointObserver: BreakpointObserver) {
    this.loading = loader.loading;
    this.adminService.addVisitor();

    router.events.subscribe((event: RouterEvent) => {
      this.navigationInterceptor(event)
    });
    this.changeIconImage();
  }

  switchLanguage(language: string) {
    this.translate.switchLanguage(language);
  }

  navigationInterceptor(event: RouterEvent): void {
    if (event instanceof NavigationStart) {
      this.loading = this.loader.start();
    }
    if (event instanceof NavigationEnd) {
      this.loading = this.loader.stop();
    }

    // Set loading state to false in both of the below events to hide the spinner in case a request fails
    if (event instanceof NavigationCancel) {
      this.loading = this.loader.stop();
    }
    if (event instanceof NavigationError) {
      this.loading = this.loader.stop();
    }
  }

  public onInitScripts() {
    $('html, body').animate({scrollTop: 0}, 'slow');
  }

  public deleteSmallMenu() {
    const selector = $('#navbar-collapse');
    if (selector.hasClass('in')) {
      selector.removeClass('in').addClass('collapse');
    }
  }

  public changeIconImage(): void {
    this.breakpointObserver.observe(['(max-width: 767px)', '(min-width: 768px)', '(min-width: 992px)'])
      .subscribe(result => {
        if (this.breakpointObserver.isMatched('(max-width: 767px)')
          || this.breakpointObserver.isMatched('(min-width: 768px)')) {
          this.srcImg = 'assets/image/icon/icon-48_x_49.png';
        }
        if (this.breakpointObserver.isMatched('(min-width: 992px)')) {
          this.srcImg = '/assets/image/icon/icon-100_x_100.png';
        }
      });
  }
}
