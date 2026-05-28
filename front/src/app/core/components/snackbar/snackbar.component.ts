import { Component, EventEmitter, Input, Output } from '@angular/core';

export type SnackbarType = 'success' | 'error' | 'warning' | 'info';

@Component({
  selector: 'app-snackbar',
  templateUrl: './snackbar.component.html',
  styleUrls: ['./snackbar.component.css']
})
export class SnackbarComponent {

  @Input() message: string = '';
  @Input() type: SnackbarType = 'info';
  @Input() show: boolean = false;
  @Input() showConfirmButton: boolean = false;
  @Input() confirmText: string = 'Confirmar';
  @Input() closeText: string = 'Cerrar';
  @Input() autoClose: boolean = false;
  @Input() duration: number = 3000;

  @Output() confirmed: EventEmitter<void> = new EventEmitter<void>();
  @Output() closed: EventEmitter<void> = new EventEmitter<void>();

  private timeoutRef: any;

  ngOnChanges(): void {
    if (this.show && this.autoClose) {
      this.startAutoClose();
    }
  }

  onConfirm(): void {
    this.confirmed.emit();
    this.close();
  }

  onClose(): void {
    this.close();
  }

  private close(): void {
    this.show = false;
    this.closed.emit();
    this.clearTimeout();
  }

  private startAutoClose(): void {
    this.clearTimeout();
    this.timeoutRef = setTimeout(() => {
      this.close();
    }, this.duration);
  }

  private clearTimeout(): void {
    if (this.timeoutRef) {
      clearTimeout(this.timeoutRef);
      this.timeoutRef = null;
    }
  }

  get snackbarClasses(): string {
    return `snackbar snackbar-${this.type} ${this.show ? 'show' : ''}`;
  }
}
