import { Component, OnInit } from '@angular/core';
import { PeopleRequestsService } from 'src/app/core/services/people-requests.service';
import { PeopleReportService } from 'src/app/core/services/people-report.service';
import { PeopleMonthClosureService } from 'src/app/core/services/people-month-closure.service';
/* Eliminado import { saveAs } from 'file-saver'; porque no está disponible y usaremos API nativa */

@Component({
  selector: 'app-people',
  templateUrl: './people.component.html',
  styleUrls: ['./people.component.css']
})
export class PeopleComponent implements OnInit {
  isSidebarClosed: boolean = false;

  requests: any[] = [];
  isLoading: boolean = false;
  reportLoading: boolean = false;
  monthClosureLoading: boolean = false;

  // Snackbar state
  snackbarMessage: string = '';
  snackbarType: 'success' | 'error' | 'info' | 'warning' = 'info';
  showSnackbar: boolean = false;

  constructor(
    private peopleRequestsService: PeopleRequestsService,
    private peopleReportService: PeopleReportService,
    private peopleMonthClosureService: PeopleMonthClosureService
  ) {}

  ngOnInit(): void {
    this.fetchRequests();
  }

  toggleSidebar(): void {
    this.isSidebarClosed = !this.isSidebarClosed;
  }

  private fetchRequests(): void {
    this.isLoading = true;
    this.peopleRequestsService.getAllRequests().subscribe({
      next: (data) => {
        this.requests = data;
        this.isLoading = false;
        // Notifica éxito solo si venía una bandera explícita, UX no debe saturar con success para cargas normales de grilla
      },
      error: (error) => {
        console.error('Error al obtener solicitudes', error);
        this.isLoading = false;

        const msg =
          typeof error === 'string'
            ? error
            : 'Hubo un problema al obtener las solicitudes. Intenta nuevamente más tarde.';

        this.openSnackbar(msg, 'error');
      },
    });
  }

  downloadReport(): void {
    if (this.reportLoading) return;

    this.reportLoading = true;

    this.peopleReportService.downloadPeopleReportCSV().subscribe({
      next: (blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = 'reporte_solicitudes.csv';
        a.click();
        window.URL.revokeObjectURL(url);

        this.openSnackbar('Reporte descargado correctamente.', 'success');
        this.reportLoading = false;
      },
      error: (error) => {
        console.error('Error al descargar el reporte', error);

        const msg =
          typeof error === 'string'
            ? error
            : 'No fue posible descargar el reporte. Por favor intenta de nuevo.';

        this.openSnackbar(msg, 'error');
        this.reportLoading = false;
      },
    });
  }

  closeMonth(): void {
    if (this.monthClosureLoading) return;

    this.monthClosureLoading = true;

    this.peopleMonthClosureService.closeMonth().subscribe({
      next: (msg) => {
        this.openSnackbar(msg || 'Cierre de mes realizado con éxito.', 'success');
        this.fetchRequests();
        this.monthClosureLoading = false;
      },
      error: (error) => {
        console.error('Error al realizar cierre de mes', error);

        const msg =
          typeof error === 'string'
            ? error
            : 'Error inesperado al realizar el cierre de mes.';

        this.openSnackbar(msg, 'error');
        this.monthClosureLoading = false;
      }
    });
  }

  openSnackbar(message: string, type: 'success' | 'error' | 'info' | 'warning'): void {
    this.snackbarMessage = message;
    this.snackbarType = type;
    this.showSnackbar = true;
  }

  onSnackbarClosed(): void {
    this.showSnackbar = false;
  }
}
