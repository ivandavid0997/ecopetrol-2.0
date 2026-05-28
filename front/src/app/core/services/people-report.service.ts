import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PeopleReportService {

  constructor(private http: HttpClient) {}

  /**
   * Solicita la descarga del reporte global de solicitudes (vacaciones, calamidad, cumpleaños, incapacidad)
   * Retorna el Blob para descargarlo como archivo (CSV, XLSX o PDF según backend)
   */
  /**
   * Solicita la descarga del reporte global en JSON, lo convierte a CSV y retorna el Blob listo para descargar.
   */
  downloadPeopleReportCSV(): Observable<Blob> {
    const url = `${environment.apiUrl}${environment.endpoint.peopleReport}`;
    return this.http.get<any[]>(url).pipe(
      map((data: any[]) => {
        if (!data || !Array.isArray(data)) {
          throw new Error('Datos de reporte inválidos');
        }
        const columns = [
          'id',
          'numeroEmpleado',
          'nombreEmpleado',
          'tipoSolicitud',
          'descripcion',
          'estado',
          'unidadNegocio',
          'fechaCreacion',
          'fechaInicio',
          'fechaFin'
        ];
        const header = columns.join(',');
        const rows = data.map(item =>
          columns.map(col => {
            // Escapar comillas dobles y separar por comas
            const val = item[col] == null ? '' : String(item[col]).replace(/"/g, '""');
            return `"${val}"`;
          }).join(',')
        );
        const csv = [header, ...rows].join('\r\n');
        return new Blob([csv], { type: 'text/csv' });
      }),
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    const errorMsg = error.error?.message || 'Error de conexión con el backend al descargar el reporte';
    return throwError(() => errorMsg);
  }
}
