import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-rol',
  templateUrl: './rol.component.html',
  styleUrls: ['./rol.component.css']
})
export class RolComponent {
  empleadoEnabled = false;
  liderEnabled = false;
  peopleEnabled = false;

  constructor(private router: Router) {
    const userStr = sessionStorage.getItem('User');

    if (userStr) {
      try {
        const user = JSON.parse(userStr);

        if (!user || !user.rol) {
          return;
        }

        const role = String(user.rol).toLowerCase();

        if (role === 'empleado') {
          this.empleadoEnabled = true;
        } else if (role === 'lider') {
          this.empleadoEnabled = true;
          this.liderEnabled = true;
        } else if (role === 'people') {
          this.empleadoEnabled = true;
          this.liderEnabled = true;
          this.peopleEnabled = true;
        }
      } catch (e) {
        console.error('Error parsing User from sessionStorage', e);
      }
    }
  }

  selectRole(role: string): void {
    console.log(`Rol seleccionado: ${role}`);
    this.router.navigate(['/' + `${role}`]);
  }
}
