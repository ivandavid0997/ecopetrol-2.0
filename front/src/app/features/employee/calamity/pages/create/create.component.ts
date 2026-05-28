import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CalamityRequestService } from 'src/app/core/services/calamity-request.service';

interface EmployeeMock {
  number: string;
  name: string;
  businessUnit: string;
  leader: string;
}

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.css']
})
export class CreateComponent implements OnInit {
  isSidebarClosed: boolean = false;

  calamityForm!: FormGroup;
  totalDays: number = 0;
  attachedFileName: string = 'No hay nada adjunto.';
  selectedFile: File | null = null;

  employeeList: EmployeeMock[] = [
    { number: '556781', name: 'Carlos Mendoza', businessUnit: 'Digital Strategy & Business', leader: 'Laura Gómez' },
    { number: '902314', name: 'Ana María Silva', businessUnit: 'Digital Strategy & Business', leader: 'Sergio Torres' },
    { number: '778920', name: 'Juan Pérez', businessUnit: 'Desarrollo Angular', leader: 'Andrés Felipe Restrepo' },
    { number: '641275', name: 'Mariana López', businessUnit: 'Diseño UI/UX', leader: 'Laura Gómez' },
    { number: '830492', name: 'Felipe Ramírez', businessUnit: 'Desarrollo Angular', leader: 'Sergio Torres' },
    { number: '715903', name: 'Camila Torres', businessUnit: 'Digital Strategy & Business', leader: 'Andrés Felipe Restrepo' },
    { number: '964120', name: 'Sebastián Herrera', businessUnit: 'Diseño UI/UX', leader: 'Laura Gómez' },
    { number: '583746', name: 'Valentina Castro', businessUnit: 'Desarrollo Angular', leader: 'Sergio Torres' }
  ];

  businessUnits: string[] = ['Digital Strategy & Business', 'Desarrollo Angular', 'Diseño UI/UX'];
  leaders: string[] = ['Laura Gómez', 'Sergio Torres', 'Andrés Felipe Restrepo'];

  isLoading: boolean = false;
  successMessage: string = '';
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private calamityRequestService: CalamityRequestService
  ) { }

  ngOnInit(): void {
    this.initForm();
    this.subscribeToChanges();
  }

  toggleSidebar(): void {
    this.isSidebarClosed = !this.isSidebarClosed;
  }

  private initForm(): void {
    this.calamityForm = this.fb.group({
      employeeNumber: ['', Validators.required],
      employeeName: [{ value: '', disabled: true }, Validators.required],
      businessUnit: ['Digital Strategy & Business', Validators.required], // Valor por defecto de la imagen
      leaderName: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      comments: [''],
      document: [null] // Opcional para calamidades según la imagen
    });
  }

  private subscribeToChanges(): void {
    // Escucha cambios del número de empleado para autocompletar nombre
    this.calamityForm.get('employeeNumber')?.valueChanges.subscribe((num: string) => {
      const emp = this.employeeList.find(e => e.number === num);
      if (emp) {
        this.calamityForm.patchValue({
          employeeName: emp.name,
          businessUnit: emp.businessUnit,
          leaderName: emp.leader
        });
      } else {
        this.calamityForm.patchValue({ employeeName: '', leaderName: '' });
      }
    });

    // Cómputo dinámico de días transcurridos
    this.calamityForm.valueChanges.subscribe(values => {
      const { startDate, endDate } = values;
      if (startDate && endDate) {
        const start = new Date(startDate);
        const end = new Date(endDate);
        const diffTime = end.getTime() - start.getTime();
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;
        this.totalDays = diffDays > 0 ? diffDays : 0;
      } else {
        this.totalDays = 0;
      }
    });
  }

  onFileChange(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      this.attachedFileName = file.name;
      this.calamityForm.patchValue({ document: file });
    }
  }

  onGoBack(): void {
    this.router.navigate(['/calamity']); // Ruta de retorno al historial
  }

  onSubmit(): void {
    if (this.calamityForm.invalid || this.totalDays === 0) return;

    this.successMessage = '';
    this.errorMessage = '';

    const payload = {
      ...this.calamityForm.getRawValue(),
      totalDays: this.totalDays,
      fileName: this.selectedFile ? this.selectedFile.name : null
    };

    this.isLoading = true;
    this.calamityForm.disable();

    this.calamityRequestService.createCalamityRequest(payload).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.calamityForm.enable();
        this.successMessage = 'Solicitud de Calamidad enviada exitosamente.';
        setTimeout(() => {
          this.successMessage = '';
          this.onGoBack();
        }, 1800);
      },
      error: (err) => {
        this.isLoading = false;
        this.calamityForm.enable();
        this.errorMessage = 'Error al enviar la solicitud: ' + err;
        setTimeout(() => { this.errorMessage = ''; }, 3000);
      }
    });
  }
}
