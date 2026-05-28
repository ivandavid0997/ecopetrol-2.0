import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { VacationLeaderRequestsService } from 'src/app/core/services/vacation-leader-requests.service';

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
  isSidebarClosed: boolean = false;

  vacationRequests: VacationRequest[] = [];
  isLoading: boolean = false;
  errorMessage: string = '';

  constructor(
    private router: Router,
    private vacationLeaderRequestsService: VacationLeaderRequestsService
  ) { }

  ngOnInit(): void {
    this.loadRequests();
  }

  loadRequests(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.vacationLeaderRequestsService.getRequests().subscribe({
      next: (data) => {
        this.vacationRequests = data;
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

  viewRequest(request: VacationRequest): void {
    this.router.navigate(['/vacation-leader/detail', request.id]);
  }

  reloadRequests(): void {
    this.loadRequests();
  }
}
