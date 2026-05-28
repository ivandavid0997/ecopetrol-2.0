import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CalamityRoutingModule } from './calamity-routing.module';
import { DetailComponent } from './pages/detail/detail.component';
import { CalamityComponent } from './pages/calamity/calamity.component';
import { SharedModule } from '../../../shared/shared.module';


@NgModule({
  declarations: [
    DetailComponent,
    CalamityComponent
  ],
  imports: [
    CommonModule,
    CalamityRoutingModule,
    SharedModule
  ]
})
export class CalamityModule { }
