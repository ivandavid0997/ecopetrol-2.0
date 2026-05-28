import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { DisabilitiesRequestService } from 'src/app/core/services/disabilities-request.service';

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

  incapacityForm!: FormGroup;
  totalDays: number = 0;
  attachedFileName: string = 'No hay nada adjunto.';
  selectedFile: File | null = null;

  // Catálogos simulados
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

  businessUnits: string[] = ['Desarrollo Angular', 'Diseño UI/UX', 'Aseguramiento de Calidad'];
  leaders: string[] = ['Laura Gómez', 'Sergio Torres'];
  incapacityTypes: string[] = ['Enfermedad General', 'Accidente de Trabajo', 'Maternidad / Paternidad'];
  healthEntities: string[] = ['EPS Sura', 'Sanitas', 'Compensar', 'Nueva EPS'];
  categories: string[] = ['Incapacidad Inicial', 'Prórroga'];

  isLoading: boolean = false;
  successMessage: string = '';
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private disabilitiesRequestService: DisabilitiesRequestService
  ) { }

  ngOnInit(): void {
    this.initForm();
    this.subscribeToChanges();
  }

  toggleSidebar(): void {
    this.isSidebarClosed = !this.isSidebarClosed;
  }

  private initForm(): void {
    this.incapacityForm = this.fb.group({
      employeeNumber: ['', Validators.required],
      employeeName: [{ value: '', disabled: true }, Validators.required],
      businessUnit: ['', Validators.required],
      leaderName: ['', Validators.required],
      incapacityType: ['', Validators.required],
      healthEntity: ['', Validators.required],
      category: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      diagnosis: ['', Validators.required],
      document: [null, Validators.required] // El archivo es estrictamente obligatorio
    });
  }

  private subscribeToChanges(): void {
    // Autocompletado por número de empleado
    this.incapacityForm.get('employeeNumber')?.valueChanges.subscribe((num: string) => {
      const emp = this.employeeList.find(e => e.number === num);
      if (emp) {
        this.incapacityForm.patchValue({
          employeeName: emp.name,
          businessUnit: emp.businessUnit,
          leaderName: emp.leader
        });
      } else {
        this.incapacityForm.patchValue({ employeeName: '', businessUnit: '', leaderName: '' });
      }
    });

    // Cálculo dinámico de días transcurridos
    this.incapacityForm.valueChanges.subscribe(values => {
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

  // Captura el archivo seleccionado desde el input oculto
  onFileChange(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      this.attachedFileName = file.name;
      this.incapacityForm.patchValue({ document: file });
      this.incapacityForm.get('document')?.updateValueAndValidity();
    }
  }

  onGoBack(): void {
    this.router.navigate(['/disabilities']); // Tu ruta base de historial
  }

  onSubmit(): void {
    if (this.incapacityForm.invalid || this.totalDays === 0) return;

    this.successMessage = '';
    this.errorMessage = '';

    // Construcción del objeto unificado incluyendo la métrica calculada de días
    const formValues = this.incapacityForm.getRawValue();
    const payload = {
      ...formValues,
      totalDays: this.totalDays,
      fileReference: this.selectedFile?.name || ''
    };

    if (this.selectedFile) {
      this.isLoading = true;
      this.incapacityForm.disable();
      this.fileToBase64(this.selectedFile).then((base64: string) => {
        payload.documentBase64 = base64;
        this.sendRequest(payload);
      }).catch(() => {
        this.isLoading = false;
        this.incapacityForm.enable();
        this.errorMessage = 'Error al procesar el archivo PDF.';
        setTimeout(() => { this.errorMessage = ''; }, 3000);
      });
    } else {
      this.errorMessage = 'Debe adjuntar un documento PDF.';
      setTimeout(() => { this.errorMessage = ''; }, 3000);
    }
  }

  private fileToBase64(file: File): Promise<string> {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        // El resultado es data:application/pdf;base64,xxxxxx
        const base64 = (reader.result as string).split(',')[1];
        resolve(base64);
      };
      reader.onerror = error => reject(error);
    });
  }

  private sendRequest(payload: any): void {
    this.disabilitiesRequestService.createDisabilitiesRequest(payload).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.successMessage = 'Solicitud de incapacidad enviada exitosamente.';
        setTimeout(() => {
          this.successMessage = '';
          this.onGoBack();
        }, 1800);
      },
      error: (err) => {
        this.isLoading = false;
        this.incapacityForm.enable();
        this.errorMessage = 'Error al enviar la solicitud: ' + err;
        setTimeout(() => { this.errorMessage = ''; }, 3000);
      }
    });
  }
}
