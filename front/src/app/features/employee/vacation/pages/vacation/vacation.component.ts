import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { VacationService } from 'src/app/core/services/vacation.service';

interface VacationRequest {
  id: number;
  cedula: string;
  collaborator: string;
  startDate: string;
  endDate: string;
  daysRequested: number;
  status: string;
}

@Component({
  selector: 'app-vacation',
  templateUrl: './vacation.component.html',
  styleUrls: ['./vacation.component.css']
})
export class VacationComponent implements OnInit {
  // Estado del menú lateral colapsable
  isSidebarClosed: boolean = false;

  vacationRequests: VacationRequest[] = [];
  isLoading: boolean = false;

  constructor(
    private router: Router,
    private vacationService: VacationService
  ) { }

  ngOnInit(): void {
    this.loadVacations();
  }

  loadVacations(): void {
    this.isLoading = true;
    this.vacationService.getVacations().subscribe({
      next: (data) => {
        this.vacationRequests = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar las solicitudes de vacaciones:', err);
        this.vacationRequests = [];
        this.isLoading = false;
      }
    });
  }

  toggleSidebar(): void {
    this.isSidebarClosed = !this.isSidebarClosed;
  }

  viewRequest(request: VacationRequest): void {
    console.log('Visualizando solicitud de:', request.collaborator);
  }

  editRequest(request: VacationRequest): void {
    console.log('Editando solicitud ID:', request.id);
  }

  createNewRequest(): void {
    console.log('Abriendo formulario de nueva solicitud');
    this.router.navigate(['/vacation/create']);
  }

  reloadRequests(): void {
    console.log('Recargando el listado de solicitudes de vacaciones...');
    this.loadVacations();
  }
}
