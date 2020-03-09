import {Injectable} from '@angular/core';
import {ToastOptions, ToastyService} from 'ng2-toasty';

const toastOptions: ToastOptions = {
  title: '',
  msg: '',
  showClose: true,
  timeout: 5000,
  theme: 'bootstrap'
};

@Injectable()
export class NotifyService {

  constructor(private toastyService: ToastyService) {
  }

  public success(title: string, msg: string): void {
    toastOptions.title = title;
    toastOptions.msg = msg;
    this.toastyService.success(toastOptions);
  }

  public successMsg(msg: string): void {
    this.success('Success', msg);
  }

  public error(title: string, msg: string): void {
    toastOptions.title = title;
    toastOptions.msg = msg;
    this.toastyService.error(toastOptions);
  }

  public errorMsg(msg: string): void {
    this.error('Error', msg);
  }

  public info(title: string, msg: string): void {
    toastOptions.title = title;
    toastOptions.msg = msg;
    this.toastyService.info(toastOptions);
  }

}
