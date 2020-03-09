import {Component, Input, OnInit} from '@angular/core';
import {Post} from '../../../models/post.model';
import {PostService} from '../../../service/post.service';
import {NotifyService} from '../../../service/notify.service';
import {CategoryService} from '../../../service/category.service';
import {Category} from '../../../models/category.model';
import {AuthenticationService} from '../../../service/authentication.service';
import {AuthModel} from '../../../models/auth.model';


@Component({
  selector: 'create-edit-post-component',
  templateUrl: './createEditPost.component.html',
  styleUrls: [
    './createEditPost.component.css',
  ]
})
export class CreateEditPostComponent implements OnInit {
  @Input() postId: number;


  public contentCKE: string;
  public ckeConfig: any;
  public title: string;
  public categoryLoadList: Category[];
  public selectCategory: any;
  private post: Post;
  public oldSelectedCategories: string;

  public fileData: File = null;
  public previewUrl: any = null;

  constructor(private postService: PostService,
              private notifyService: NotifyService,
              private categoryService: CategoryService,
              private authenticationService: AuthenticationService) {
    this.contentCKE = '';
    this.title = '';
    this.categoryLoadList = [];
    this.selectCategory = [];

    this.initCkeConfig();
    this.loadCategories();
  }

  ngOnInit(): void {
    if (this.postId) {
      this.postService.getNewsById(this.postId).subscribe((data: Post) => {
        if (!data) {
          this.notifyService.errorMsg('Post don\'t exist');
          return;
        }
        this.post = data;

        this.contentCKE = data.text;
        this.title = data.title;
        this.previewUrl = data.image;

        this.oldSelectedCategories = '';
        for (const category of data.categoriesPosts) {
          this.oldSelectedCategories += category.name + ', ';
        }
        this.oldSelectedCategories = this.oldSelectedCategories.substring(0, this.oldSelectedCategories.length - 2);
      }, error => {
        this.notifyService.error('Error', `Message: ${error.message}`);
      });
    }
  }

  public fileProgress(fileInput: any): void {
    this.fileData = <File>fileInput.target.files[0];
    this.preview();
  }

  public preview(): void {
    const mimeType = this.fileData.type;
    if (mimeType.match(/image\/*/) == null) {
      return;
    }

    const reader = new FileReader();
    reader.readAsDataURL(this.fileData);
    reader.onload = () => {
      this.previewUrl = reader.result;
    }
  }

  public clearImage(): void {
    this.fileData = null;
    this.previewUrl = null;
  }

  public savePost(): void {
    if ((this.title === '' || this.contentCKE === '' || this.selectCategory.length === 0) && !this.postId) {
      this.notifyService.error('Ошибка', 'Заполните обязательные поля');
      return;
    }

    const post: Post = {
      title: this.title,
      text: this.contentCKE,
      categoriesPosts: this.selectCategory,
    };

    if (this.postId) {
      post.id = this.postId;
    }
    const formDataPost: FormData = new FormData();
    formDataPost.append('post', new Blob([JSON.stringify(post)], {
      type: 'application/json'
    }));
    if (this.fileData !== null) {
      formDataPost.append('file', this.fileData);
    }

    const data: AuthModel = this.authenticationService.currentTokenData;
    if (this.postId) {
      this.postService.updatePost(formDataPost, data.token).subscribe(() => {
        this.notifyService.success('Успешно', 'Успешно обновлена статья');
      }, (error) => {
        const msg = 'Произошла ошибка: ' + error.message;
        this.notifyService.error('Ошибка', msg);
      });
    } else {
      this.postService.createPost(formDataPost, data.token).subscribe(() => {
        this.notifyService.success('Успешно', 'Успешно добавлена статья');
        this.clearForms();
      }, (error) => {
        const msg = 'Произошла ошибка: ' + error.message;
        this.notifyService.error('Ошибка', msg);
      });
    }
  }

  public loadCategories(): void {
    this.categoryService.getAllCategories().subscribe(response => {
      this.categoryLoadList = <Category[]> response;
    });
  }

  private clearForms(): void {
    this.title = '';
    this.contentCKE = ``;
    this.selectCategory = [];
    this.clearImage();
  }

  initCkeConfig() {
    this.ckeConfig = {
      allowedContent: false,
      extraPlugins: 'divarea,codesnippet',
      codeSnippet_theme: 'monokai_sublime',
      forcePasteAsPlainText: true,
      height: '45em',
      toolbarGroups: [
        { name: 'document', groups: [ 'mode', 'doctools', 'document' ] },
        { name: 'clipboard', groups: [ 'clipboard', 'undo' ] },
        { name: 'editing', groups: [ 'find', 'selection', 'spellchecker', 'editing' ] },
        { name: 'forms', groups: [ 'forms' ] },
        { name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
        { name: 'paragraph', groups: [ 'list', 'indent', 'blocks', 'align', 'bidi', 'paragraph' ] },
        { name: 'links', groups: [ 'links' ] },
        { name: 'insert', groups: [ 'insert' ] },
        { name: 'styles', groups: [ 'styles' ] },
        { name: 'colors', groups: [ 'colors' ] },
        { name: 'tools', groups: [ 'tools' ] },
        { name: 'others', groups: [ 'others' ] },
        { name: 'about', groups: [ 'about' ] }
      ],
      removeButtons: 'Preview,NewPage,Print,Flash,PageBreak,Iframe,Save,Templates,Source,Cut,Copy,Paste,PasteFromWord,PasteText,Find,SelectAll,Form,Checkbox,Radio,TextField,Textarea,Select,ImageButton,Button,HiddenField,CopyFormatting,CreateDiv,BidiLtr,BidiRtl,Language,Anchor,About'
    };
  }

}
