/** Utilidades compartidas */

/** Formatea una fecha ISO (yyyy-mm-dd) a d/m/yyyy como en el diseño. */
export function formatFecha(iso: string): string {
  if (!iso) return '';
  const [y, m, d] = iso.split('-').map((n) => parseInt(n, 10));
  if (!y || !m || !d) return iso;
  return `${d}/${m}/${y}`;
}

/** Rango "d/m/yyyy - d/m/yyyy" */
export function formatRango(inicio: string, fin: string): string {
  return `${formatFecha(inicio)} - ${formatFecha(fin)}`;
}

/** Diferencia en días (inclusive) entre dos fechas ISO. */
export function diasEntre(inicio: string, fin: string): number {
  if (!inicio || !fin) return 0;
  const a = new Date(inicio).getTime();
  const b = new Date(fin).getTime();
  if (isNaN(a) || isNaN(b)) return 0;
  return Math.max(0, Math.round((b - a) / 86400000) + 1);
}
