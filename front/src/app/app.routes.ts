import { Routes } from '@angular/router';
import { authGuard } from './core/auth.guard';
import { Shell } from './layout/shell';

export const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'login' },
  {
    path: 'login',
    loadComponent: () => import('./pages/login/login').then((m) => m.Login),
  },
  {
    path: 'rol',
    canActivate: [authGuard],
    loadComponent: () => import('./pages/seleccion-rol/seleccion-rol').then((m) => m.SeleccionRol),
  },
  {
    path: '',
    component: Shell,
    canActivate: [authGuard],
    children: [
      {
        path: 'empleado',
        loadComponent: () => import('./pages/empleado/portal-empleado').then((m) => m.PortalEmpleado),
      },
      {
        path: 'lider',
        loadComponent: () => import('./pages/lider/panel-aprobaciones').then((m) => m.PanelAprobaciones),
      },
      {
        path: 'people',
        loadComponent: () => import('./pages/people/reportes-people').then((m) => m.ReportesPeople),
      },
    ],
  },
  { path: '**', redirectTo: 'login' },
];
