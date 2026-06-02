import { Injectable, computed, signal } from '@angular/core';
import { EstadoSolicitud, ReporteEmpleado, Solicitud } from './models';
import { MIS_SOLICITUDES, REPORTE_EMPLEADOS, SOLICITUDES_EQUIPO } from './mock-data';

/** Store central (mock, en memoria) de solicitudes. */
@Injectable({ providedIn: 'root' })
export class SolicitudesStore {
  private seq = 100;

  private readonly _mias = signal<Solicitud[]>(structuredClone(MIS_SOLICITUDES));
  private readonly _equipo = signal<Solicitud[]>(structuredClone(SOLICITUDES_EQUIPO));
  private readonly _reporte = signal<ReporteEmpleado[]>(structuredClone(REPORTE_EMPLEADOS));

  readonly misSolicitudes = this._mias.asReadonly();
  readonly solicitudesEquipo = this._equipo.asReadonly();
  readonly reporte = this._reporte.asReadonly();

  /** KPIs del Panel de Aprobaciones (líder) */
  readonly statsEquipo = computed(() => {
    const list = this._equipo();
    return {
      total: list.length,
      pendientes: list.filter((s) => s.estado === 'pendiente').length,
      aprobadas: list.filter((s) => s.estado === 'aprobado').length,
      rechazadas: list.filter((s) => s.estado === 'rechazado').length,
    };
  });

  /** KPIs de Reportes Mensuales (People) */
  readonly statsReporte = computed(() => {
    const list = this._reporte();
    return {
      totalDias: list.reduce((a, r) => a + r.vacaciones + r.cumpleanos + r.incapacidad + r.calamidad, 0),
      diasVacaciones: list.reduce((a, r) => a + r.vacaciones, 0),
      aprobadas: list.reduce((a, r) => a + r.aprobadas, 0),
    };
  });

  crearSolicitud(parcial: Omit<Solicitud, 'id' | 'estado'>): Solicitud {
    const nueva: Solicitud = { ...parcial, id: `new-${++this.seq}`, estado: 'pendiente' };
    this._mias.update((list) => [nueva, ...list]);
    return nueva;
  }

  aprobar(id: string): void {
    this.setEstado(id, 'aprobado');
  }

  rechazar(id: string): void {
    this.setEstado(id, 'rechazado');
  }

  private setEstado(id: string, estado: EstadoSolicitud): void {
    this._equipo.update((list) => list.map((s) => (s.id === id ? { ...s, estado } : s)));
  }
}
