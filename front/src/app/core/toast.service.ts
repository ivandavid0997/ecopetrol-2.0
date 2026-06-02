import { Injectable, signal } from '@angular/core';

export interface Toast {
  id: number;
  mensaje: string;
  tipo: 'success' | 'error' | 'info';
}

@Injectable({ providedIn: 'root' })
export class ToastService {
  private seq = 0;
  readonly toasts = signal<Toast[]>([]);

  show(mensaje: string, tipo: Toast['tipo'] = 'success', duracion = 3500): void {
    const id = ++this.seq;
    this.toasts.update((list) => [...list, { id, mensaje, tipo }]);
    setTimeout(() => this.dismiss(id), duracion);
  }

  success(mensaje: string) {
    this.show(mensaje, 'success');
  }
  error(mensaje: string) {
    this.show(mensaje, 'error');
  }

  dismiss(id: number): void {
    this.toasts.update((list) => list.filter((t) => t.id !== id));
  }
}
