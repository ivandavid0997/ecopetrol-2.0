import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DisabilitiesService } from 'src/app/core/services/disabilities.service';

interface DisabilitiesRequest {
  id: number;
  cedula: string;
  collaborator: string;
  startDate: string;
  endDate: string;
  daysRequested: number;
  status: string;
}

@Component({
  selector: 'app-disabilities',
  templateUrl: './disabilities.component.html',
  styleUrls: ['./disabilities.component.css']
})
export class DisabilitiesComponent implements OnInit {

  // Estado del menú lateral colapsable
  isSidebarClosed: boolean = false;

  disabilitiesRequests: DisabilitiesRequest[] = [];
  isLoading: boolean = false;

  constructor(
    private router: Router,
    private disabilitiesService: DisabilitiesService
  ) { }

  ngOnInit(): void {
    this.loadDisabilities();
  }

  loadDisabilities(): void {
    this.isLoading = true;
    this.disabilitiesService.getDisabilities().subscribe({
      next: (data) => {
        this.disabilitiesRequests = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar las incapacidades:', err);
        this.disabilitiesRequests = [];
        this.isLoading = false;
      }
    });
  }

  toggleSidebar(): void {
    this.isSidebarClosed = !this.isSidebarClosed;
  }

  viewRequest(request: DisabilitiesRequest): void {
    console.log('Visualizando solicitud de:', request.collaborator);
  }

  editRequest(request: DisabilitiesRequest): void {
    console.log('Editando solicitud ID:', request.id);
  }

  createNewRequest(): void {
    console.log('Abriendo formulario de nueva solicitud');
    this.router.navigate(['/disabilities/create']);
  }

  reloadRequests(): void {
    console.log('Recargando el listado de solicitudes de incapacidades...');
    this.loadDisabilities();
  }
}
