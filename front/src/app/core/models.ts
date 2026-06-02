/** Modelos de dominio — Portal de Solicitudes Ecopetrol */

export type Rol = 'empleado' | 'lider' | 'people';

export type TipoPermiso = 'vacaciones' | 'incapacidad' | 'calamidad' | 'cumpleanos';

export type EstadoSolicitud = 'pendiente' | 'aprobado' | 'rechazado';

export interface Usuario {
  id: string;
  nombre: string;
  correo: string;
  iniciales: string;
  numeroEmpleado?: string;
}

export interface Solicitud {
  id: string;
  tipo: TipoPermiso;
  /** Empleado solicitante */
  numeroEmpleado: string;
  nombreEmpleado: string;
  departamento?: string;
  unidadNegocio?: string;
  lider?: string;
  fechaInicio: string; // ISO yyyy-mm-dd
  fechaFin: string; // ISO yyyy-mm-dd
  totalDias: number;
  descripcion?: string;
  estado: EstadoSolicitud;
  // Campos específicos
  tipoIncapacidad?: string;
  entidadSalud?: string;
  categoria?: string;
  diagnostico?: string;
  archivoAdjunto?: string;
}

/** Fila de reporte mensual por empleado (Gestión de People) */
export interface ReporteEmpleado {
  id: string;
  nombre: string;
  departamento: string;
  vacaciones: number;
  cumpleanos: number;
  incapacidad: number;
  calamidad: number;
  total: number;
  aprobadas: number;
  rechazadas: number;
}

/** Metadatos visuales por tipo de permiso (label, ícono, color) */
export interface TipoMeta {
  tipo: TipoPermiso;
  label: string;
  /** clave del ícono SVG en TypeIcon */
  icon: 'plane' | 'health' | 'warning' | 'gift';
  /** clases tailwind para el cuadro del ícono */
  iconClasses: string;
}

export const TIPOS_META: Record<TipoPermiso, TipoMeta> = {
  vacaciones: {
    tipo: 'vacaciones',
    label: 'Vacaciones',
    icon: 'plane',
    iconClasses: 'bg-brand-light text-white',
  },
  incapacidad: {
    tipo: 'incapacidad',
    label: 'Incapacidad',
    icon: 'health',
    iconClasses: 'bg-eco-greenbright text-white',
  },
  calamidad: {
    tipo: 'calamidad',
    label: 'Calamidad',
    icon: 'warning',
    iconClasses: 'bg-eco-green text-white',
  },
  cumpleanos: {
    tipo: 'cumpleanos',
    label: 'Día de Cumpleaños',
    icon: 'gift',
    iconClasses: 'bg-brand-dark text-white',
  },
};

export const ESTADO_LABEL: Record<EstadoSolicitud, string> = {
  pendiente: 'Pendiente',
  aprobado: 'Aprobado',
  rechazado: 'Rechazado',
};

export const ROLES_META: { rol: Rol; titulo: string; descripcion: string; icon: 'user' | 'briefcase' | 'people'; iconClasses: string }[] = [
  { rol: 'empleado', titulo: 'Empleado', descripcion: 'Solicita y gestiona tus permisos', icon: 'user', iconClasses: 'bg-brand-light' },
  { rol: 'lider', titulo: 'Líder de Equipo', descripcion: 'Aprueba o rechaza solicitudes', icon: 'briefcase', iconClasses: 'bg-eco-greenbright' },
  { rol: 'people', titulo: 'Gestión de People', descripcion: 'Consulta reportes y estadísticas', icon: 'people', iconClasses: 'bg-eco-green' },
];
