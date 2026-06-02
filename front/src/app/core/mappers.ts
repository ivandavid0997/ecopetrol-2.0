import { EstadoSolicitud, Rol, Solicitud, TipoPermiso } from './models';

/** Respuesta de /autenticacion/login */
export interface LoginResponse {
  token: string;
  usuario: string;
  rol: string; // EMPLEADO | LIDER | PEOPLE
}

/** /usuario/listaUsuarios */
export interface UsuarioApi {
  id: number;
  nombre: string;
  usuario: string;
  rol: string;
  numeroEmpleado?: number;
  unidadNegocio?: string;
}

export function rolFromBackend(rol: string): Rol {
  switch ((rol || '').toUpperCase()) {
    case 'LIDER':
      return 'lider';
    case 'PEOPLE':
      return 'people';
    default:
      return 'empleado';
  }
}

export function estadoFromBackend(estado: string): EstadoSolicitud {
  switch ((estado || '').toUpperCase()) {
    case 'APROBADA':
      return 'aprobado';
    case 'RECHAZADA':
    case 'CANCELADA':
      return 'rechazado';
    default:
      return 'pendiente';
  }
}

/** ISO ("2026-03-03T05:00:00.000Z") o yyyy-mm-dd -> yyyy-mm-dd */
export function isoToFecha(v: string | number | null | undefined): string {
  if (v == null) return '';
  const s = String(v);
  if (/^\d{4}-\d{2}-\d{2}/.test(s)) return s.slice(0, 10);
  const d = new Date(v);
  return isNaN(d.getTime()) ? '' : d.toISOString().slice(0, 10);
}

/** yyyy-mm-dd (input date) -> ISO datetime para el backend (java.util.Date) */
export function fechaToIso(v: string): string {
  if (!v) return '';
  const d = new Date(v + 'T00:00:00.000Z');
  return isNaN(d.getTime()) ? '' : d.toISOString();
}

/** Mapea un item de listado del backend (según el tipo) a Solicitud del front. */
export function solicitudFromApi(tipo: TipoPermiso, raw: any): Solicitud {
  const base = {
    id: raw.id != null ? String(raw.id) : `${tipo}-${raw.numeroEmpleado}-${raw.fechaCreacion ?? ''}`,
    tipo,
    numeroEmpleado: raw.numeroEmpleado ?? '',
    nombreEmpleado: raw.nombreEmpleado ?? '',
    unidadNegocio: raw.unidadNegocio,
    departamento: raw.unidadNegocio,
    lider: raw.liderNombre,
    totalDias: raw.totalDias ?? 0,
    estado: estadoFromBackend(raw.estado),
  };
  switch (tipo) {
    case 'cumpleanos':
      return {
        ...base,
        fechaInicio: isoToFecha(raw.fechaCumpleanio),
        fechaFin: isoToFecha(raw.fechaCumpleanio),
        descripcion: raw.comentario,
      };
    case 'calamidad':
      return {
        ...base,
        fechaInicio: isoToFecha(raw.fechaInicio ?? raw.fechaEvento),
        fechaFin: isoToFecha(raw.fechaFin ?? raw.fechaEvento),
        descripcion: raw.descripcion ?? raw.motivo ?? raw.comentario,
      };
    default:
      return {
        ...base,
        fechaInicio: isoToFecha(raw.fechaInicio),
        fechaFin: isoToFecha(raw.fechaFin),
        descripcion: raw.comentario ?? raw.diagnostico,
      };
  }
}

/** Construye el payload de creación (RqDTO) según el tipo. */
export function crearPayload(
  tipo: TipoPermiso,
  f: {
    numeroEmpleado: string;
    nombreEmpleado: string;
    unidadNegocio: string;
    fechaInicio: string;
    fechaFin: string;
    totalDias: number;
    descripcion?: string;
    tipoIncapacidad?: string;
    entidadSalud?: string;
    categoria?: string;
    diagnostico?: string;
    archivoAdjunto?: string;
  },
): any {
  const ini = fechaToIso(f.fechaInicio);
  const fin = fechaToIso(f.fechaFin);
  switch (tipo) {
    case 'vacaciones':
      return {
        numeroEmpleado: f.numeroEmpleado,
        nombreEmpleado: f.nombreEmpleado,
        unidadNegocio: f.unidadNegocio,
        fechaInicio: ini,
        fechaFin: fin,
        totalDias: f.totalDias,
        estado: 'PENDIENTE',
        comentario: f.descripcion,
      };
    case 'incapacidad':
      return {
        numeroEmpleado: f.numeroEmpleado,
        nombreEmpleado: f.nombreEmpleado,
        unidadNegocio: f.unidadNegocio,
        tipoIncapacidad: f.tipoIncapacidad,
        entidadSalud: f.entidadSalud,
        categoria: f.categoria,
        fechaInicio: ini,
        fechaFin: fin,
        totalDias: f.totalDias,
        diagnostico: f.diagnostico,
        estado: 'PENDIENTE',
        archivoAdjunto: f.archivoAdjunto,
      };
    case 'calamidad':
      return {
        numeroEmpleado: f.numeroEmpleado,
        nombreEmpleado: f.nombreEmpleado,
        unidadNegocio: f.unidadNegocio,
        fechaInicio: ini,
        fechaFin: fin,
        totalDias: f.totalDias,
        descripcion: f.descripcion,
        comentario: f.descripcion,
        estado: 'PENDIENTE',
        archivoAdjunto: f.archivoAdjunto,
      };
    case 'cumpleanos':
      return {
        numeroEmpleado: f.numeroEmpleado,
        nombreEmpleado: f.nombreEmpleado,
        unidadNegocio: f.unidadNegocio,
        fechaCumpleanio: ini,
        comentario: f.descripcion,
        estado: 'PENDIENTE',
      };
  }
}
