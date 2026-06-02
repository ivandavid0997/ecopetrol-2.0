import { ChangeDetectionStrategy, Component, input } from '@angular/core';

/** Logo Ecopetrol: iguana + wordmark "ecoPETROL" (apilado). */
@Component({
  selector: 'app-logo',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="flex flex-col items-center">
      <img src="iguana.png" alt="Ecopetrol" [style.width.px]="iguanaWidth()" class="select-none -mb-1" />
      <div class="font-display font-extrabold tracking-tight leading-none" [class]="textSize()">
        <span class="text-eco-yellow">eco</span><span class="text-eco-green">PETROL</span>
      </div>
    </div>
  `,
})
export class Logo {
  /** 'lg' para login/rol, 'md' por defecto */
  readonly size = input<'md' | 'lg'>('lg');

  protected iguanaWidth() {
    return this.size() === 'lg' ? 150 : 110;
  }
  protected textSize() {
    return this.size() === 'lg' ? 'text-4xl' : 'text-2xl';
  }
}
