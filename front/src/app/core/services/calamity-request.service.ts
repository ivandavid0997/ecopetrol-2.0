import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CalamityRequestService {

  constructor(private http: HttpClient) { }

  createCalamityRequest(payload: any): Observable<any> {
    const body = {
      numeroEmpleado: payload.employeeNumber,
      nombreEmpleado: payload.employeeName,
      unidadNegocio: payload.businessUnit,
      fechaInicio: payload.startDate || null,
      fechaFin: payload.endDate || null,
      totalDias: payload.totalDays || null,
      comentario: payload.comments || null,
      descripcion: payload.comments || null,
      archivoAdjunto: payload.fileName || null,
      lider: null,
      fechaCreacion: new Date().toISOString()
    };
    return this.http.post<any>(`${environment.apiUrl}${environment.endpoint.createCalamity}`, body).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    const errorMsg = error.error?.message || 'Error de conexión con el servidor backend';
    return throwError(() => errorMsg);
  }
}
