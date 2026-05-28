import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DisabilitiesComponent } from './pages/disabilities/disabilities.component';
import { DetailComponent } from './pages/detail/detail.component';

const routes: Routes = [
  {
    path: '',
    children: [
      { path: '', component: DisabilitiesComponent },

      { path: 'detail/:id', component: DetailComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DisabilitiesRoutingModule { }
