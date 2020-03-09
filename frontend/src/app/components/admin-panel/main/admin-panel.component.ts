import {Component, OnInit} from '@angular/core';
import {PostService} from '../../../service/post.service';
import {CookieService} from 'ngx-cookie-service';
import { MatDrawer } from '@angular/material/sidenav';
import {Route, Router} from '@angular/router';
import {AuthenticationService} from '../../../service/authentication.service';
import {UserService} from '../../../service/user.service';
import {AuthModel} from '../../../models/auth.model';
import {User} from '../../../models/user.model';
import {NotifyService} from '../../../service/notify.service';
import {Location} from '@angular/common';

@Component({
  selector: 'admin-panel',
  templateUrl: './admin-panel.component.html',
})
export class AdminPanelComponent implements OnInit {
  public stateNav;
  public user: User;

  constructor(private router: Router,
              private location: Location,
              private cookie: CookieService,
              private postService: PostService,
              private notifyService: NotifyService,
              private authService: AuthenticationService,
              private userService: UserService) {

    this.stateNav = this.cookie.get('adminPanel') || true;
    this.checkAuthenticated();
  }

  ngOnInit(): void {
    this.printPath('', this.router.config);
  }

  private checkAuthenticated() {
    if (this.authService.isAuthenticated()) {
      const tokenData: AuthModel = this.authService.currentTokenData;
      this.userService.getUserByToken(tokenData.token).subscribe(data => {
        this.user = data;
      }, error => {
        this.notifyService.error('Error', `Message: ${error.message}`);
      });

      this.router.navigate([this.location.path()]);
    } else {
      this.authService.logout();
      this.router.navigate(['/login']);
    }
  }

  public logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  drawerToggle(drawer: MatDrawer) {
    this.cookie.set('adminPanel', (!drawer.opened).toString());
    drawer.toggle();
  }

  printPath(parent: String, config: Route[]) {
    for (let i = 0; i < config.length; i++) {
      const route = config[i];
      // console.log(parent + '/' + route.path);
      if (route.children) {
        const currentPath = route.path ? parent + '/' + route.path : parent;
        this.printPath(currentPath, route.children);
      }
    }
  }
}
