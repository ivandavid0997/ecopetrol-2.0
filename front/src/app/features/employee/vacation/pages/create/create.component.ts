import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { VacationRequestService } from 'src/app/core/services/vacation-request.service';

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

  vacationForm!: FormGroup;
  totalDays: number = 0;
  isLoading: boolean = false;
  successMessage: string = '';
  errorMessage: string = '';

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private vacationRequestService: VacationRequestService
  ) { }

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

  businessUnits: string[] = ['Desarrollo Angular', 'Diseño UI/UX', 'Aseguramiento de Calidad', 'Célula de Innovación'];
  leaders: string[] = ['Laura Gómez', 'Sergio Torres', 'Andrés Felipe Restrepo']

  ngOnInit(): void {
    this.initForm();
    this.subscribeToFormChanges();
  }

  toggleSidebar(): void {
    this.isSidebarClosed = !this.isSidebarClosed;
  }

  private initForm(): void {
    this.vacationForm = this.fb.group({
      employeeNumber: ['', Validators.required],
      employeeName: [{ value: '', disabled: true }, Validators.required],
      businessUnit: ['', Validators.required],
      leaderName: ['', Validators.required],
      startDate: ['', Validators.required],
      endDate: ['', Validators.required],
      comments: ['']
    });
  }

  private subscribeToFormChanges(): void {
    this.vacationForm.get('employeeNumber')?.valueChanges.subscribe((selectedNumber: string) => {
      const foundEmployee = this.employeeList.find(emp => emp.number === selectedNumber);

      if (foundEmployee) {
        this.vacationForm.patchValue({
          employeeName: foundEmployee.name,
          businessUnit: foundEmployee.businessUnit,
          leaderName: foundEmployee.leader
        });
      } else {
        this.vacationForm.patchValue({ employeeName: '', businessUnit: '', leaderName: '' });
      }
    });

    this.vacationForm.valueChanges.subscribe(values => {
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

  onGoBack(): void {
    this.router.navigate(['/vacation']);
  }

  onSubmit(): void {
    if (this.vacationForm.invalid || this.totalDays === 0) {
      return;
    }

    this.successMessage = '';
    this.errorMessage = '';

    const formValues = this.vacationForm.getRawValue();

    const payloadToBackend = {
      ...formValues,
      totalDays: this.totalDays
    };

    this.isLoading = true;
    this.vacationForm.disable();
    this.vacationRequestService.createVacationRequest(payloadToBackend).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.vacationForm.enable();
        this.successMessage = 'Solicitud de vacaciones enviada exitosamente.';
        setTimeout(() => {
          this.successMessage = '';
          this.onGoBack();
        }, 1800);
      },
      error: (err) => {
        this.isLoading = false;
        this.vacationForm.enable();
        this.errorMessage = 'Error al enviar la solicitud: ' + err;
        setTimeout(() => {
          this.errorMessage = '';
        }, 3000);
      }
    });
  }

}
