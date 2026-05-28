import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, of } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PeopleMonthClosureService {

  constructor(private http: HttpClient) { }

  /**
   * Realiza el cierre de mes para solicitudes de ausencias
   * Consolida y finaliza todas las solicitudes del mes actual
   */
  closeMonth(): Observable<string> {
    // Endpoint asumido; ajustar según la API real del backend
    const url = `${environment.apiUrl}${environment.endpoint.closeMonth}`;

    return this.http.post<any>(url, {}).pipe(
      map(() => {
        const msg = 'El cierre del mes se ha enviado correctamente.';
        return msg;
      }),
      catchError((error: HttpErrorResponse) => {
        const errorMsg = error.error?.message || 'Error de conexión con el backend al realizar cierre de mes';
        return throwError(() => errorMsg);
      })
    );
  }

  /**
   * Obtiene el estado actual del cierre de mes
   */
  getMonthClosureStatus(): Observable<any> {
    const url = `${environment.apiUrl}${environment.endpoint.monthClosureStatus}`;

    return this.http.get<any>(url).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    const errorMsg = error.error?.message || 'Error de conexión con el backend al realizar cierre de mes';
    return throwError(() => errorMsg);
  }
}
