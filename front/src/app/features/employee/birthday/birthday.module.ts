import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { BirthdayRoutingModule } from './birthday-routing.module';
import { BirthdayComponent } from './pages/birthday/birthday.component';
import { CreateComponent } from './pages/create/create.component';
import { ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    BirthdayComponent,
    CreateComponent
  ],
  imports: [
    CommonModule,
    BirthdayRoutingModule,
    ReactiveFormsModule
  ]
})
export class BirthdayModule { }
