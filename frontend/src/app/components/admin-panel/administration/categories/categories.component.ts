import {Component, ViewChild} from '@angular/core';
import {NotifyService} from '../../../../service/notify.service';
import {MatTableDataSource} from '@angular/material/table';
import {SelectionModel} from '@angular/cdk/collections';
import {MatPaginator} from '@angular/material/paginator';
import {CategoryService} from '../../../../service/category.service';
import {Category} from '../../../../models/category.model';
import {MatDialog} from '@angular/material/dialog';
import {DialogCategoryComponent} from './dialog/dialog.category';

@Component({
  templateUrl: './categories.component.html'
})
export class CategoriesComponent {
  public displayedColumns: string[] = ['id', 'name', 'edit', 'delete'];
  public dataSource = null;
  public selection = new SelectionModel<Category>(true, []);
  private elementData: Category[] = [];
  public editDataVisible: boolean = false;
  public editDataModel: Category;

  @ViewChild(MatPaginator, {static: false}) paginator: MatPaginator;

  constructor(private categoriesService: CategoryService,
              private notifyService: NotifyService,
              private dialog: MatDialog) {
    this.getData();
  }

  public getData(): void {
    this.categoriesService.getAllCategories().subscribe(json => {
      for (const content of json) {
        this.elementData.push({
          id: content.id,
          name: content.name,
        });
      }

      this.dataSource = new MatTableDataSource<Category>(this.elementData);
      this.dataSource.paginator = this.paginator;
    });
  }

  public delete(id: number): void {
    if (confirm('Дейтвительно удалить?')) {
      this.categoriesService.delete(id).subscribe((data: boolean) => {
        if (data) {
          this.notifyService.successMsg('Категория удалена');
          this.elementData = [];
          this.getData();
        } else {
          this.notifyService.errorMsg('Категория не удалена. Возникли проблемы');
        }
      });
    }
  }

  public editData(element: Category): void {
    this.editDataVisible = true;
    this.editDataModel = element;
  }

  public saveEditData(element: Category): void {
    this.editDataVisible = false;
    element.name = this.editDataModel.name;
    this.categoriesService.update(this.editDataModel).subscribe(
      () => this.notifyService.successMsg('Категория обновлена'),
      () => this.notifyService.errorMsg('Категория не обновлена. Возникли проблемы')
    );
  }

  public create() {
    const dialogRef = this.dialog.open(DialogCategoryComponent);
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const category: Category = {
          name: result
        };
        this.categoriesService.create(category).subscribe(
          () => {
            this.notifyService.successMsg('Создана категория');
            this.elementData = [];
            this.getData();
          },
          () => this.notifyService.errorMsg('Категория не создана. Возникли проблемы')
        );
      }
    });
  }

  applyFilter(filterValue: string): void {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
}
