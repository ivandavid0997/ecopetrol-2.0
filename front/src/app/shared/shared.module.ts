import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SnackbarComponent } from '../core/components/snackbar/snackbar.component';

@NgModule({
  declarations: [
    SnackbarComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [
    SnackbarComponent
  ]
})
export class SharedModule { }
