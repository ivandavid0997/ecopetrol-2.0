import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CalamityLeaderRequestsService } from 'src/app/core/services/calamity-leader-requests.service';

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
  isSidebarClosed: boolean = false;

  calamityRequests: CalamityRequest[] = [];
  isLoading: boolean = false;
  errorMessage: string = '';

  constructor(
    private router: Router,
    private calamityLeaderRequestsService: CalamityLeaderRequestsService
  ) { }

  ngOnInit(): void {
    this.loadRequests();
  }

  loadRequests(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.calamityLeaderRequestsService.getRequests().subscribe({
      next: (data) => {
        this.calamityRequests = data;
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = 'Error al cargar las solicitudes: ' + err;
        this.isLoading = false;
      }
    });
  }

  toggleSidebar(): void {
    this.isSidebarClosed = !this.isSidebarClosed;
  }

  viewRequest(request: CalamityRequest): void {
    this.router.navigate(['/calamity-leader/detail', request.id]);
  }

  reloadRequests(): void {
    this.loadRequests();
  }
}
