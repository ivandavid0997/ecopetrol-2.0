import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { formatDate, mapStatus } from './field-mapper';

@Injectable({
  providedIn: 'root'
})
export class CalamityLeaderRequestsService {
  constructor(private http: HttpClient) { }

  private getAuthHeaders(): HttpHeaders {
    const sessionId = sessionStorage.getItem('X-Session-ID');
    return new HttpHeaders({
      'X-Session-ID': sessionId || ''
    });
  }

  getRequests(): Observable<any[]> {
    return this.http.get<any[]>(`${environment.apiUrl}${environment.endpoint.calamityLeader}`).pipe(
      map(items => items.map(c => ({
        id: c.id,
        cedula: c.numeroEmpleado,
        collaborator: c.nombreEmpleado,
        startDate: formatDate(c.fechaInicio),
        endDate: formatDate(c.fechaFin),
        daysRequested: c.totalDias,
        status: mapStatus(c.estado),
        businessUnit: c.unidadNegocio,
        comments: c.comentario,
        leaderName: c.lider?.nombre || '',
        attachedFileName: c.archivoAdjunto || ''
      }))),
      catchError(this.handleError)
    );
  }

  approve(id: number): Observable<any> {
    return this.http.post<any>(
      `${environment.apiUrl}${environment.endpoint.approveCalamity}/${id}?rol=LIDER`,
      {},
      { headers: this.getAuthHeaders() }
    ).pipe(
      map((resp) => resp),
      catchError((error: HttpErrorResponse) => {

        if (error.status === 400) {
          const msg = error.error?.message || 'La solicitud no cumple las reglas de negocio.';
          return throwError(() => msg);
        }

        if (error.status === 404) {
          const msg = typeof error.error === 'string'
            ? error.error
            : 'Solicitud no encontrada.';
          return throwError(() => msg);
        }

        if (error.status === 409) {
          const msg = error.error?.message || 'La solicitud ya fue procesada.';
          return throwError(() => msg);
        }

        if (error.status === 500) {
          const msg = typeof error.error === 'string'
            ? error.error
            : 'Error interno del servidor.';
          return throwError(() => msg);
        }

        const msg = error.error?.message || 'Error de conexión con el servidor backend';
        return throwError(() => msg);
      })
    );
  }

  reject(id: number): Observable<any> {
    return this.http.post<any>(`${environment.apiUrl}${environment.endpoint.rejectCalamity}/${id}?rol=LIDER`, {}).pipe(
      map((resp) => resp),
      catchError((error: HttpErrorResponse) => {

        if (error.status === 400) {
          const msg = error.error?.message || 'No se puede rechazar esta solicitud.';
          return throwError(() => msg);
        }

        if (error.status === 404) {
          const msg = typeof error.error === 'string'
            ? error.error
            : 'Solicitud no encontrada.';
          return throwError(() => msg);
        }

        if (error.status === 409) {
          const msg = error.error?.message || 'La solicitud ya fue procesada.';
          return throwError(() => msg);
        }

        if (error.status === 500) {
          const msg = typeof error.error === 'string'
            ? error.error
            : 'Error interno del servidor.';
          return throwError(() => msg);
        }

        const msg = error.error?.message || 'Error de conexión con el servidor backend';
        return throwError(() => msg);
      })
    );
  }

  private handleError(error: HttpErrorResponse) {
    const errorMsg = error.error?.message || 'Error de conexión con el servidor backend';
    return throwError(() => errorMsg);
  }
}
