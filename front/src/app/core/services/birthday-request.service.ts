import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BirthdayRequestService {

  constructor(private http: HttpClient) { }

  createBirthdayRequest(payload: any): Observable<any> {
    const body = {
      numeroEmpleado: payload.employeeNumber,
      nombreEmpleado: payload.employeeName,
      unidadNegocio: payload.businessUnit,
      fechaCumpleanio: payload.startDate || null,
      comentario: payload.comments || null,
      lider: null,
      fechaCreacion: new Date().toISOString()
    };
    return this.http.post<any>(`${environment.apiUrl}${environment.endpoint.createBirthday}`, body).pipe(
      catchError((error: HttpErrorResponse) => {
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
