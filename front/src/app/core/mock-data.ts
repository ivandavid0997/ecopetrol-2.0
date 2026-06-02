import { ReporteEmpleado, Solicitud, Usuario } from './models';

export const USUARIO_DEMO: Usuario = {
  id: 'u-juan',
  nombre: 'Juan Perez',
  correo: 'Juan.perez@ecopetrol.com.co',
  iniciales: 'JP',
};

/** "Mis Solicitudes" del empleado (pantalla Portal del Empleado) */
export const MIS_SOLICITUDES: Solicitud[] = [
  {
    id: 'ms-1',
    tipo: 'vacaciones',
    numeroEmpleado: 'EMP000',
    nombreEmpleado: 'Juan Perez',
    fechaInicio: '2026-06-14',
    fechaFin: '2026-06-19',
    totalDias: 5,
    descripcion: 'Vacaciones familiares',
    estado: 'aprobado',
  },
  {
    id: 'ms-2',
    tipo: 'cumpleanos',
    numeroEmpleado: 'EMP000',
    nombreEmpleado: 'Juan Perez',
    fechaInicio: '2026-07-09',
    fechaFin: '2026-07-09',
    totalDias: 1,
    descripcion: 'Celebración de cumpleaños',
    estado: 'pendiente',
  },
  {
    id: 'ms-3',
    tipo: 'incapacidad',
    numeroEmpleado: 'EMP000',
    nombreEmpleado: 'Juan Perez',
    fechaInicio: '2026-05-14',
    fechaFin: '2026-05-17',
    totalDias: 3,
    descripcion: 'Incapacidad médica - gripe',
    estado: 'aprobado',
  },
];

/** Solicitudes del equipo (Panel de Aprobaciones del líder) */
export const SOLICITUDES_EQUIPO: Solicitud[] = [
  {
    id: 'eq-1',
    tipo: 'vacaciones',
    numeroEmpleado: 'EMP001',
    nombreEmpleado: 'María García',
    departamento: 'IT',
    fechaInicio: '2026-06-19',
    fechaFin: '2026-06-26',
    totalDias: 8,
    descripcion: 'Vacaciones con familia',
    estado: 'pendiente',
  },
  {
    id: 'eq-2',
    tipo: 'cumpleanos',
    numeroEmpleado: 'EMP002',
    nombreEmpleado: 'Carlos Ruiz',
    departamento: 'Recursos Humanos',
    fechaInicio: '2026-06-14',
    fechaFin: '2026-06-14',
    totalDias: 1,
    descripcion: 'Celebración de cumpleaños',
    estado: 'pendiente',
  },
  {
    id: 'eq-3',
    tipo: 'incapacidad',
    numeroEmpleado: 'EMP003',
    nombreEmpleado: 'Ana Martínez',
    departamento: 'Finanzas',
    fechaInicio: '2026-06-17',
    fechaFin: '2026-06-21',
    totalDias: 5,
    descripcion: 'Incapacidad médica - recuperación post-operatoria',
    estado: 'pendiente',
  },
  {
    id: 'eq-4',
    tipo: 'vacaciones',
    numeroEmpleado: 'EMP004',
    nombreEmpleado: 'Pedro López',
    departamento: 'IT',
    fechaInicio: '2026-06-30',
    fechaFin: '2026-07-07',
    totalDias: 8,
    descripcion: 'Viaje familiar programado',
    estado: 'aprobado',
  },
  {
    id: 'eq-5',
    tipo: 'calamidad',
    numeroEmpleado: 'EMP005',
    nombreEmpleado: 'Laura Hernández',
    departamento: 'Marketing',
    fechaInicio: '2026-06-09',
    fechaFin: '2026-06-11',
    totalDias: 3,
    descripcion: 'Calamidad doméstica - situación familiar urgente',
    estado: 'aprobado',
  },
];

/** Reporte mensual por empleado (Gestión de People) */
export const REPORTE_EMPLEADOS: ReporteEmpleado[] = [
  { id: 'EMP001', nombre: 'María García', departamento: 'IT', vacaciones: 5, cumpleanos: 1, incapacidad: 0, calamidad: 0, total: 6, aprobadas: 2, rechazadas: 0 },
  { id: 'EMP002', nombre: 'Carlos Ruiz', departamento: 'Recursos Humanos', vacaciones: 3, cumpleanos: 1, incapacidad: 2, calamidad: 0, total: 6, aprobadas: 3, rechazadas: 0 },
  { id: 'EMP003', nombre: 'Ana Martínez', departamento: 'Finanzas', vacaciones: 0, cumpleanos: 0, incapacidad: 5, calamidad: 0, total: 5, aprobadas: 1, rechazadas: 0 },
  { id: 'EMP004', nombre: 'Pedro López', departamento: 'IT', vacaciones: 7, cumpleanos: 1, incapacidad: 0, calamidad: 0, total: 8, aprobadas: 2, rechazadas: 0 },
  { id: 'EMP005', nombre: 'Laura Hernández', departamento: 'Marketing', vacaciones: 4, cumpleanos: 0, incapacidad: 0, calamidad: 3, total: 7, aprobadas: 2, rechazadas: 1 },
];
