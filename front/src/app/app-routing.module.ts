import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'login',
    loadChildren: () => import('./features/auth/auth.module').then(m => m.AuthModule)
  },
  {
    path: 'rol',
    loadChildren: () => import('./features/rol/rol.module').then(m => m.RolModule)
  },
  {
    path: 'dashboard',
    loadChildren: () => import('./features/employee/dashboard/dashboard.module').then(m => m.DashboardModule)
  },
  {
    path: 'dashboard-leader',
    loadChildren: () => import('./features/leader/dashboard/dashboard.module').then(m => m.DashboardModule)
  },
  {
    path: 'vacation',
    loadChildren: () => import('./features/employee/vacation/vacation.module').then(m => m.VacationModule)
  },
  {
    path: 'disabilities',
    loadChildren: () => import('./features/employee/disabilities/disabilities.module').then(m => m.DisabilitiesModule)
  },
  {
    path: 'calamity',
    loadChildren: () => import('./features/employee/calamity/calamity.module').then(m => m.CalamityModule)
  },
  {
    path: 'birthday',
    loadChildren: () => import('./features/employee/birthday/birthday.module').then(m => m.BirthdayModule)
  },
  {
    path: 'vacation-leader',
    loadChildren: () => import('./features/leader/vacation/vacation.module').then(m => m.VacationModule)
  },
  {
    path: 'disabilities-leader',
    loadChildren: () => import('./features/leader/disabilities/disabilities.module').then(m => m.DisabilitiesModule)
  },
  {
    path: 'calamity-leader',
    loadChildren: () => import('./features/leader/calamity/calamity.module').then(m => m.CalamityModule)
  },
  {
    path: 'birthday-leader',
    loadChildren: () => import('./features/leader/birthday/birthday.module').then(m => m.BirthdayModule)
  },
  {
    path: 'people',
    loadChildren: () => import('./features/people/people.module').then(m => m.PeopleModule)
  },
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: 'login'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
