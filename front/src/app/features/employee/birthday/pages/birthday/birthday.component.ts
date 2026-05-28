import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { BirthdayService } from 'src/app/core/services/birthday.service';

interface BirthdayRequest {
  id: number;
  cedula: string;
  collaborator: string;
  birthdayDate: string;
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

  birthdayRequests: BirthdayRequest[] = [];
  isLoading: boolean = false;

  constructor(
    private router: Router,
    private birthdayService: BirthdayService
  ) { }

  ngOnInit(): void {
    this.loadBirthdays();
  }

  loadBirthdays(): void {
    this.isLoading = true;
    this.birthdayService.getBirthdays().subscribe({
      next: (data) => {
        this.birthdayRequests = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar los cumpleaños:', err);
        this.birthdayRequests = [];
        this.isLoading = false;
      }
    });
  }

  toggleSidebar(): void {
    this.isSidebarClosed = !this.isSidebarClosed;
  }

  viewRequest(request: BirthdayRequest): void {
    console.log('Visualizando cumpleaños de:', request.collaborator);
  }

  editRequest(request: BirthdayRequest): void {
    console.log('Editando cumpleaños ID:', request.id);
  }

  createNewRequest(): void {
    console.log('Abriendo formulario de nuevo cumpleaños');
    this.router.navigate(['/birthday/create']);
  }

  reloadRequests(): void {
    console.log('Recargando el listado de cumpleaños...');
    this.loadBirthdays();
  }
}
