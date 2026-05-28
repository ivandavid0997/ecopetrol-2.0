import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { tap, catchError } from 'rxjs/operators';

@Injectable()
export class LoggingInterceptor implements HttpInterceptor {

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const startTime = Date.now();
    const { method, urlWithParams } = request;

    // Log de la petición saliente
    console.log(`%c[HTTP Request] OUT -> ${method} ${urlWithParams}`, 'color: #00bcd4; font-weight: bold;');
    if (request.body) {
      console.log('Payload:', request.body);
    }

    return next.handle(request).pipe(
      tap((event: HttpEvent<unknown>) => {
        if (event instanceof HttpResponse) {
          const elapsedTime = Date.now() - startTime;
          // Log de la respuesta exitosa (simulada o real)
          console.log(
            `%c[HTTP Response] IN <- ${method} ${urlWithParams} | Status: ${event.status} | ${elapsedTime}ms`,
            'color: #4caf50; font-weight: bold;'
          );
          console.log('Response Body:', event.body);
        }
      }),
      catchError((error: HttpErrorResponse) => {
        const elapsedTime = Date.now() - startTime;
        // Log en caso de fallos de red o errores HTTP
        console.error(
          `%c[HTTP Error] IN <- ${method} ${urlWithParams} | Status: ${error.status} | ${elapsedTime}ms`,
          'color: #f44336; font-weight: bold;'
        );
        console.error('Error Details:', error.error || error.message);

        return throwError(() => error);
      })
    );
  }
}
