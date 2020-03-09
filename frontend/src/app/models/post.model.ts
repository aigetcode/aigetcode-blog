import {User} from './user.model';
import {PostComment} from './postComment.model';
import {Category} from './category.model';

export class Post {
  id?: number;
  author?: User;
  categoriesPosts?: Array<Category>;
  title?: string;
  text?: string;
  createAt?: number;
  previewPost?: string;
  image?: string;
  commentaries?: Array<PostComment>;
  isLoadedImage?: boolean;
}


