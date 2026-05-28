import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class DisabilitiesRequestService {

  constructor(private http: HttpClient) { }

  createDisabilitiesRequest(payload: any): Observable<any> {
    const body = {
      numeroEmpleado: payload.employeeNumber,
      nombreEmpleado: payload.employeeName,
      unidadNegocio: payload.businessUnit,
      tipoIncapacidad: payload.incapacityType || null,
      entidadSalud: payload.healthEntity || null,
      categoria: payload.category || null,
      fechaInicio: payload.startDate || null,
      fechaFin: payload.endDate || null,
      totalDias: payload.totalDays || null,
      diagnostico: payload.diagnosis || null,
      archivoAdjunto: payload.fileName || null,
      lider: null,
      fechaCreacion: new Date().toISOString()
    };
    return this.http.post<any>(`${environment.apiUrl}${environment.endpoint.createDisability}`, body).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    const errorMsg = error.error?.message || 'Error de conexión con el servidor backend';
    return throwError(() => errorMsg);
  }
}
