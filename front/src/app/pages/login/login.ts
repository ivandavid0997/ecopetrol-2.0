import { ChangeDetectionStrategy, Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { SessionService } from '../../core/session.service';
import { ToastService } from '../../core/toast.service';
import { Icon } from '../../shared/icon';
import { Logo } from '../../shared/logo';

@Component({
  selector: 'app-login',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [ReactiveFormsModule, Icon, Logo],
  template: `
    <div class="flex min-h-screen items-center justify-center bg-surface p-6">
      <div class="app-card w-full max-w-md p-8 sm:p-10">
        <app-logo size="lg" />

        <h1 class="mt-6 text-center text-3xl font-bold text-ink">Sistema de Permisos</h1>
        <p class="mt-1.5 text-center text-sm text-muted">Ingresa tus credenciales para continuar</p>

        <form class="mt-8 space-y-5" [formGroup]="form" (ngSubmit)="continuar()">
          <div>
            <label class="field-label" for="usuario">Usuario</label>
            <div class="relative">
              <span class="pointer-events-none absolute left-3 top-1/2 -translate-y-1/2 text-slate-400">
                <app-icon name="user-circle" [size]="18" />
              </span>
              <input id="usuario" formControlName="usuario" class="field-input pl-10" placeholder="Ingresa tu usuario" autocomplete="username" />
            </div>
          </div>

          <div>
            <label class="field-label" for="contrasena">Contraseña</label>
            <div class="relative">
              <span class="pointer-events-none absolute left-3 top-1/2 -translate-y-1/2 text-slate-400">
                <app-icon name="lock" [size]="18" />
              </span>
              <input
                id="contrasena"
                type="password"
                formControlName="contrasena"
                class="field-input pl-10"
                placeholder="Ingresa tu contraseña"
                autocomplete="current-password"
              />
            </div>
          </div>

          <button type="submit" class="btn-primary btn-block mt-2" [disabled]="form.invalid || cargando()">
            {{ cargando() ? 'Ingresando…' : 'Continuar' }}
            @if (!cargando()) {
              <app-icon name="arrow-right" [size]="18" />
            }
          </button>
        </form>
      </div>
    </div>
  `,
})
export class Login {
  private readonly fb = inject(FormBuilder);
  private readonly session = inject(SessionService);
  private readonly router = inject(Router);
  private readonly toast = inject(ToastService);

  protected readonly form = this.fb.nonNullable.group({
    usuario: ['', Validators.required],
    contrasena: ['', Validators.required],
  });

  protected readonly cargando = signal(false);

  protected continuar(): void {
    if (this.form.invalid || this.cargando()) return;
    const { usuario, contrasena } = this.form.getRawValue();
    this.cargando.set(true);
    this.session.login(usuario, contrasena).subscribe({
      next: () => {
        this.cargando.set(false);
        this.router.navigate(['/rol']);
      },
      error: () => {
        this.cargando.set(false);
        this.toast.error('Usuario o contraseña incorrectos');
      },
    });
  }
}
