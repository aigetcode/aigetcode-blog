import {Component} from '@angular/core';


@Component({
  template: '<router-outlet></router-outlet>' +
    '<ng2-toasty [position]="\'top-right\'"></ng2-toasty>'
})
export class MainRouteComponent {}
