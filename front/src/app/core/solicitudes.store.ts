import { Injectable, computed, inject, signal } from '@angular/core';
import { Observable, forkJoin, of } from 'rxjs';
import { catchError, finalize, map, tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { TalentoApi } from './api.service';
import { crearPayload, solicitudFromApi } from './mappers';
import { EstadoSolicitud, ReporteEmpleado, Solicitud, TipoPermiso } from './models';
import { MIS_SOLICITUDES, REPORTE_EMPLEADOS, SOLICITUDES_EQUIPO } from './mock-data';
import { SessionService } from './session.service';

const TIPOS: TipoPermiso[] = ['vacaciones', 'incapacidad', 'calamidad', 'cumpleanos'];

@Injectable({ providedIn: 'root' })
export class SolicitudesStore {
  private readonly api = inject(TalentoApi);
  private readonly session = inject(SessionService);

  private readonly _mias = signal<Solicitud[]>([]);
  private readonly _equipo = signal<Solicitud[]>([]);
  private readonly _reporte = signal<ReporteEmpleado[]>([]);
  private readonly _cargando = signal(false);
  private cargado = false;

  readonly misSolicitudes = this._mias.asReadonly();
  readonly solicitudesEquipo = this._equipo.asReadonly();
  readonly reporte = this._reporte.asReadonly();
  readonly cargando = this._cargando.asReadonly();

  readonly statsEquipo = computed(() => {
    const list = this._equipo();
    return {
      total: list.length,
      pendientes: list.filter((s) => s.estado === 'pendiente').length,
      aprobadas: list.filter((s) => s.estado === 'aprobado').length,
      rechazadas: list.filter((s) => s.estado === 'rechazado').length,
    };
  });

  readonly statsReporte = computed(() => {
    const list = this._reporte();
    return {
      totalDias: list.reduce((a, r) => a + r.vacaciones + r.cumpleanos + r.incapacidad + r.calamidad, 0),
      diasVacaciones: list.reduce((a, r) => a + r.vacaciones, 0),
      aprobadas: list.reduce((a, r) => a + r.aprobadas, 0),
    };
  });

  /** Carga inicial (idempotente). */
  cargar(forzar = false): void {
    if (this.cargado && !forzar) return;
    this.cargado = true;

    if (environment.useMock) {
      this._mias.set(structuredClone(MIS_SOLICITUDES));
      this._equipo.set(structuredClone(SOLICITUDES_EQUIPO));
      this._reporte.set(structuredClone(REPORTE_EMPLEADOS));
      return;
    }

    this._cargando.set(true);
    forkJoin(
      TIPOS.map((t) => this.api.listar(t).pipe(catchError(() => of<any[]>([])), map((arr) => arr.map((x) => solicitudFromApi(t, x))))),
    )
      .pipe(finalize(() => this._cargando.set(false)))
      .subscribe((porTipo) => {
        const all = porTipo.flat();
        this._equipo.set(all);
        const num = this.session.usuario()?.numeroEmpleado;
        this._mias.set(num ? all.filter((s) => s.numeroEmpleado === num) : all);
        this._reporte.set(agregarReporte(all));
      });
  }

  crearSolicitud(tipo: TipoPermiso, form: Parameters<typeof crearPayload>[1]): Observable<unknown> {
    if (environment.useMock) {
      const nueva: Solicitud = {
        id: `new-${Date.now()}`,
        tipo,
        numeroEmpleado: form.numeroEmpleado || 'EMP000',
        nombreEmpleado: form.nombreEmpleado,
        unidadNegocio: form.unidadNegocio,
        fechaInicio: form.fechaInicio,
        fechaFin: form.fechaFin,
        totalDias: form.totalDias,
        descripcion: form.descripcion,
        estado: 'pendiente',
      };
      this._mias.update((l) => [nueva, ...l]);
      return of(nueva);
    }
    return this.api.crear(tipo, crearPayload(tipo, form)).pipe(tap(() => this.cargar(true)));
  }

  aprobar(s: Solicitud): Observable<unknown> {
    return this.cambiarEstado(s, 'aprobado', () => this.api.aprobar(s.tipo, s.id));
  }

  rechazar(s: Solicitud): Observable<unknown> {
    return this.cambiarEstado(s, 'rechazado', () => this.api.rechazar(s.tipo, s.id));
  }

  private cambiarEstado(s: Solicitud, estado: EstadoSolicitud, call: () => Observable<unknown>): Observable<unknown> {
    if (environment.useMock) {
      this._equipo.update((l) => l.map((x) => (x.id === s.id ? { ...x, estado } : x)));
      return of(true);
    }
    return call().pipe(tap(() => this._equipo.update((l) => l.map((x) => (x.id === s.id ? { ...x, estado } : x)))));
  }

  cierreMes(): Observable<unknown> {
    return environment.useMock ? of(true) : this.api.cierreMes();
  }
}

/** Agrega solicitudes por empleado para el reporte de People. */
function agregarReporte(all: Solicitud[]): ReporteEmpleado[] {
  const map = new Map<string, ReporteEmpleado>();
  for (const s of all) {
    const key = s.numeroEmpleado || s.nombreEmpleado;
    let r = map.get(key);
    if (!r) {
      r = { id: s.numeroEmpleado || key, nombre: s.nombreEmpleado, departamento: s.departamento ?? s.unidadNegocio ?? '', vacaciones: 0, cumpleanos: 0, incapacidad: 0, calamidad: 0, total: 0, aprobadas: 0, rechazadas: 0 };
      map.set(key, r);
    }
    if (s.tipo === 'vacaciones') r.vacaciones++;
    else if (s.tipo === 'cumpleanos') r.cumpleanos++;
    else if (s.tipo === 'incapacidad') r.incapacidad++;
    else if (s.tipo === 'calamidad') r.calamidad++;
    r.total++;
    if (s.estado === 'aprobado') r.aprobadas++;
    else if (s.estado === 'rechazado') r.rechazadas++;
  }
  return [...map.values()].sort((a, b) => a.id.localeCompare(b.id));
}
