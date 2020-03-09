import {Component} from '@angular/core';
import {PostService} from '../../../../service/post.service';
import {CategoryService} from '../../../../service/category.service';
import * as $ from 'jquery';
import {NotifyService} from '../../../../service/notify.service';
import {Location} from '@angular/common';
import {ActivatedRoute, Router} from '@angular/router';
import {BreakpointObserver} from '@angular/cdk/layout';
import {Category} from '../../../../models/category.model';
import {Post} from '../../../../models/post.model';


@Component({
  selector: 'blog-page',
  templateUrl: './blog.component.html',
  styleUrls: ['./blog.css'],
})
export class BlogComponent {
  public searchParam: string = '';
  public news: Array<Post> = [];
  public totalPages: number;
  public totalPagesArr: Set<number> = new Set<number>();
  public numberPage: number;
  public totalElements: number;
  public recentPosts: any = null;
  public categories: Category[] = [];
  public isLoadingNews = false;

  public imgPreloadUrl: string;
  public categoryId: number;

  constructor(private postService: PostService,
              private categoryService: CategoryService,
              private notifyService: NotifyService,
              private location: Location,
              private activatedRoute: ActivatedRoute,
              private route: Router,
              private breakpointObserver: BreakpointObserver) {

    this.activatedRoute.queryParams.subscribe(params => {
      let page: any = params['page'];
      const category: number = +params['category'];

      if (!page) {
        page = 1;
      }
      if (!!category) {
        this.categoryId = category;
      }

      this.getNews(page);
    });
    this.getRecentPosts();
    this.getTopCategories();
    this.changeImage();
  }

  public getTopCategories(): void {
    this.categoryService.getTopCategories().subscribe(response => {
      if (!!response) {
        this.categories = <Category[]> response;
      }
    });
  }

  public getRecentPosts(): void {
    this.postService.getRecentNews().subscribe(data => {
      if (!!data) {
        this.recentPosts = data;
      }
    });
  }

  public searchNews(): void {
    this.getNews(1);
  }

  public getNews(pageNum: number, isCallPagination?: boolean): void {
    this.isLoadingNews = true;
    this.postService.getNews(pageNum, this.searchParam, this.categoryId).subscribe(json => {
      this.setNewsWithData(json);

      if (!!isCallPagination) {
        $('html, body').animate({
          scrollTop: $($('#blog-standard').get(0)).offset().top
        }, 'slow');
      }
    }, error => {
      this.isLoadingNews = false;
      if (error.status === 0) {
        this.notifyService.error('Error ', 'Server is not available');
      } else {
        this.notifyService.error('Error ', error.message);
      }
    });
  }

  public changeGroupNewsSearch(groupId: number): void {
      this.route.navigate([], {
        relativeTo: this.activatedRoute,
        queryParams: {
          page: 1,
          category: groupId,
        },
        skipLocationChange: true,
      });
  }

  private setNewsWithData(json): void {
    if (!!json) {
      const { content, number, totalElements, totalPages } = json;
      this.news = content;
      this.totalPages = totalPages;
      this.numberPage = number + 1;
      this.totalElements = totalElements;

      const page = this.totalPages < this.numberPage ? 1 : this.numberPage;
      this.changePageUrl(page);
      this.pagination();
    } else {
      this.news = null;
      this.totalPages = 0;
      this.numberPage = 0;
      this.totalElements = 0;
    }
    this.isLoadingNews = false;
  }

  private pagination(): void {
    this.totalPagesArr = new Set<number>();
    const startFor: number = this.numberPage - 2 <= 0 ? 1 : this.numberPage - 2;
    const endFor: number = this.numberPage + 2 > this.totalPages ? this.totalPages : this.numberPage + 2;
    for (let i = startFor; i <= endFor; i++) {
      this.totalPagesArr.add(i);
    }
  }

  private changePageUrl(page: number): void {
    let categoryId: number;
    if (this.categoryId && this.categoryId !== -1) {
      categoryId = this.categoryId;
    }

    this.route.navigate([], {
      relativeTo: this.activatedRoute,
      queryParams: {
        page: page,
        category: categoryId,
      },
      skipLocationChange: true,
    });
  }

  public changeImage(): void {
    this.breakpointObserver.observe(['(max-width: 767px)', '(min-width: 768px)', '(min-width: 992px)'])
      .subscribe(result => {
        if (this.breakpointObserver.isMatched('(max-width: 767px)')) {
          this.imgPreloadUrl = '/assets/image/blog/little-image.png';
        }
        if (this.breakpointObserver.isMatched('(min-width: 768px)')) {
          this.imgPreloadUrl = '/assets/image/blog/middle-image.png';
        }
        if (this.breakpointObserver.isMatched('(min-width: 992px)')) {
          this.imgPreloadUrl = '/assets/image/blog/large-image.png';
        }
      });
  }

  public onLoadImageNews(index: number): void {
    this.news[index].isLoadedImage = true;
  }
}
