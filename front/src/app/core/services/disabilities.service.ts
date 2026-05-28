import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { formatDate, mapStatus } from './field-mapper';

@Injectable({
  providedIn: 'root'
})
export class DisabilitiesService {

  constructor(private http: HttpClient) { }

  getDisabilities(): Observable<any[]> {
    if (environment.useMock) {
      return this.http.get<any[]>(environment.mockUrl.disabilities).pipe(catchError(this.handleError));
    }
    return this.http.get<any[]>(`${environment.apiUrl}${environment.endpoint.disabilities}`).pipe(
      map(items => items.map(i => ({
        id: i.id,
        cedula: i.numeroEmpleado,
        collaborator: i.nombreEmpleado,
        startDate: formatDate(i.fechaInicio),
        endDate: formatDate(i.fechaFin),
        daysRequested: i.totalDias,
        status: mapStatus(i.estado)
      }))),
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    const errorMsg = error.error?.message || 'Error de conexión con el servidor backend';
    return throwError(() => errorMsg);
  }
}
