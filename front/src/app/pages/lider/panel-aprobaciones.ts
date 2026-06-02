import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { EstadoSolicitud, Solicitud, TIPOS_META, TipoPermiso } from '../../core/models';
import { SolicitudesStore } from '../../core/solicitudes.store';
import { ToastService } from '../../core/toast.service';
import { formatRango } from '../../core/util';
import { Icon } from '../../shared/icon';
import { StatusBadge } from '../../shared/status-badge';

type Filtro = 'todas' | EstadoSolicitud;

@Component({
  selector: 'app-panel-aprobaciones',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [Icon, StatusBadge],
  template: `
    <h1 class="mb-6 text-3xl font-bold text-ink">Panel de Aprobaciones</h1>

    <!-- KPIs -->
    <div class="grid grid-cols-2 gap-4 lg:grid-cols-4">
      @for (k of kpis(); track k.label) {
        <div class="app-card flex items-center justify-between border-t-4 p-5" [class]="k.accent">
          <div>
            <p class="text-sm text-muted">{{ k.label }}</p>
            <p class="mt-1 text-3xl font-bold text-ink">{{ k.valor }}</p>
          </div>
          <span class="flex h-11 w-11 items-center justify-center rounded-xl" [class]="k.iconBg">
            <app-icon [name]="k.icon" [size]="22" />
          </span>
        </div>
      }
    </div>

    <!-- Lista -->
    <section class="app-card mt-6 p-6 sm:p-7">
      <div class="flex flex-wrap items-center justify-between gap-3">
        <h2 class="text-xl font-bold text-ink">Solicitudes</h2>
        <div class="flex flex-wrap gap-1.5 rounded-xl bg-slate-100 p-1">
          @for (f of filtros; track f.valor) {
            <button
              class="rounded-lg px-3 py-1.5 text-sm font-medium transition"
              [class.bg-brand]="filtro() === f.valor"
              [class.text-white]="filtro() === f.valor"
              [class.text-muted]="filtro() !== f.valor"
              (click)="filtro.set(f.valor)"
            >
              {{ f.label }}
            </button>
          }
        </div>
      </div>

      <div class="mt-5 space-y-3">
        @for (s of filtradas(); track s.id) {
          <article class="rounded-xl border border-line p-4">
            <div class="flex flex-wrap items-start justify-between gap-3">
              <div class="flex min-w-0 flex-1 gap-3">
                <span class="flex h-10 w-10 shrink-0 items-center justify-center rounded-xl" [class]="meta(s.tipo).iconClasses">
                  <app-icon [name]="meta(s.tipo).icon" [size]="20" />
                </span>
                <div class="min-w-0">
                  <div class="flex flex-wrap items-center gap-2">
                    <h3 class="font-semibold text-ink">{{ s.nombreEmpleado }}</h3>
                    <span class="text-sm text-muted">({{ s.numeroEmpleado }})</span>
                    <app-status-badge [estado]="s.estado" />
                  </div>
                  <p class="text-sm text-muted">{{ meta(s.tipo).label }}</p>
                  <p class="mt-0.5 flex items-center gap-1.5 text-sm text-muted">
                    <app-icon name="calendar" [size]="14" /> {{ rango(s) }}
                  </p>
                </div>
              </div>

              @if (s.estado === 'pendiente') {
                <div class="flex shrink-0 gap-2">
                  <button class="btn-primary !px-3.5 !py-2" (click)="aprobar(s)">
                    <app-icon name="check" [size]="16" /> Aprobar
                  </button>
                  <button class="btn-outline !px-3.5 !py-2" (click)="rechazar(s)">
                    <app-icon name="x" [size]="16" /> Rechazar
                  </button>
                </div>
              }
            </div>

            @if (s.descripcion) {
              <p class="mt-3 rounded-lg bg-slate-50 px-3 py-2 text-sm text-muted">{{ s.descripcion }}</p>
            }
          </article>
        } @empty {
          <p class="py-8 text-center text-sm text-muted">No hay solicitudes en esta categoría.</p>
        }
      </div>
    </section>
  `,
})
export class PanelAprobaciones {
  private readonly store = inject(SolicitudesStore);
  private readonly toast = inject(ToastService);

  protected readonly filtro = signal<Filtro>('todas');
  private readonly solicitudes = this.store.solicitudesEquipo;
  private readonly stats = this.store.statsEquipo;

  protected readonly filtros: { valor: Filtro; label: string }[] = [
    { valor: 'todas', label: 'Todas' },
    { valor: 'pendiente', label: 'Pendientes' },
    { valor: 'aprobado', label: 'Aprobadas' },
    { valor: 'rechazado', label: 'Rechazadas' },
  ];

  constructor() {
    this.store.cargar();
  }

  protected readonly kpis = computed(() => {
    const s = this.stats();
    return [
      { label: 'Total Solicitudes', valor: s.total, icon: 'calendar' as const, iconBg: 'bg-slate-100 text-slate-500', accent: 'border-slate-300' },
      { label: 'Pendientes', valor: s.pendientes, icon: 'calendar' as const, iconBg: 'bg-amber-400 text-white', accent: 'border-amber-400' },
      { label: 'Aprobadas', valor: s.aprobadas, icon: 'check' as const, iconBg: 'bg-emerald-500 text-white', accent: 'border-emerald-500' },
      { label: 'Rechazadas', valor: s.rechazadas, icon: 'x' as const, iconBg: 'bg-brand-dark text-white', accent: 'border-brand-dark' },
    ];
  });

  protected readonly filtradas = computed(() => {
    const f = this.filtro();
    const list = this.solicitudes();
    return f === 'todas' ? list : list.filter((s) => s.estado === f);
  });

  protected meta(tipo: TipoPermiso) {
    return TIPOS_META[tipo];
  }
  protected rango(s: Solicitud) {
    return formatRango(s.fechaInicio, s.fechaFin);
  }

  protected aprobar(s: Solicitud): void {
    this.store.aprobar(s).subscribe({
      next: () => this.toast.success(`Solicitud de ${s.nombreEmpleado} aprobada`),
      error: (e) => this.toast.error(this.errorMsg(e) ?? `No se pudo aprobar la solicitud de ${s.nombreEmpleado}`),
    });
  }
  protected rechazar(s: Solicitud): void {
    this.store.rechazar(s).subscribe({
      next: () => this.toast.show(`Solicitud de ${s.nombreEmpleado} rechazada`, 'error'),
      error: (e) => this.toast.error(this.errorMsg(e) ?? `No se pudo rechazar la solicitud de ${s.nombreEmpleado}`),
    });
  }

  private errorMsg(e: unknown): string | null {
    const err = (e as { error?: unknown })?.error;
    if (typeof err === 'string' && err.trim()) return err;
    if (err && typeof err === 'object' && 'error' in err) return String((err as { error: unknown }).error);
    return null;
  }
}
