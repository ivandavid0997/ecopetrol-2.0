import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { VacationRoutingModule } from './vacation-routing.module';
import { VacationComponent } from './pages/vacation/vacation.component';
import { DetailComponent } from './pages/detail/detail.component';
import { SharedModule } from '../../../shared/shared.module';


@NgModule({
  declarations: [
    VacationComponent,
    DetailComponent
  ],
  imports: [
    CommonModule,
    VacationRoutingModule,
    SharedModule
  ]
})
export class VacationModule { }
