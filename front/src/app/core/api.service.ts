import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { API_TIPO, environment } from '../../environments/environment';
import { LoginResponse, UsuarioApi } from './mappers';
import { TipoPermiso } from './models';

/** Cliente HTTP del backend talento (endpoints por feature + JWT). */
@Injectable({ providedIn: 'root' })
export class TalentoApi {
  private readonly http = inject(HttpClient);

  login(usuario: string, contrasena: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(environment.api.login, { usuario, contrasena });
  }

  listaUsuarios(): Observable<UsuarioApi[]> {
    return this.http.get<UsuarioApi[]>(environment.api.usuarios);
  }

  listar(tipo: TipoPermiso): Observable<any[]> {
    return this.http.get<any[]>(API_TIPO[tipo].listar);
  }

  crear(tipo: TipoPermiso, payload: unknown): Observable<unknown> {
    return this.http.post(API_TIPO[tipo].crear, payload);
  }

  aprobar(tipo: TipoPermiso, id: string): Observable<unknown> {
    return this.http.post(`${API_TIPO[tipo].aprobar}/${id}`, {}, { responseType: 'text' });
  }

  rechazar(tipo: TipoPermiso, id: string): Observable<unknown> {
    return this.http.post(`${API_TIPO[tipo].rechazar}/${id}`, {}, { responseType: 'text' });
  }

  cierreMes(): Observable<unknown> {
    return this.http.post(environment.api.cierreMes, {});
  }
}
