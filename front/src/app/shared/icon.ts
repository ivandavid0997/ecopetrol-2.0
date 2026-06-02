import { ChangeDetectionStrategy, Component, input } from '@angular/core';

export type IconName =
  | 'plane'
  | 'health'
  | 'warning'
  | 'gift'
  | 'user'
  | 'briefcase'
  | 'people'
  | 'menu'
  | 'bell'
  | 'globe'
  | 'calendar'
  | 'file'
  | 'check'
  | 'x'
  | 'download'
  | 'search'
  | 'filter'
  | 'arrow-right'
  | 'arrow-left'
  | 'paperclip'
  | 'lock'
  | 'user-circle'
  | 'logout';

/** Ícono SVG (trazo, hereda currentColor). Uso: <app-icon name="plane" class="w-5 h-5" /> */
@Component({
  selector: 'app-icon',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <svg
      [attr.width]="size()"
      [attr.height]="size()"
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      stroke-width="1.9"
      stroke-linecap="round"
      stroke-linejoin="round"
      aria-hidden="true"
    >
      @switch (name()) {
        @case ('plane') {
          <path d="M17.8 19.2 16 11l3.5-3.5a2.1 2.1 0 0 0-3-3L13 8 4.8 6.2a.5.5 0 0 0-.5.8l3.9 4-2.2 2.2-2-.4a.5.5 0 0 0-.5.8L4.8 17l1.4 1.7a.5.5 0 0 0 .8-.1l1.1-2 2.2-2.2 4 3.9a.5.5 0 0 0 .8-.5Z" />
        }
        @case ('health') {
          <path d="M19 14c1.5-1.5 3-3.2 3-5.5A5.5 5.5 0 0 0 16.5 3c-1.8 0-3 .5-4.5 2-1.5-1.5-2.7-2-4.5-2A5.5 5.5 0 0 0 2 8.5c0 2.3 1.5 4 3 5.5l7 7Z" />
          <path d="M3.2 12h4l1.5-3 2.5 6 1.5-3h4" />
        }
        @case ('warning') {
          <path d="M10.3 3.8 1.8 18a2 2 0 0 0 1.7 3h17a2 2 0 0 0 1.7-3L13.7 3.8a2 2 0 0 0-3.4 0Z" />
          <line x1="12" y1="9" x2="12" y2="13" />
          <line x1="12" y1="17" x2="12.01" y2="17" />
        }
        @case ('gift') {
          <rect x="3" y="8" width="18" height="4" rx="1" />
          <path d="M12 8v13M5 12v7a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2v-7" />
          <path d="M12 8C12 8 12 3 8.5 3A2.5 2.5 0 0 0 8.5 8H12ZM12 8s0-5 3.5-5A2.5 2.5 0 0 1 15.5 8H12Z" />
        }
        @case ('user') {
          <path d="M20 21a8 8 0 0 0-16 0" />
          <circle cx="12" cy="7" r="4" />
        }
        @case ('briefcase') {
          <rect x="2.5" y="7" width="19" height="13" rx="2" />
          <path d="M16 21V5a2 2 0 0 0-2-2h-4a2 2 0 0 0-2 2v16" />
        }
        @case ('people') {
          <path d="M17 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2" />
          <circle cx="9.5" cy="7" r="4" />
          <path d="M22 21v-2a4 4 0 0 0-3-3.9M16 3.1a4 4 0 0 1 0 7.8" />
        }
        @case ('menu') {
          <line x1="3" y1="6" x2="21" y2="6" /><line x1="3" y1="12" x2="21" y2="12" /><line x1="3" y1="18" x2="21" y2="18" />
        }
        @case ('bell') {
          <path d="M18 8a6 6 0 0 0-12 0c0 7-3 9-3 9h18s-3-2-3-9" />
          <path d="M13.7 21a2 2 0 0 1-3.4 0" />
        }
        @case ('globe') {
          <circle cx="12" cy="12" r="9" />
          <path d="M3 12h18M12 3a15 15 0 0 1 0 18 15 15 0 0 1 0-18Z" />
        }
        @case ('calendar') {
          <rect x="3" y="4.5" width="18" height="17" rx="2" />
          <path d="M16 2.5v4M8 2.5v4M3 9.5h18" />
        }
        @case ('file') {
          <path d="M14 2.5H7a2 2 0 0 0-2 2v15a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2V7.5Z" />
          <path d="M14 2.5v5h5M9 13h6M9 17h6" />
        }
        @case ('check') {
          <polyline points="20 6 9 17 4 12" />
        }
        @case ('x') {
          <line x1="18" y1="6" x2="6" y2="18" /><line x1="6" y1="6" x2="18" y2="18" />
        }
        @case ('download') {
          <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
          <polyline points="7 10 12 15 17 10" /><line x1="12" y1="15" x2="12" y2="3" />
        }
        @case ('search') {
          <circle cx="11" cy="11" r="7" /><line x1="21" y1="21" x2="16.65" y2="16.65" />
        }
        @case ('filter') {
          <polygon points="22 3 2 3 10 12.5 10 19 14 21 14 12.5 22 3" />
        }
        @case ('arrow-right') {
          <line x1="5" y1="12" x2="19" y2="12" /><polyline points="12 5 19 12 12 19" />
        }
        @case ('arrow-left') {
          <line x1="19" y1="12" x2="5" y2="12" /><polyline points="12 19 5 12 12 5" />
        }
        @case ('paperclip') {
          <path d="m21.4 11.1-9 9a5.5 5.5 0 0 1-7.8-7.8l9-9a3.7 3.7 0 0 1 5.2 5.2l-9 9a1.8 1.8 0 0 1-2.6-2.6l8.3-8.3" />
        }
        @case ('lock') {
          <rect x="4" y="11" width="16" height="10" rx="2" />
          <path d="M8 11V7a4 4 0 0 1 8 0v4" />
        }
        @case ('user-circle') {
          <circle cx="12" cy="12" r="9" />
          <circle cx="12" cy="10" r="3" />
          <path d="M6.5 18.5a6 6 0 0 1 11 0" />
        }
        @case ('logout') {
          <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" />
          <polyline points="16 17 21 12 16 7" /><line x1="21" y1="12" x2="9" y2="12" />
        }
      }
    </svg>
  `,
})
export class Icon {
  readonly name = input.required<IconName>();
  readonly size = input<number>(20);
}
