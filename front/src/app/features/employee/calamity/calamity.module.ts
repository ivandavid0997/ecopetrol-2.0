import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CalamityRoutingModule } from './calamity-routing.module';
import { CalamityComponent } from './pages/calamity/calamity.component';
import { CreateComponent } from './pages/create/create.component';
import { ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    CalamityComponent,
    CreateComponent
  ],
  imports: [
    CommonModule,
    CalamityRoutingModule,
    ReactiveFormsModule
  ]
})
export class CalamityModule { }
