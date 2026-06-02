import { Injectable, computed, inject, signal } from '@angular/core';
import { Observable, catchError, map, of, switchMap, tap } from 'rxjs';
import { environment } from '../../environments/environment';
import { TalentoApi } from './api.service';
import { LoginResponse, UsuarioApi, rolFromBackend } from './mappers';
import { Rol, Usuario } from './models';
import { USUARIO_DEMO } from './mock-data';

const LS_KEY = 'eco_session';

interface SesionPersistida {
  token: string | null;
  usuario: Usuario;
  rol: Rol | null;
}

function iniciales(nombre: string): string {
  const parts = nombre.trim().split(/\s+/);
  return ((parts[0]?.[0] ?? '') + (parts[1]?.[0] ?? '')).toUpperCase() || 'EC';
}

/** Sesión: login contra /autenticacion/login (JWT) + rol del backend. */
@Injectable({ providedIn: 'root' })
export class SessionService {
  private readonly api = inject(TalentoApi);

  private readonly _token = signal<string | null>(null);
  private readonly _usuario = signal<Usuario | null>(null);
  private readonly _rol = signal<Rol | null>(null);

  readonly token = this._token.asReadonly();
  readonly usuario = this._usuario.asReadonly();
  readonly rol = this._rol.asReadonly();
  readonly autenticado = computed(() => this._usuario() !== null);

  constructor() {
    const raw = localStorage.getItem(LS_KEY);
    if (raw) {
      try {
        const s = JSON.parse(raw) as SesionPersistida;
        this._token.set(s.token);
        this._usuario.set(s.usuario);
        this._rol.set(s.rol);
      } catch {
        /* ignore */
      }
    }
  }

  /** Login: mock o real según environment.useMock. */
  login(usuario: string, contrasena: string): Observable<Usuario> {
    if (environment.useMock) {
      const u: Usuario = { ...USUARIO_DEMO };
      this._token.set('mock-token');
      this._usuario.set(u);
      this._rol.set(null);
      this.persistir();
      return of(u);
    }

    return this.api.login(usuario, contrasena).pipe(
      tap((r: LoginResponse) => {
        this._token.set(r.token);
        this._rol.set(rolFromBackend(r.rol));
      }),
      switchMap((r) =>
        this.api.listaUsuarios().pipe(
          map((users) => users.find((u) => u.usuario?.toLowerCase() === usuario.trim().toLowerCase())),
          map((u) => this.construir(r.usuario, u)),
          catchError(() => of(this.construir(r.usuario, undefined))),
        ),
      ),
      tap((u) => {
        this._usuario.set(u);
        this.persistir();
      }),
    );
  }

  seleccionarRol(rol: Rol): void {
    this._rol.set(rol);
    this.persistir();
  }

  logout(): void {
    this._token.set(null);
    this._usuario.set(null);
    this._rol.set(null);
    localStorage.removeItem(LS_KEY);
  }

  private construir(usuarioLogin: string, u: UsuarioApi | undefined): Usuario {
    const nombre = u?.nombre ?? usuarioLogin;
    return {
      id: u ? String(u.id) : usuarioLogin,
      nombre,
      correo: `${usuarioLogin}@ecopetrol.com.co`,
      iniciales: iniciales(nombre),
      numeroEmpleado: u?.numeroEmpleado != null ? String(u.numeroEmpleado) : undefined,
    };
  }

  private persistir(): void {
    const u = this._usuario();
    if (!u) return;
    localStorage.setItem(LS_KEY, JSON.stringify({ token: this._token(), usuario: u, rol: this._rol() } satisfies SesionPersistida));
  }
}
