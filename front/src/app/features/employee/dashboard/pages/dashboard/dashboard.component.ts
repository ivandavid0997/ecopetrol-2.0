import { VacationComponent } from './../../../vacation/pages/vacation/vacation.component';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

interface AbsenceOption {
  id: string;
  label: string;
  disabled?: boolean;
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {

  absenceOptions: AbsenceOption[] = [
    { id: 'vacation', label: 'Vacaciones' },
    { id: 'disabilities', label: 'Incapacidad' },
    { id: 'calamity', label: 'Calamidad' },
    { id: 'birthday', label: 'Día de Cumpleaños' },
  ];

  constructor(private router: Router) {}

  selectAbsence(option: AbsenceOption): void {
    if (option.disabled) return;
    console.log(`Ausencia solicitada: ${option.label} ${option.id}`);

    this.router.navigate(['/'+`${option.id}`]);
  }

  changeModule(): void {
    this.router.navigate(['/rol']);
  }
}
