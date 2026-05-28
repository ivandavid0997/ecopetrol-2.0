import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { formatDate, mapStatus } from './field-mapper';

@Injectable({
  providedIn: 'root'
})
export class BirthdayService {

  constructor(private http: HttpClient) { }

  getBirthdays(): Observable<any[]> {
    if (environment.useMock) {
      return this.http.get<any[]>(environment.mockUrl.birthday).pipe(catchError(this.handleError));
    }
    return this.http.get<any[]>(`${environment.apiUrl}${environment.endpoint.birthday}`).pipe(
      map(items => items.map(d => ({
        id: d.id,
        cedula: d.numeroEmpleado,
        collaborator: d.nombreEmpleado,
        birthdayDate: formatDate(d.fechaCumpleanio),
        status: mapStatus(d.estado)
      }))),
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    const errorMsg = error.error?.message || 'Error de conexión con el servidor backend';
    return throwError(() => errorMsg);
  }
}
