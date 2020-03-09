import {Component} from '@angular/core';
import {AbstractControl, FormControl, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from '../../../service/authentication.service';
import {NotifyService} from '../../../service/notify.service';
import {HttpErrorResponse} from '@angular/common/http';
import {Router} from '@angular/router';

@Component({
  selector: 'my-login-form',
  templateUrl: './login.component.html',
  styleUrls: ['./login.css'],
})
export class LoginComponent {

  public form: FormGroup = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', Validators.required),
  });

  constructor(private authenticationService: AuthenticationService,
              private notifyService: NotifyService,
              private router: Router) {

    this.checkAuthenticated();
  }

  private checkAuthenticated() {
    if (this.authenticationService.isAuthenticated()) {
      this.router.navigate(['/admin']);
    }
  }

  submit() {
    if (this.form.valid) {
      const {email, password}: {[key: string]: AbstractControl} = this.form.controls;

      this.authenticationService.login(email.value, password.value)
        .subscribe(() => {
            this.router.navigate(['/admin']);
          }, (error: HttpErrorResponse) => {
            this.notifyService.error('Error', `Message: ${error.message}`);
          });
    } else {
      this.notifyService.error('Error', `Not valid form`);
    }
  }

}
