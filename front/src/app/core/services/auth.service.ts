import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map, catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

export interface AuthUser {
  id?: number;
  usuario: string;
  rol: string;
  nombre?: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly USER_KEY = 'User';
  private readonly SESSION_KEY = 'X-Session-ID';

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<AuthUser> {
    const body = { usuario: username, contrasena: password };

    return this.http.post<AuthUser>(
      `${environment.apiUrl}${environment.endpoint.login}`,
      body,
      { observe: 'response' }
    ).pipe(
      map((response: HttpResponse<AuthUser>) => {
        const user = response.body;

        if (!user) {
          throw new Error('Respuesta inválida del servidor');
        }

        // Guardar información en sessionStorage
        sessionStorage.setItem(this.USER_KEY, JSON.stringify(user));
        sessionStorage.setItem(this.SESSION_KEY, '56432'); // Valor fijo para simular sesión
        // const sessionId = response.headers.get(this.SESSION_KEY);

        // if (!sessionId) {
        //   throw new Error('El servidor no devolvió el X-Session-ID');
        // }

        // sessionStorage.setItem(this.SESSION_KEY, sessionId);

        return user;
      }),
      catchError((error: HttpErrorResponse) => {
        const errorMsg =
          error.error?.message ||
          error.error?.mensaje ||
          'Credenciales inválidas o error de conexión';
        return throwError(() => errorMsg);
      })
    );
  }
}
