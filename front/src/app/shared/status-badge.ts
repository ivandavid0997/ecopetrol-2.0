import { ChangeDetectionStrategy, Component, computed, input } from '@angular/core';
import { ESTADO_LABEL, EstadoSolicitud } from '../core/models';

/** Badge de estado: Aprobado / Pendiente / Rechazado */
@Component({
  selector: 'app-status-badge',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `<span [class]="cls()">{{ label() }}</span>`,
})
export class StatusBadge {
  readonly estado = input.required<EstadoSolicitud>();
  protected readonly label = computed(() => ESTADO_LABEL[this.estado()]);
  protected readonly cls = computed(() => {
    switch (this.estado()) {
      case 'aprobado':
        return 'badge-aprobado';
      case 'rechazado':
        return 'badge-rechazado';
      default:
        return 'badge-pendiente';
    }
  });
}
