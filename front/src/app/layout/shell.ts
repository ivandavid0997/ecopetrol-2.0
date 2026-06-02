import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { SessionService } from '../core/session.service';
import { Icon } from '../shared/icon';

/** Layout autenticado: navbar teal + contenido. */
@Component({
  selector: 'app-shell',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [RouterOutlet, Icon],
  template: `
    <div class="flex min-h-screen flex-col">
      <header class="bg-brand-dark text-white shadow-md">
        <div class="mx-auto flex h-16 max-w-[1400px] items-center gap-4 px-5 lg:px-10">
          <button class="rounded-lg p-1.5 hover:bg-white/10" (click)="cambiarRol()" aria-label="Cambiar de rol">
            <app-icon name="menu" [size]="22" />
          </button>

          <a class="flex items-center gap-2.5" routerLink="/">
            <span class="flex h-8 w-8 items-center justify-center rounded-full bg-eco-yellow text-brand-dark">
              <app-icon name="globe" [size]="20" />
            </span>
            <span class="text-lg font-semibold tracking-tight">Portal de solicitudes</span>
          </a>

          <div class="ml-auto flex items-center gap-4">
            <button class="relative rounded-lg p-1.5 hover:bg-white/10" aria-label="Notificaciones">
              <app-icon name="bell" [size]="20" />
            </button>

            <div class="relative">
              <button class="flex items-center gap-2.5" (click)="toggleMenu()">
                <span class="flex h-9 w-9 items-center justify-center rounded-full bg-white text-sm font-bold text-brand-dark">
                  {{ usuario()?.iniciales }}
                </span>
                <span class="hidden text-left leading-tight sm:block">
                  <span class="block text-sm font-semibold">{{ usuario()?.nombre }}</span>
                  <span class="block text-xs text-white/70">{{ usuario()?.correo }}</span>
                </span>
              </button>

              @if (menuAbierto()) {
                <div
                  class="animate-rise absolute right-0 top-12 z-40 w-52 overflow-hidden rounded-xl bg-white py-1 text-ink shadow-xl ring-1 ring-black/5"
                >
                  <button class="flex w-full items-center gap-2 px-4 py-2.5 text-sm hover:bg-slate-50" (click)="cambiarRol()">
                    <app-icon name="people" [size]="16" /> Cambiar de rol
                  </button>
                  <button class="flex w-full items-center gap-2 px-4 py-2.5 text-sm text-red-600 hover:bg-red-50" (click)="logout()">
                    <app-icon name="logout" [size]="16" /> Cerrar sesión
                  </button>
                </div>
              }
            </div>
          </div>
        </div>
      </header>

      <main class="mx-auto w-full max-w-[1400px] flex-1 px-5 py-8 lg:px-10">
        <router-outlet />
      </main>
    </div>
  `,
})
export class Shell {
  private readonly session = inject(SessionService);
  private readonly router = inject(Router);

  protected readonly usuario = this.session.usuario;
  protected readonly menuAbierto = signal(false);

  protected toggleMenu() {
    this.menuAbierto.update((v) => !v);
  }
  protected cambiarRol() {
    this.menuAbierto.set(false);
    this.router.navigate(['/rol']);
  }
  protected logout() {
    this.session.logout();
    this.router.navigate(['/login']);
  }
}
