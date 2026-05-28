import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DetailComponent } from './pages/detail/detail.component';
import { BirthdayComponent } from './pages/birthday/birthday.component';

const routes: Routes = [
  {
    path: '',
    children: [
      { path: '', component: BirthdayComponent },

      { path: 'detail/:id', component: DetailComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BirthdayRoutingModule { }
