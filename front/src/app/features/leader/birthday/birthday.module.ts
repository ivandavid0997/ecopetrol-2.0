import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { BirthdayRoutingModule } from './birthday-routing.module';
import { DetailComponent } from './pages/detail/detail.component';
import { BirthdayComponent } from './pages/birthday/birthday.component';
import { SharedModule } from '../../../shared/shared.module';


@NgModule({
  declarations: [
    DetailComponent,
    BirthdayComponent
  ],
  imports: [
    CommonModule,
    BirthdayRoutingModule,
    SharedModule
  ]
})
export class BirthdayModule { }
