import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { VacationRoutingModule } from './vacation-routing.module';
import { VacationComponent } from './pages/vacation/vacation.component';
import { CreateComponent } from './pages/create/create.component';
import { ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    VacationComponent,
    CreateComponent
  ],
  imports: [
    CommonModule,
    VacationRoutingModule,
    ReactiveFormsModule
  ]
})
export class VacationModule { }
