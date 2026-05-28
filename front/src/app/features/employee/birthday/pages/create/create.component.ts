import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { BirthdayRequestService } from 'src/app/core/services/birthday-request.service';

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

  birthdayForm!: FormGroup;

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
    private birthdayRequestService: BirthdayRequestService
  ) { }

  ngOnInit(): void {
    this.initForm();
    this.subscribeToEmployeeChanges();
  }

  toggleSidebar(): void {
    this.isSidebarClosed = !this.isSidebarClosed;
  }

  private initForm(): void {
    this.birthdayForm = this.fb.group({
      employeeNumber: ['', Validators.required],
      employeeName: [{ value: '', disabled: true }, Validators.required],
      businessUnit: ['Digital Strategy & Business', Validators.required], // Valor por defecto de la imagen
      leaderName: ['', Validators.required],
      startDate: ['', Validators.required], // Representa el único día de disfrute
      comments: ['']
    });
  }

  private subscribeToEmployeeChanges(): void {
    this.birthdayForm.get('employeeNumber')?.valueChanges.subscribe((num: string) => {
      const emp = this.employeeList.find(e => e.number === num);
      if (emp) {
        this.birthdayForm.patchValue({
          employeeName: emp.name,
          businessUnit: emp.businessUnit,
          leaderName: emp.leader
        });
      } else {
        this.birthdayForm.patchValue({ employeeName: '', leaderName: '' });
      }
    });
  }

  onGoBack(): void {
    this.router.navigate(['/birthday']); // Redirige al módulo histórico base
  }

  onSubmit(): void {
    if (this.birthdayForm.invalid || this.isLoading) return;

    this.successMessage = '';
    this.errorMessage = '';

    // Al ser un beneficio de un único día, fijamos totalDays en 1 de forma estricta para el backend
    const payload = {
      ...this.birthdayForm.getRawValue(),
      endDate: this.birthdayForm.getRawValue().startDate, // Fecha fin es el mismo día
      totalDays: 1
    };

    this.isLoading = true;
    this.birthdayForm.disable();

    this.birthdayRequestService.createBirthdayRequest(payload).subscribe({
      next: (response) => {
        this.isLoading = false;
        this.birthdayForm.enable();

        this.successMessage = 'Solicitud de Día de Cumpleaños enviada exitosamente.';
        setTimeout(() => {
          this.successMessage = '';
          this.onGoBack();
        }, 1800);
      },
      error: (err) => {
        this.isLoading = false;
        this.birthdayForm.enable();

        this.errorMessage = 'Error al enviar la solicitud: ' + err;
        setTimeout(() => { this.errorMessage = ''; }, 3000);
      }
    });
  }
}
