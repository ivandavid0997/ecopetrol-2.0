import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, forkJoin, throwError } from 'rxjs';
import { map, catchError, delay } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { formatDate, mapStatus } from './field-mapper';

@Injectable({
  providedIn: 'root'
})
export class PeopleRequestsService {

  constructor(private http: HttpClient) { }

  /**
   * Obtiene todas las solicitudes (vacaciones, calamidad, cumpleaños, incapacidad) con un solo método,
   * agregando el campo requestType para diferenciarlas.
   */
  getAllRequests(): Observable<any[]> {
    return forkJoin([
      this.getVacations(),
      this.getCalamities(),
      this.getBirthdays(),
      this.getDisabilities()
    ]).pipe(
      map(([vacations, calamities, birthdays, disabilities]) => [
        ...vacations,
        ...calamities,
        ...birthdays,
        ...disabilities
      ]),
      catchError(this.handleError)
    );
  }

  private getVacations(): Observable<any[]> {
    if (environment.useMock) {
      // reutiliza el mock genérico de vacaciones
      return this.http.get<any[]>(environment.mockUrl.vacation).pipe(
        delay(400),
        map(items => items.map(v => ({
          id: v.id,
          cedula: v.numeroEmpleado,
          collaborator: v.nombreEmpleado,
          startDate: formatDate(v.fechaInicio),
          endDate: formatDate(v.fechaFin),
          daysRequested: v.totalDias,
          status: mapStatus(v.estado),
          requestType: 'Vacaciones'
        }))),
        catchError(this.handleError)
      );
    }

    // ENDPOINT VACATION desde environment
    return this.http.get<any[]>(`${environment.apiUrl}${environment.endpoint.vacation}`).pipe(
      map(items => items.map(v => ({
        id: v.id,
        cedula: v.numeroEmpleado,
        collaborator: v.nombreEmpleado,
        startDate: formatDate(v.fechaInicio),
        endDate: formatDate(v.fechaFin),
        daysRequested: v.totalDias,
        status: mapStatus(v.estado),
        requestType: 'Vacaciones'
      }))),
      catchError(this.handleError)
    );
  }

  private getCalamities(): Observable<any[]> {
    if (environment.useMock) {
      return this.http.get<any[]>(environment.mockUrl.calamity).pipe(
        delay(400),
        map(items => items.map(c => ({
          id: c.id,
          cedula: c.numeroEmpleado,
          collaborator: c.nombreEmpleado,
          startDate: formatDate(c.fechaInicio),
          endDate: formatDate(c.fechaFin),
          daysRequested: c.totalDias,
          status: mapStatus(c.estado),
          requestType: 'Calamidad'
        }))),
        catchError(this.handleError)
      );
    }

    // ENDPOINT CALAMITY desde environment
    return this.http.get<any[]>(`${environment.apiUrl}${environment.endpoint.calamity}`).pipe(
      map(items => items.map(c => ({
        id: c.id,
        cedula: c.numeroEmpleado,
        collaborator: c.nombreEmpleado,
        startDate: formatDate(c.fechaInicio),
        endDate: formatDate(c.fechaFin),
        daysRequested: c.totalDias,
        status: mapStatus(c.estado),
        requestType: 'Calamidad'
      }))),
      catchError(this.handleError)
    );
  }

  private getBirthdays(): Observable<any[]> {
    if (environment.useMock) {
      return this.http.get<any[]>(environment.mockUrl.birthday).pipe(
        delay(400),
        map(items => items.map(d => ({
          id: d.id,
          cedula: d.numeroEmpleado,
          collaborator: d.nombreEmpleado,
          startDate: formatDate(d.fechaCumpleanio),
          endDate: formatDate(d.fechaCumpleanio),
          daysRequested: 1,
          status: mapStatus(d.estado),
          requestType: 'Cumpleaños'
        }))),
        catchError(this.handleError)
      );
    }

    // ENDPOINT BIRTHDAY desde environment
    return this.http.get<any[]>(`${environment.apiUrl}${environment.endpoint.birthday}`).pipe(
      map(items => items.map(d => ({
        id: d.id,
        cedula: d.numeroEmpleado,
        collaborator: d.nombreEmpleado,
        startDate: formatDate(d.fechaCumpleanio),
        endDate: formatDate(d.fechaCumpleanio),
        daysRequested: 1,
        status: mapStatus(d.estado),
        requestType: 'Cumpleaños'
      }))),
      catchError(this.handleError)
    );
  }

  private getDisabilities(): Observable<any[]> {
    if (environment.useMock) {
      return this.http.get<any[]>(environment.mockUrl.disabilities).pipe(
        delay(400),
        map(items => items.map(i => ({
          id: i.id,
          cedula: i.numeroEmpleado,
          collaborator: i.nombreEmpleado,
          startDate: formatDate(i.fechaInicio),
          endDate: formatDate(i.fechaFin),
          daysRequested: i.totalDias,
          status: mapStatus(i.estado),
          requestType: 'Incapacidad'
        }))),
        catchError(this.handleError)
      );
    }

    // ENDPOINT DISABILITIES desde environment
    return this.http.get<any[]>(`${environment.apiUrl}${environment.endpoint.disabilities}`).pipe(
      map(items => items.map(i => ({
        id: i.id,
        cedula: i.numeroEmpleado,
        collaborator: i.nombreEmpleado,
        startDate: formatDate(i.fechaInicio),
        endDate: formatDate(i.fechaFin),
        daysRequested: i.totalDias,
        status: mapStatus(i.estado),
        requestType: 'Incapacidad'
      }))),
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse) {
    const errorMsg = error.error?.message || 'Error de conexión con el servidor backend al obtener solicitudes de People';
    return throwError(() => errorMsg);
  }
}
