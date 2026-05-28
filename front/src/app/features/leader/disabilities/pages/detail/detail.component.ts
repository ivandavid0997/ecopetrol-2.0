import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DisabilitiesLeaderRequestsService } from 'src/app/core/services/disabilities-leader-requests.service';
import { SnackbarType } from 'src/app/core/components/snackbar/snackbar.component';

@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.css']
})
export class DetailComponent implements OnInit {
  isSidebarClosed: boolean = false;
  detailData: any = null;
  isLoading: boolean = false;
  actionLoading: 'approve' | 'reject' | null = null;
  errorMessage: string = '';

  // Snackbar state
  snackbarMessage: string = '';
  snackbarType: SnackbarType = 'info';
  showSnackbar: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private service: DisabilitiesLeaderRequestsService
  ) { }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.isLoading = true;
    this.service.getRequests().subscribe({
      next: (items) => {
        this.detailData = items.find(r => r.id === id) || null;
        this.isLoading = false;
        if (!this.detailData) this.errorMessage = 'Solicitud no encontrada.';
      },
      error: (err) => {
        this.errorMessage = 'Error al cargar la solicitud: ' + err;
        this.isLoading = false;
      }
    });
  }

  toggleSidebar(): void {
    this.isSidebarClosed = !this.isSidebarClosed;
  }

  onBack(): void {
    this.router.navigate(['/disabilities-leader']);
  }

  onDownloadFile(): void {
    if (this.detailData?.attachedFileName) {
      console.log(`Descargando: ${this.detailData.attachedFileName}`);
    }
  }

  onApprove(): void {
    if (!this.detailData || this.actionLoading) return;

    this.actionLoading = 'approve';

    this.service.approve(this.detailData.id).subscribe({
      next: () => {
        this.detailData.status = 'Aprobado';
        this.openSnackbar('La solicitud fue aprobada correctamente.', 'success');
        this.actionLoading = null;
      },
      error: (err) => {
        this.openSnackbar(err || 'Error al aprobar la solicitud.', 'error');
        this.actionLoading = null;
      }
    });
  }

  onReject(): void {
    if (!this.detailData || this.actionLoading) return;

    this.actionLoading = 'reject';

    this.service.reject(this.detailData.id).subscribe({
      next: () => {
        this.detailData.status = 'Rechazado';
        this.openSnackbar('La solicitud fue rechazada correctamente.', 'success');
        this.actionLoading = null;
      },
      error: (err) => {
        this.openSnackbar(err || 'Error al rechazar la solicitud.', 'error');
        this.actionLoading = null;
      }
    });
  }

  openSnackbar(message: string, type: SnackbarType): void {
    this.snackbarMessage = message;
    this.snackbarType = type;
    this.showSnackbar = true;
  }

  onSnackbarClosed(): void {
    this.showSnackbar = false;
  }
}
