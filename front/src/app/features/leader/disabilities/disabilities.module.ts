import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DisabilitiesRoutingModule } from './disabilities-routing.module';
import { DisabilitiesComponent } from './pages/disabilities/disabilities.component';
import { DetailComponent } from './pages/detail/detail.component';
import { SharedModule } from '../../../shared/shared.module';


@NgModule({
  declarations: [
    DisabilitiesComponent,
    DetailComponent
  ],
  imports: [
    CommonModule,
    DisabilitiesRoutingModule,
    SharedModule
  ]
})
export class DisabilitiesModule { }
