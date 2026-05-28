import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DisabilitiesLeaderRequestsService } from 'src/app/core/services/disabilities-leader-requests.service';

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
  isSidebarClosed: boolean = false;

  disabilitiesRequests: DisabilitiesRequest[] = [];
  isLoading: boolean = false;
  errorMessage: string = '';

  constructor(
    private router: Router,
    private disabilitiesLeaderRequestsService: DisabilitiesLeaderRequestsService
  ) { }

  ngOnInit(): void {
    this.loadRequests();
  }

  loadRequests(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.disabilitiesLeaderRequestsService.getRequests().subscribe({
      next: (data) => {
        this.disabilitiesRequests = data;
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

  viewRequest(request: DisabilitiesRequest): void {
    this.router.navigate(['/disabilities-leader/detail', request.id]);
  }

  reloadRequests(): void {
    this.loadRequests();
  }
}
