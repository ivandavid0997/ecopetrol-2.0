import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CalamityComponent } from './pages/calamity/calamity.component';
import { CreateComponent } from './pages/create/create.component';

const routes: Routes = [
  {
    path: '',
    children: [
      { path: '', component: CalamityComponent },

      { path: 'create', component: CreateComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CalamityRoutingModule { }
