import {Component, Directive, Inject} from '@angular/core';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';


@Component({
  selector: 'dialog-overview',
  templateUrl: 'dialog.post.html',
})
export class DialogPostComponent {
  public dataLocal: any;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any) {
    this.dataLocal = data;
  }

}
