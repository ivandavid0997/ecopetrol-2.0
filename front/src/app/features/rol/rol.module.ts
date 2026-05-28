import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { RolRoutingModule } from './rol-routing.module';
import { RolComponent } from './pages/rol/rol.component';


@NgModule({
  declarations: [
    RolComponent
  ],
  imports: [
    CommonModule,
    RolRoutingModule
  ]
})
export class RolModule { }
