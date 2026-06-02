import { ChangeDetectionStrategy, Component, computed, inject, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { Solicitud, TIPOS_META, TipoPermiso } from '../../core/models';
import { SessionService } from '../../core/session.service';
import { SolicitudesStore } from '../../core/solicitudes.store';
import { ToastService } from '../../core/toast.service';
import { diasEntre, formatRango } from '../../core/util';
import { Icon } from '../../shared/icon';
import { StatusBadge } from '../../shared/status-badge';

@Component({
  selector: 'app-portal-empleado',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, Icon, StatusBadge],
  template: `
    <h1 class="mb-6 text-3xl font-bold text-ink">Portal del Empleado</h1>

    <div class="grid grid-cols-1 gap-6 lg:grid-cols-2">
      <!-- ===== Nueva Solicitud ===== -->
      <section class="app-card p-6 sm:p-7">
        <h2 class="text-2xl font-bold text-ink">Nueva Solicitud</h2>

        <p class="mt-5 text-sm text-muted">Tipo de Permiso</p>
        <div class="mt-2 grid grid-cols-2 gap-3">
          @for (t of tipos; track t.tipo) {
            <button
              type="button"
              class="flex flex-col items-center justify-center gap-2 rounded-xl border p-4 transition"
              [class.border-brand]="tipo() === t.tipo"
              [class.bg-brand-50]="tipo() === t.tipo"
              [class.border-line]="tipo() !== t.tipo"
              [class.hover:border-brand]="tipo() !== t.tipo"
              (click)="seleccionar(t.tipo)"
            >
              <span class="flex h-11 w-11 items-center justify-center rounded-xl" [class]="t.iconClasses">
                <app-icon [name]="t.icon" [size]="22" />
              </span>
              <span class="text-sm font-medium text-ink">{{ t.label }}</span>
            </button>
          }
        </div>

        @if (tipo(); as tp) {
          <form class="mt-6" [formGroup]="form" (ngSubmit)="enviar()">
            <div class="grid grid-cols-1 gap-x-4 gap-y-4 sm:grid-cols-2 xl:grid-cols-3">
              <div>
                <label class="field-label">Número de Empleado *</label>
                <input class="field-input" formControlName="numeroEmpleado" placeholder="Buscar" />
              </div>
              <div>
                <label class="field-label">Nombre de Empleado</label>
                <input class="field-input" formControlName="nombreEmpleado" />
              </div>
              <div>
                <label class="field-label">Unidad de Negocio *</label>
                <input class="field-input" formControlName="unidadNegocio" />
              </div>

              <div>
                <label class="field-label">Nombre de Líder</label>
                <input class="field-input" formControlName="lider" placeholder="Buscar" />
              </div>

              @if (tp === 'incapacidad') {
                <div>
                  <label class="field-label">Tipo de Incapacidad *</label>
                  <input class="field-input" formControlName="tipoIncapacidad" />
                </div>
                <div>
                  <label class="field-label">Entidad Salud *</label>
                  <input class="field-input" formControlName="entidadSalud" />
                </div>
                <div>
                  <label class="field-label">Categoría *</label>
                  <input class="field-input" formControlName="categoria" />
                </div>
              }

              <div>
                <label class="field-label">Fecha Inicio *</label>
                <input type="date" class="field-input" formControlName="fechaInicio" />
              </div>
              <div>
                <label class="field-label">Fecha Fin *</label>
                <input type="date" class="field-input" formControlName="fechaFin" />
              </div>
              <div>
                <label class="field-label">Total Días</label>
                <input class="field-input" [value]="totalDias()" disabled />
              </div>
            </div>

            <!-- Diagnóstico (incapacidad) / Comentario (resto) -->
            <div class="mt-4">
              @if (tp === 'incapacidad') {
                <label class="field-label">¿Cuál es tu diagnóstico? *</label>
                <textarea rows="3" class="field-input resize-none" formControlName="diagnostico" placeholder="Indique el código o nombre del diagnóstico..."></textarea>
              } @else {
                <label class="field-label">Comentario</label>
                <textarea rows="3" class="field-input resize-none" formControlName="descripcion" placeholder="Escribe un comentario..."></textarea>
              }
            </div>

            <!-- Documentos adjuntos (incapacidad y calamidad) -->
            @if (tp === 'incapacidad' || tp === 'calamidad') {
              <div class="mt-4">
                <label class="field-label">Documentos Adjuntos</label>
                <label
                  class="flex cursor-pointer flex-col items-center justify-center gap-1 rounded-xl border border-dashed border-line bg-slate-50/50 px-4 py-6 text-center transition hover:border-brand"
                >
                  <span class="text-sm text-muted">{{ archivo() || 'No hay nada adjunto' }}</span>
                  <span class="flex items-center gap-1 text-sm font-medium text-brand">
                    <app-icon name="paperclip" [size]="15" /> Adjuntar un archivo
                  </span>
                  <input type="file" class="hidden" (change)="onArchivo($event)" />
                </label>
              </div>
            }

            <button type="submit" class="btn-primary btn-block mt-6">
              Enviar Solicitud de {{ meta(tp).label }}
            </button>
          </form>
        }
      </section>

      <!-- ===== Mis Solicitudes ===== -->
      <section class="app-card h-fit p-6 sm:p-7">
        <h2 class="text-2xl font-bold text-ink">Mis Solicitudes</h2>
        <div class="mt-5 space-y-3">
          @for (s of misSolicitudes(); track s.id) {
            <article class="rounded-xl border border-line p-4">
              <div class="flex items-start gap-3">
                <span class="flex h-10 w-10 shrink-0 items-center justify-center rounded-xl" [class]="meta(s.tipo).iconClasses">
                  <app-icon [name]="meta(s.tipo).icon" [size]="20" />
                </span>
                <div class="min-w-0 flex-1">
                  <div class="flex items-center justify-between gap-2">
                    <h3 class="font-semibold text-ink">{{ meta(s.tipo).label }}</h3>
                    <app-status-badge [estado]="s.estado" />
                  </div>
                  <p class="text-sm text-muted">{{ rango(s) }}</p>
                </div>
              </div>
              @if (s.descripcion) {
                <p class="mt-2 text-sm text-muted">{{ s.descripcion }}</p>
              }
            </article>
          }
        </div>
      </section>
    </div>
  `,
})
export class PortalEmpleado {
  private readonly fb = inject(FormBuilder);
  private readonly store = inject(SolicitudesStore);
  private readonly session = inject(SessionService);
  private readonly toast = inject(ToastService);

  protected readonly tipos = Object.values(TIPOS_META);
  protected readonly misSolicitudes = this.store.misSolicitudes;
  protected readonly tipo = signal<TipoPermiso | null>(null);
  protected readonly archivo = signal<string>('');

  protected readonly form = this.fb.nonNullable.group({
    numeroEmpleado: '',
    nombreEmpleado: this.session.usuario()?.nombre ?? '',
    unidadNegocio: '',
    lider: '',
    tipoIncapacidad: '',
    entidadSalud: '',
    categoria: '',
    fechaInicio: '',
    fechaFin: '',
    diagnostico: '',
    descripcion: '',
  });

  private readonly valores = toSignal(this.form.valueChanges, { initialValue: this.form.getRawValue() });
  protected readonly totalDias = computed(() => diasEntre(this.valores().fechaInicio ?? '', this.valores().fechaFin ?? ''));

  constructor() {
    this.store.cargar();
  }

  protected meta(tipo: TipoPermiso) {
    return TIPOS_META[tipo];
  }
  protected rango(s: Solicitud) {
    return formatRango(s.fechaInicio, s.fechaFin);
  }

  protected seleccionar(tipo: TipoPermiso): void {
    this.tipo.set(tipo);
  }

  protected onArchivo(e: Event): void {
    const input = e.target as HTMLInputElement;
    this.archivo.set(input.files?.[0]?.name ?? '');
  }

  protected enviar(): void {
    const tipo = this.tipo();
    if (!tipo) return;
    const v = this.form.getRawValue();
    this.store
      .crearSolicitud(tipo, {
        numeroEmpleado: v.numeroEmpleado || this.session.usuario()?.numeroEmpleado || 'EMP000',
        nombreEmpleado: v.nombreEmpleado,
        unidadNegocio: v.unidadNegocio,
        fechaInicio: v.fechaInicio,
        fechaFin: v.fechaFin,
        totalDias: this.totalDias(),
        descripcion: v.descripcion,
        tipoIncapacidad: v.tipoIncapacidad,
        entidadSalud: v.entidadSalud,
        categoria: v.categoria,
        diagnostico: v.diagnostico,
        archivoAdjunto: this.archivo(),
      })
      .subscribe({
        next: () => {
          this.toast.success(`Solicitud de ${TIPOS_META[tipo].label} enviada`);
          this.form.reset({ nombreEmpleado: this.session.usuario()?.nombre ?? '' });
          this.archivo.set('');
          this.tipo.set(null);
        },
        error: () => this.toast.error('No se pudo enviar la solicitud'),
      });
  }
}
