import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { formatDate, mapStatus } from './field-mapper';

@Injectable({
  providedIn: 'root'
})
export class VacationService {

  constructor(private http: HttpClient) { }

  getVacations(): Observable<any[]> {
    if (environment.useMock) {
      return this.http.get<any[]>(environment.mockUrl.vacation).pipe(catchError(this.handleError));
    }
    return this.http.get<any[]>(`${environment.apiUrl}${environment.endpoint.vacation}`).pipe(
      map(items => items.map(v => ({
        id: v.id,
        cedula: v.numeroEmpleado,
        collaborator: v.nombreEmpleado,
        startDate: formatDate(v.fechaInicio),
        endDate: formatDate(v.fechaFin),
        daysRequested: v.totalDias,
        status: mapStatus(v.estado)
      }))),
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    const errorMsg = error.error?.message || 'Error de conexión con el servidor backend';
    return throwError(() => errorMsg);
  }
}
