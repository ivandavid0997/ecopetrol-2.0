export function formatDate(value: any): string {
  if (!value) return '';
  const d = new Date(value);
  if (isNaN(d.getTime())) return String(value);
  const day = String(d.getDate()).padStart(2, '0');
  const month = String(d.getMonth() + 1).padStart(2, '0');
  const year = d.getFullYear();
  return `${day}/${month}/${year}`;
}

export function mapStatus(estado: string): string {
  switch ((estado || '').toUpperCase()) {
    case 'APROBADA': return 'Aprobado';
    case 'RECHAZADA': return 'Rechazado';
    case 'PENDIENTE': return 'Pendiente';
    default: return estado || '';
  }
}

export function toBackendPayload(form: any): any {
  return {
    numeroEmpleado: form.employeeNumber,
    nombreEmpleado: form.employeeName,
    unidadNegocio: form.businessUnit,
    fechaInicio: form.startDate || null,
    fechaFin: form.endDate || null,
    totalDias: form.totalDays || form.totalDias || null,
    comentario: form.comments || form.comentario || null,
    lider: null
  };
}
