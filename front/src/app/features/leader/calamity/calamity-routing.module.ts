import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CalamityComponent } from './pages/calamity/calamity.component';
import { DetailComponent } from './pages/detail/detail.component';

const routes: Routes = [
  {
    path: '',
    children: [
      { path: '', component: CalamityComponent },

      { path: 'detail/:id', component: DetailComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CalamityRoutingModule { }
