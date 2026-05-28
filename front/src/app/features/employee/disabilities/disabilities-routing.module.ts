import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DisabilitiesComponent } from './pages/disabilities/disabilities.component';
import { CreateComponent } from './pages/create/create.component';

const routes: Routes = [
  {
    path: '',
    children: [
      { path: '', component: DisabilitiesComponent },

      { path: 'create', component: CreateComponent }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DisabilitiesRoutingModule { }
