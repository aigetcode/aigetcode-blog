import {Component} from '@angular/core';
import {PostService} from '../../../../service/post.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NotifyService} from '../../../../service/notify.service';
import {TranslateService} from '@ngx-translate/core';
import {BreakpointObserver} from '@angular/cdk/layout';

@Component({
  selector: 'portfolio-page',
  templateUrl: './portfolio.component.html'
})
export class PortfolioComponent {
  public formGroup: FormGroup;
  public submitted: boolean = false;
  public isLoading: boolean = true;
  public srcImg: string;

  constructor(private readonly postService: PostService,
              private readonly formBuilder: FormBuilder,
              private readonly notifyService: NotifyService,
              private readonly translate: TranslateService,
              private readonly breakpointObserver: BreakpointObserver) {

    this.formGroup = this.formBuilder.group({
      name: ['', Validators.required],
      text: ['', Validators.required],
      subject: ['', Validators.required],
      toEmail: ['', [Validators.required, Validators.email]],
    });
    this.changeImageForDomWidth();
  }

  public get field() { return this.formGroup.controls; }

  public sendEmailContact(): void {
    const {name, toEmail, subject, text} = this.formGroup.controls;
    this.submitted = true;

    if (this.formGroup.invalid) {
      const title = this.translate.instant('portfolio.error');
      const msg = this.translate.instant('portfolio.errorMsg_1');
      this.notifyService.error(title, msg);
      return;
    }

    this.postService.sendEmail(name.value, toEmail.value, subject.value, text.value)
      .subscribe(
        () => {
          const title = this.translate.instant('portfolio.success');
          const msg = this.translate.instant('portfolio.successMsg_1');
          this.notifyService.success(title, msg);

          this.formGroup.reset();
          this.formGroup.enable();
          this.submitted = false;
        },
        error => {
          const title = this.translate.instant('portfolio.error');
          const msg = this.translate.instant('portfolio.errorMsg_2');
          this.notifyService.error(title, msg + ' ' + error.message);
          this.formGroup.enable();
        }
      );
    this.formGroup.disable();
  }

  public onLoadImage(): void {
    this.isLoading = false;
  }

  public changeImageForDomWidth(): void {
    this.breakpointObserver.observe(['(max-width: 767px)', '(min-width: 768px)', '(min-width: 992px)'])
      .subscribe(result => {
        if (this.breakpointObserver.isMatched('(max-width: 767px)')
              || this.breakpointObserver.isMatched('(min-width: 768px)')) {
          this.srcImg = '/assets/image/icon/me-330_x_360.jpg';
        }
        if (this.breakpointObserver.isMatched('(min-width: 992px)')) {
          this.srcImg = '/assets/image/icon/me-555_x_600.jpg';
        }
      });
  }
}
