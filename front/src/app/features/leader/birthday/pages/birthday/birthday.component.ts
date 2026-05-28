import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BirthdayLeaderRequestsService } from 'src/app/core/services/birthday-leader-requests.service';

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
  selector: 'app-birthday',
  templateUrl: './birthday.component.html',
  styleUrls: ['./birthday.component.css']
})
export class BirthdayComponent implements OnInit {
  // Estado del menú lateral colapsable
  isSidebarClosed: boolean = false;

  vacationRequests: VacationRequest[] = [];
  isLoading: boolean = false;
  errorMessage: string = '';

  constructor(
    private router: Router,
    private birthdayLeaderRequestsService: BirthdayLeaderRequestsService
  ) { }

  ngOnInit(): void {
    this.loadRequests();
  }

  loadRequests(): void {
    this.isLoading = true;
    this.errorMessage = '';
    this.birthdayLeaderRequestsService.getRequests().subscribe({
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
    this.router.navigate(['/birthday-leader/detail', request.id]);
  }

  reloadRequests(): void {
    this.loadRequests();
  }
}
