import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { formatDate, mapStatus } from './field-mapper';

@Injectable({
  providedIn: 'root'
})
export class CalamityService {

  constructor(private http: HttpClient) { }

  getCalamities(): Observable<any[]> {
    if (environment.useMock) {
      return this.http.get<any[]>(environment.mockUrl.calamity).pipe(catchError(this.handleError));
    }
    return this.http.get<any[]>(`${environment.apiUrl}${environment.endpoint.calamity}`).pipe(
      map(items => items.map(c => ({
        id: c.id,
        cedula: c.numeroEmpleado,
        collaborator: c.nombreEmpleado,
        startDate: formatDate(c.fechaInicio),
        endDate: formatDate(c.fechaFin),
        daysRequested: c.totalDias,
        status: mapStatus(c.estado)
      }))),
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    const errorMsg = error.error?.message || 'Error de conexión con el servidor backend';
    return throwError(() => errorMsg);
  }
}
