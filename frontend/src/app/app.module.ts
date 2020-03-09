// module
import { BrowserModule } from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AgmCoreModule } from '@agm/core';
import {Routes, RouterModule} from '@angular/router';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {
  TranslateModule, TranslateLoader
} from '@ngx-translate/core';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { MatLabel } from '@angular/material/form-field';
import { MatIconModule, MatIcon } from '@angular/material/icon';
import { MatListModule, MatListItem, MatNavList } from '@angular/material/list';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbar, MatToolbarRow, MatToolbarModule } from '@angular/material/toolbar';

import * as paginator from '@angular/material/paginator';
import * as table from '@angular/material/table';
import * as formField from '@angular/material/form-field';
import * as input from '@angular/material/input';
import * as checkbox from '@angular/material/checkbox';
import * as card from '@angular/material/card';
import * as select from '@angular/material/select';
import * as dialog from '@angular/material/dialog';
import {ToastyModule} from 'ng2-toasty';
import {CKEditorModule} from 'ng2-ckeditor';
import { ChartsModule } from 'ng2-charts';
import { ServiceWorkerModule } from '@angular/service-worker';
import {QuicklinkModule, QuicklinkStrategy} from 'ngx-quicklink';


// component
import { HeaderComponent } from './components/main/header/header.component';
import { ApplicationComponent } from './routes/app.routes.component';
import { BlogComponent } from './components/main/pages/blog/blog.component';
import { PostPageComponent } from './components/main/pages/blog/post-page/post-page.component';
import { AdminPanelComponent } from './components/admin-panel/main/admin-panel.component';
import { AppComponent } from './components/main/pages/main-page/main.component';
import { PortfolioComponent } from './components/main/pages/portfolio/portfolio.component';
import {FooterMainComponent} from './components/main/footer/footer.component';
import {RouteMainComponent} from './components/main/route/route.component';
import {PageNotFoundComponent} from './components/main/pages/404/404.component';
import {GridAdminComponent} from './components/admin-panel/administration/posts/posts';
import {StatisticsComponent} from './components/admin-panel/statistics/statistics.component';

// service
import { PreloaderService } from './service/preloader.service';
import {PostService} from './service/post.service';
import {CookieService} from 'ngx-cookie-service';
import {DatePipe} from '@angular/common';
import {TranslateSettingsService} from './service/translate.service';
import {LocalizedDatePipe} from './pipe/localizeDate.pipe';
import {CreateComponent} from './components/admin-panel/createPost/create.component';
import {MainRouteComponent} from './components/route.component';
import {AppRoutes} from './routes.path';
import {CategoryService} from './service/category.service';
import {NotifyService} from './service/notify.service';
import {AdminService} from './service/admin.service';
import {AuthenticationService} from './service/authentication.service';
import {LoginComponent} from './components/admin-panel/login/login.component';
import {UserService} from './service/user.service';
import {DialogPostComponent} from './components/admin-panel/administration/posts/dialog/dialog.post';
import {CreateEditPostComponent} from './components/admin-panel/createEditPostComponent/createEditPost.component';
import {CategoriesComponent} from './components/admin-panel/administration/categories/categories.component';
import {DialogCategoryComponent} from './components/admin-panel/administration/categories/dialog/dialog.category';
import {environment} from '../environments/environment';
import {SafeHtmlPipe} from './pipe/safeHtmlPipe.pipe';


// определение маршрутов
const appRoutes: Routes = AppRoutes;

export function HttpLoaderFactory(httpClient: HttpClient) {
  return new TranslateHttpLoader(httpClient);
}

@NgModule({
  declarations: [
    // main
    MainRouteComponent,
    PageNotFoundComponent,

    // blog
    RouteMainComponent,
    ApplicationComponent,
    AppComponent,
    HeaderComponent,
    BlogComponent,
    PortfolioComponent,
    PostPageComponent,
    FooterMainComponent,

    // admin
    AdminPanelComponent,
    CreateEditPostComponent,
    GridAdminComponent,
    StatisticsComponent,
    LoginComponent,
    DialogPostComponent,
    CreateEditPostComponent,
    CategoriesComponent,
    DialogCategoryComponent,
    CreateComponent,

    // pipe
    LocalizedDatePipe,
    SafeHtmlPipe
  ],
  imports: [
    AgmCoreModule.forRoot({
      apiKey: 'AIzaSyCBRW3OEiA1PSvuy_N0f5xiZZKXGjhDD24',
      language: localStorage && localStorage.gml || 'en'
    }),
    QuicklinkModule,
    RouterModule.forRoot(appRoutes, {
      preloadingStrategy: QuicklinkStrategy,
      scrollPositionRestoration: 'enabled'
    }),
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient]
      }
    }),
    ToastyModule.forRoot(),
    ServiceWorkerModule.register('ngsw-worker.js', { enabled: environment.production }),

    BrowserModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatSidenavModule,
    MatIconModule,
    MatListModule,
    MatToolbarModule,
    CKEditorModule,
    ChartsModule,

    table.MatTableModule,
    paginator.MatPaginatorModule,
    formField.MatFormFieldModule,
    input.MatInputModule,
    checkbox.MatCheckboxModule,
    card.MatCardModule,
    select.MatSelectModule,
    dialog.MatDialogModule,
  ],
  providers: [
    AdminService,
    PostService,
    CategoryService,
    PreloaderService,
    CookieService,
    TranslateSettingsService,
    NotifyService,
    AuthenticationService,
    UserService,
    DatePipe
  ],
  bootstrap: [
    ApplicationComponent
  ],
  schemas: [
    // angular material
    MatListItem, MatNavList, MatIcon,
    MatLabel, MatToolbar, MatToolbarRow,
    table.MatRow, table.MatCell, table.MatCellDef, table.MatColumnDef,
    table.MatHeaderCell, table.MatHeaderCellDef, table.MatRowDef, table.MatTable,
    table.MatTableDataSource, table.MatHeaderRow,
    paginator.MatPaginator, paginator.MatPaginatorIntl,
    formField.MatFormField, formField.MatFormFieldControl,
    input.MatInput,
    checkbox.MatCheckbox, card.MatCard,
    select.MatSelect, select.MatSelectChange,
    dialog.MatDialog, dialog.MatDialogTitle, dialog.MatDialogContent,
    dialog.MatDialogActions, dialog.MatDialogClose,
  ],
  entryComponents: [
    DialogPostComponent,
    DialogCategoryComponent
  ]
})
export class AppModule { }
