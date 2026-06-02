import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { ToastService } from '../core/toast.service';
import { Icon } from './icon';

/** Contenedor global de toasts (esquina superior derecha). */
@Component({
  selector: 'app-toast-host',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [Icon],
  template: `
    <div class="pointer-events-none fixed right-4 top-4 z-50 flex w-full max-w-sm flex-col gap-2">
      @for (t of toast.toasts(); track t.id) {
        <div
          class="animate-rise pointer-events-auto flex items-center gap-3 rounded-xl border bg-white px-4 py-3 shadow-lg"
          [class.border-emerald-200]="t.tipo === 'success'"
          [class.border-red-200]="t.tipo === 'error'"
          [class.border-line]="t.tipo === 'info'"
        >
          <span
            class="flex h-6 w-6 shrink-0 items-center justify-center rounded-full text-white"
            [class.bg-emerald-500]="t.tipo === 'success'"
            [class.bg-red-500]="t.tipo === 'error'"
            [class.bg-brand]="t.tipo === 'info'"
          >
            <app-icon [name]="t.tipo === 'error' ? 'x' : 'check'" [size]="14" />
          </span>
          <p class="flex-1 text-sm font-medium text-ink">{{ t.mensaje }}</p>
          <button class="text-slate-400 hover:text-ink" (click)="toast.dismiss(t.id)" aria-label="Cerrar">
            <app-icon name="x" [size]="16" />
          </button>
        </div>
      }
    </div>
  `,
})
export class ToastHost {
  protected readonly toast = inject(ToastService);
}
