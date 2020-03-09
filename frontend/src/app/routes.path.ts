import {Routes} from '@angular/router';
import {MainRouteComponent} from './components/route.component';
import {RouteMainComponent} from './components/main/route/route.component';
import {AppComponent} from './components/main/pages/main-page/main.component';
import {BlogComponent} from './components/main/pages/blog/blog.component';
import {PostPageComponent} from './components/main/pages/blog/post-page/post-page.component';
import {PortfolioComponent} from './components/main/pages/portfolio/portfolio.component';
import {AdminPanelComponent} from './components/admin-panel/main/admin-panel.component';
import {GridAdminComponent} from './components/admin-panel/administration/posts/posts';
import {CreateComponent} from './components/admin-panel/createPost/create.component';
import {PageNotFoundComponent} from './components/main/pages/404/404.component';
import {StatisticsComponent} from './components/admin-panel/statistics/statistics.component';
import {LoginComponent} from './components/admin-panel/login/login.component';
import {CategoriesComponent} from './components/admin-panel/administration/categories/categories.component';

export const AppRoutes: Routes = [
  {
    path: '',
    component: MainRouteComponent,
    children: [
      {
        path: '',
        component: RouteMainComponent,
        children: [
          { path: '', component: AppComponent },
          { path: 'blog', component: BlogComponent },
          { path: 'blog/post/:id', component: PostPageComponent },
          { path: 'portfolio', component: PortfolioComponent },
        ]
      },
      {
        path: 'admin', component: AdminPanelComponent, children: [
          { path: '', component: StatisticsComponent },
          { path: 'news-create', component: CreateComponent },
          { path: 'news-list', component: GridAdminComponent },
          { path: 'categories', component: CategoriesComponent }
        ]
      },
      {
        path: 'login', component: LoginComponent,
      }
    ]
  },
  { path: '**', component: PageNotFoundComponent }
];
