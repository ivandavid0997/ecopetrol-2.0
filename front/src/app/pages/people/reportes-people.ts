import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { SolicitudesStore } from '../../core/solicitudes.store';
import { ToastService } from '../../core/toast.service';
import { Icon } from '../../shared/icon';

@Component({
  selector: 'app-reportes-people',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [Icon],
  template: `
    <div class="mb-6 flex flex-wrap items-center justify-between gap-4">
      <h1 class="text-3xl font-bold text-ink">Reportes Mensuales</h1>
      <div class="flex flex-wrap gap-3">
        <button class="btn-primary" (click)="descargar()">
          <app-icon name="download" [size]="18" /> Descargar Reporte
        </button>
        <button class="btn-outline" (click)="cierreMes()">
          <app-icon name="calendar" [size]="18" /> Cierre de mes
        </button>
      </div>
    </div>

    <!-- KPIs -->
    <div class="grid grid-cols-1 gap-4 sm:grid-cols-3">
      @for (k of kpis(); track k.label) {
        <div class="app-card flex items-center justify-between border-l-4 p-5" [class]="k.accent">
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

    <!-- Detalle -->
    <section class="app-card mt-6 p-6 sm:p-7">
      <div class="flex flex-wrap items-center justify-between gap-3">
        <h2 class="text-xl font-bold text-ink">Detalle por Empleado</h2>
        <div class="flex flex-wrap items-center gap-2">
          <div class="relative">
            <span class="pointer-events-none absolute left-3 top-1/2 -translate-y-1/2 text-slate-400">
              <app-icon name="calendar" [size]="16" />
            </span>
            <input type="month" class="field-input !py-2 pl-9" />
          </div>
          <div class="relative">
            <span class="pointer-events-none absolute left-3 top-1/2 -translate-y-1/2 text-slate-400">
              <app-icon name="filter" [size]="16" />
            </span>
            <input class="field-input !py-2 pl-9" placeholder="Filtrar empleado…" [value]="query()" (input)="onQuery($event)" />
          </div>
        </div>
      </div>

      <div class="mt-5 overflow-x-auto">
        <table class="w-full min-w-[820px] text-sm">
          <thead>
            <tr class="border-b border-line text-left text-xs font-semibold uppercase tracking-wide text-muted">
              <th class="px-3 py-3">ID</th>
              <th class="px-3 py-3">Empleado</th>
              <th class="px-3 py-3">Departamento</th>
              <th class="px-3 py-3 text-center">Vacaciones</th>
              <th class="px-3 py-3 text-center">Cumpleaños</th>
              <th class="px-3 py-3 text-center">Incapacidad</th>
              <th class="px-3 py-3 text-center">Calamidad</th>
              <th class="px-3 py-3 text-center">Total</th>
              <th class="px-3 py-3 text-center">Aprobadas</th>
              <th class="px-3 py-3 text-center">Rechazadas</th>
            </tr>
          </thead>
          <tbody>
            @for (r of filtradas(); track r.id) {
              <tr class="border-b border-line/70 last:border-0 hover:bg-slate-50/60">
                <td class="px-3 py-3.5 font-semibold text-brand-dark">{{ r.id }}</td>
                <td class="px-3 py-3.5 font-medium text-ink">{{ r.nombre }}</td>
                <td class="px-3 py-3.5 text-muted">{{ r.departamento }}</td>
                <td class="px-3 py-3.5 text-center text-ink">{{ r.vacaciones }}</td>
                <td class="px-3 py-3.5 text-center text-ink">{{ r.cumpleanos }}</td>
                <td class="px-3 py-3.5 text-center text-ink">{{ r.incapacidad }}</td>
                <td class="px-3 py-3.5 text-center text-ink">{{ r.calamidad }}</td>
                <td class="px-3 py-3.5 text-center font-bold text-ink">{{ r.total }}</td>
                <td class="px-3 py-3.5 text-center">
                  <span class="inline-flex min-w-7 justify-center rounded-full bg-emerald-100 px-2 py-0.5 text-xs font-semibold text-emerald-700">{{ r.aprobadas }}</span>
                </td>
                <td class="px-3 py-3.5 text-center">
                  <span class="inline-flex min-w-7 justify-center rounded-full bg-red-100 px-2 py-0.5 text-xs font-semibold text-red-700">{{ r.rechazadas }}</span>
                </td>
              </tr>
            } @empty {
              <tr>
                <td colspan="10" class="py-8 text-center text-sm text-muted">Sin resultados.</td>
              </tr>
            }
          </tbody>
        </table>
      </div>
    </section>
  `,
})
export class ReportesPeople {
  private readonly store = inject(SolicitudesStore);
  private readonly toast = inject(ToastService);

  private readonly reporte = this.store.reporte;
  private readonly stats = this.store.statsReporte;
  protected readonly query = signal('');

  constructor() {
    this.store.cargar();
  }

  protected readonly kpis = computed(() => {
    const s = this.stats();
    return [
      { label: 'Total Días de Permiso', valor: s.totalDias, icon: 'calendar' as const, iconBg: 'bg-brand-light text-white', accent: 'border-brand' },
      { label: 'Días de Vacaciones', valor: s.diasVacaciones, icon: 'calendar' as const, iconBg: 'bg-emerald-500 text-white', accent: 'border-emerald-500' },
      { label: 'Solicitudes Aprobadas', valor: s.aprobadas, icon: 'file' as const, iconBg: 'bg-eco-green text-white', accent: 'border-eco-green' },
    ];
  });

  protected readonly filtradas = computed(() => {
    const q = this.query().trim().toLowerCase();
    const list = this.reporte();
    if (!q) return list;
    return list.filter((r) => r.nombre.toLowerCase().includes(q) || r.departamento.toLowerCase().includes(q) || r.id.toLowerCase().includes(q));
  });

  protected onQuery(e: Event): void {
    this.query.set((e.target as HTMLInputElement).value);
  }

  protected descargar(): void {
    const rows = this.reporte();
    const header = ['ID', 'Empleado', 'Departamento', 'Vacaciones', 'Cumpleanos', 'Incapacidad', 'Calamidad', 'Total', 'Aprobadas', 'Rechazadas'];
    const csv = [
      header.join(','),
      ...rows.map((r) => [r.id, r.nombre, r.departamento, r.vacaciones, r.cumpleanos, r.incapacidad, r.calamidad, r.total, r.aprobadas, r.rechazadas].join(',')),
    ].join('\n');
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = 'reporte-mensual.csv';
    a.click();
    URL.revokeObjectURL(url);
    this.toast.success('Reporte descargado');
  }

  protected cierreMes(): void {
    this.store.cierreMes().subscribe({
      next: () => this.toast.success('Cierre de mes ejecutado correctamente'),
      error: () => this.toast.error('No se pudo ejecutar el cierre de mes'),
    });
  }
}
