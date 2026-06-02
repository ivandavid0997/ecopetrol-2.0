import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { Router } from '@angular/router';
import { Rol, ROLES_META } from '../../core/models';
import { SessionService } from '../../core/session.service';
import { Icon } from '../../shared/icon';
import { Logo } from '../../shared/logo';

@Component({
  selector: 'app-seleccion-rol',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [Icon, Logo],
  template: `
    <div class="flex min-h-screen items-center justify-center bg-surface p-6">
      <div class="app-card w-full max-w-md p-8 sm:p-10">
        <app-logo size="lg" />

        <h1 class="mt-6 text-center text-3xl font-bold text-ink">Selecciona tu Rol</h1>
        <p class="mt-1.5 text-center text-sm text-muted">
          Bienvenido, <span class="font-semibold text-ink">{{ usuario()?.nombre }}</span>
        </p>

        <div class="mt-7 space-y-3">
          @for (r of roles; track r.rol) {
            <button
              class="group flex w-full items-center gap-4 rounded-xl border border-line bg-white p-4 text-left transition hover:border-brand hover:shadow-md"
              (click)="elegir(r.rol)"
            >
              <span class="flex h-12 w-12 shrink-0 items-center justify-center rounded-xl text-white" [class]="r.iconClasses">
                <app-icon [name]="r.icon" [size]="24" />
              </span>
              <span class="min-w-0">
                <span class="block font-semibold text-ink">{{ r.titulo }}</span>
                <span class="block text-sm text-muted">{{ r.descripcion }}</span>
              </span>
            </button>
          }
        </div>

        <div class="mt-6 text-center">
          <button class="btn-ghost mx-auto" (click)="volver()">
            <app-icon name="arrow-left" [size]="16" /> Volver
          </button>
        </div>
      </div>
    </div>
  `,
})
export class SeleccionRol {
  private readonly session = inject(SessionService);
  private readonly router = inject(Router);

  protected readonly usuario = this.session.usuario;
  protected readonly roles = ROLES_META;

  private readonly rutaPorRol: Record<Rol, string> = {
    empleado: '/empleado',
    lider: '/lider',
    people: '/people',
  };

  protected elegir(rol: Rol): void {
    this.session.seleccionarRol(rol);
    this.router.navigate([this.rutaPorRol[rol]]);
  }

  protected volver(): void {
    this.session.logout();
    this.router.navigate(['/login']);
  }
}
