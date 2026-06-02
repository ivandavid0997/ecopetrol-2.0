import { Injectable, computed, signal } from '@angular/core';
import { Rol, Usuario } from './models';
import { USUARIO_DEMO } from './mock-data';

const LS_KEY = 'eco_session';

interface SesionPersistida {
  usuario: Usuario;
  rol: Rol | null;
}

/** Sesión mock: usuario autenticado + rol seleccionado. */
@Injectable({ providedIn: 'root' })
export class SessionService {
  private readonly _usuario = signal<Usuario | null>(null);
  private readonly _rol = signal<Rol | null>(null);

  readonly usuario = this._usuario.asReadonly();
  readonly rol = this._rol.asReadonly();
  readonly autenticado = computed(() => this._usuario() !== null);

  constructor() {
    const raw = localStorage.getItem(LS_KEY);
    if (raw) {
      try {
        const s = JSON.parse(raw) as SesionPersistida;
        this._usuario.set(s.usuario);
        this._rol.set(s.rol);
      } catch {
        /* ignore */
      }
    }
  }

  /** Login mock — no valida contraseña, igual que el backend actual. */
  login(_usuario: string, _contrasena: string): Usuario {
    const usuario: Usuario = { ...USUARIO_DEMO };
    this._usuario.set(usuario);
    this._rol.set(null);
    this.persistir();
    return usuario;
  }

  seleccionarRol(rol: Rol): void {
    this._rol.set(rol);
    this.persistir();
  }

  logout(): void {
    this._usuario.set(null);
    this._rol.set(null);
    localStorage.removeItem(LS_KEY);
  }

  private persistir(): void {
    const u = this._usuario();
    if (!u) return;
    localStorage.setItem(LS_KEY, JSON.stringify({ usuario: u, rol: this._rol() } satisfies SesionPersistida));
  }
}
