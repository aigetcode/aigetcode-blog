import {Component} from '@angular/core';
import {PostService} from '../../../../../service/post.service';
import {Router} from '@angular/router';
import {Post} from '../../../../../models/post.model';
import {NotifyService} from '../../../../../service/notify.service';
import {PostComment} from '../../../../../models/postComment.model';
import {TranslateService} from '@ngx-translate/core';

@Component({
  selector: 'post-page',
  templateUrl: './post-page.component.html'
})
export class PostPageComponent {
  public news: Post = null;
  public stringCategories: string = '';

  public commentName: string;
  public commentText: string;

  constructor(private postService: PostService,
              private router: Router,
              private notifyService: NotifyService,
              private readonly translate: TranslateService) {
    const url: string = this.router.url;
    const subUrls: string[] = url.split('/');
    const idPost: string = subUrls[subUrls.length - 1];

    this.postService.getNewsById(+idPost).subscribe(news => {
      if (!!news) {
        this.news = news;

        const { categoriesPosts } = this.news;
        for (let count = 0; count <= categoriesPosts.length - 2; count++) {
          this.stringCategories += categoriesPosts[count].name + ', ';
        }
        this.stringCategories += categoriesPosts[categoriesPosts.length - 1].name;
        setTimeout(() => this.highlightCode(), 0);
      }
    });
  }

  private highlightCode() {
    document.querySelectorAll('pre code').forEach((block) => {
      hljs.highlightBlock(block);
    });
  }

  public addComment(): void {
    this.postService.addComment(this.news.id, this.commentName, this.commentText).subscribe(() => {
      const commentary: PostComment = {
        name: this.commentName,
        comment: this.commentText,
        createAt: Date.now(),
      };
      this.commentName = '';
      this.commentText = '';

      if (this.news.commentaries) {
        this.news.commentaries.push(commentary);
      } else {
        this.news.commentaries = [commentary];
      }

      const msg = this.translate.instant('blog.labelSuccessComment');
      this.notifyService.successMsg(msg);
    }, (error) => {

      const msg = this.translate.instant('blog.labelError');
      this.notifyService.errorMsg(msg + ' ' + error.message);
    });
  }

}
