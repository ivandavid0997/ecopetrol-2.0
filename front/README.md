# Proyecto Angular Monolítico - Gestión de Solicitudes

Este proyecto es una aplicación monolítica desarrollada en **Angular 14** para la gestión de solicitudes de empleados y líderes, incluyendo vacaciones, incapacidades, calamidades y cumpleaños. El sistema está diseñado para facilitar tanto el consumo de servicios reales como el uso de datos simulados (mocks) para pruebas y desarrollo rápido.

## Descripción General

- **Framework:** Angular 14
- **Arquitectura:** Monolítica, modularizada por features
- **Roles:** Employee (Empleado) y Leader (Líder)
- **Funcionalidades:** Gestión de solicitudes de vacaciones, incapacidades, calamidades y cumpleaños, tanto para empleados como para líderes.
- **Soporte de Mocks:** Permite alternar entre consumo de API real y archivos mock JSON locales mediante configuración en environments.
- **UI:** Tablas responsivas, loading animado, manejo de errores, estados visuales diferenciados.

## Estructura de Carpetas

```
src/
├── app/
│   ├── core/
│   │   └── services/                # Servicios centralizados (por feature y rol)
│   ├── features/
│   │   ├── employee/
│   │   │   ├── vacation/
│   │   │   ├── disabilities/
│   │   │   ├── calamity/
│   │   │   └── birthday/
│   │   └── leader/
│   │       ├── vacation/
│   │       ├── disabilities/
│   │       ├── calamity/
│   │       └── birthday/
│   └── ...
├── assets/
│   └── mocks/                       # Archivos JSON de mocks por feature y rol
├── environments/
│   ├── environment.ts               # Configuración local (dev)
│   └── environment.prod.ts          # Configuración producción
└── ...
```

## Uso de Environments y Mocks

- **Alternancia entre API real y mocks:**  
  En los archivos `src/environments/environment.ts` y `src/environments/environment.prod.ts` se define la variable `useMock`.  
  - `useMock: true` → El sistema consume archivos JSON locales desde `assets/mocks/`.
  - `useMock: false` → El sistema consume los endpoints reales definidos en `apiUrl` y `endpoint`.

- **Configuración de endpoints y mocks:**  
  Cada feature y rol tiene su propio endpoint y mockUrl configurados en los environments, por ejemplo:
  ```ts
  mockUrl: {
    vacationLeader: 'assets/mocks/vacation-leader-mock.json',
    disabilitiesLeader: 'assets/mocks/disabilities-leader-mock.json',
    ...
  },
  endpoint: {
    vacationLeader: '/vacation-leader/requests',
    disabilitiesLeader: '/disabilities-leader/requests',
    ...
  }
  ```

## Servicios

Los servicios de cada feature y rol (por ejemplo, `VacationLeaderRequestsService`) se encuentran en `src/app/core/services/` y son responsables de consumir los endpoints reales o los archivos mock según la configuración del environment.

## Loading y Estados Visuales

- Cada tabla muestra un loader animado mientras se cargan los datos (`isLoading`).
- Los estados de las solicitudes (Aprobado, Pendiente, Rechazado) se visualizan con colores diferenciados mediante clases CSS.
- El manejo de errores se muestra en la UI de forma clara.

## Cómo Correr el Proyecto

1. Instala las dependencias:
   ```bash
   npm install
   ```
2. Corre el servidor de desarrollo:
   ```bash
   ng serve
   ```
3. Accede a la aplicación en [http://localhost:4200](http://localhost:4200)

> **Nota:** Para alternar entre mocks y API real, edita la variable `useMock` en `src/environments/environment.ts`.

