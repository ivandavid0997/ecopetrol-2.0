import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DisabilitiesRoutingModule } from './disabilities-routing.module';
import { DisabilitiesComponent } from './pages/disabilities/disabilities.component';
import { CreateComponent } from './pages/create/create.component';
import { ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    DisabilitiesComponent,
    CreateComponent
  ],
  imports: [
    CommonModule,
    DisabilitiesRoutingModule,
    ReactiveFormsModule
  ]
})
export class DisabilitiesModule { }
