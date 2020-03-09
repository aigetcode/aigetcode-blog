import {Component, ViewChild} from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import {SelectionModel} from '@angular/cdk/collections';
import {PostService} from '../../../../service/post.service';
import {MatDialog} from '@angular/material/dialog';
import {DialogPostComponent} from './dialog/dialog.post';
import {NotifyService} from '../../../../service/notify.service';

export interface PeriodicElement {
  id: number;
  position: number;
  name: string;
  authorName: string;
  categories: string;
}

@Component({
  selector: 'admin-grid',
  templateUrl: './posts.html',
})
export class GridAdminComponent {
  public displayedColumns: string[] = ['position', 'name', 'authorName', 'categories', 'edit', 'delete'];
  public dataSource = null;
  public selection = new SelectionModel<PeriodicElement>(true, []);
  private elementData: PeriodicElement[] = [];


  @ViewChild(MatPaginator, {static: false}) paginator: MatPaginator;

  constructor(private postService: PostService,
              private dialog: MatDialog,
              private notifyService: NotifyService) {
    this.getData();
  }

  public getData(): void {
    this.postService.getNews(1).subscribe(json => {
      let i = 1;
      for (const content of json.content) {
        let categories = '<ul>';
        for (const category of content.categoriesPosts) {
          categories += '<li>' + category.name + '</li>';
        }
        categories += '</ul>';

        this.elementData.push({
          position: i++,
          id: content.id,
          name: content.title,
          authorName: content.author.fullName,
          categories: categories
        });
      }

      this.dataSource = new MatTableDataSource<PeriodicElement>(this.elementData);
      this.dataSource.paginator = this.paginator;
    });
  }

  public openDialog(postId: number): void {
    const dialogRef = this.dialog.open(DialogPostComponent, {
      data: {
        postId: postId
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.elementData = [];
        this.getData();
      }
    });
  }

  public deletePost(postId: number): void {
    if (confirm('Дейтвительно удалить?')) {
      this.postService.delete(postId).subscribe(
        () => {
          this.elementData = [];
          this.getData();
        },
        error => this.notifyService.error('Ошибка', error.message)
      );
    }
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
}



