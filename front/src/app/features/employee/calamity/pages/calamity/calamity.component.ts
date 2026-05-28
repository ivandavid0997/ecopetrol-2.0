import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CalamityService } from 'src/app/core/services/calamity.service';

interface CalamityRequest {
  id: number;
  cedula: string;
  collaborator: string;
  startDate: string;
  endDate: string;
  daysRequested: number;
  status: string;
}

@Component({
  selector: 'app-calamity',
  templateUrl: './calamity.component.html',
  styleUrls: ['./calamity.component.css']
})
export class CalamityComponent implements OnInit {
  // Estado del menú lateral colapsable
  isSidebarClosed: boolean = false;

  calamityRequests: CalamityRequest[] = [];
  isLoading: boolean = false;

  constructor(
    private router: Router,
    private calamityService: CalamityService
  ) { }

  ngOnInit(): void {
    this.loadCalamities();
  }

  loadCalamities(): void {
    this.isLoading = true;
    this.calamityService.getCalamities().subscribe({
      next: (data) => {
        this.calamityRequests = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar las calamidades:', err);
        this.calamityRequests = [];
        this.isLoading = false;
      }
    });
  }

  toggleSidebar(): void {
    this.isSidebarClosed = !this.isSidebarClosed;
  }

  viewRequest(request: CalamityRequest): void {
    console.log('Visualizando solicitud de:', request.collaborator);
  }

  editRequest(request: CalamityRequest): void {
    console.log('Editando solicitud ID:', request.id);
  }

  createNewRequest(): void {
    console.log('Abriendo formulario de nueva solicitud');
    this.router.navigate(['/calamity/create']);
  }

  reloadRequests(): void {
    console.log('Recargando el listado de solicitudes de calamidades...');
    this.loadCalamities();
  }
}
