import {Component} from '@angular/core';

@Component({
  selector: 'route-main',
  template: '<div data-spy="scroll" data-offset="60" data-target=".navbar-fixed-top">' +
            '  <!--header-->' +
            '  <header-site></header-site>' +
            '  <!--main-->' +
            '  <router-outlet></router-outlet>' +
            '  <!-- Footer -->' +
            '  <footer-site></footer-site>' +
            '</div>'
})
export class RouteMainComponent {}

