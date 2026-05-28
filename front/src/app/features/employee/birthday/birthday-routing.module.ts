import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreateComponent } from './pages/create/create.component';
import { BirthdayComponent } from './pages/birthday/birthday.component';

const routes: Routes = [
   {
    path: '',
    children: [
      { path: '', component: BirthdayComponent },

      { path: 'create', component: CreateComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class BirthdayRoutingModule { }
