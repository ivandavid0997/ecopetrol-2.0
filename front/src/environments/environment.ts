import { TipoPermiso } from '../app/core/models';

/**
 * Config de entorno. `useMock: true` usa datos en memoria (demo sin backend).
 * `useMock: false` consume el backend real vía el proxy (/autenticacion, /vacaciones, ...).
 */
export const environment = {
  useMock: false,
  api: {
    login: '/autenticacion/login',
    cierreMes: '/administracion/cierreMes',
    usuarios: '/usuario/listaUsuarios',
  },
};

/** Endpoints por tipo de permiso (feature-prefixed en el backend). */
export const API_TIPO: Record<TipoPermiso, { listar: string; crear: string; aprobar: string; rechazar: string; borrar: string }> = {
  vacaciones: {
    listar: '/vacaciones/listarVacaciones',
    crear: '/vacaciones/crearVacaciones',
    aprobar: '/vacaciones/aprobarVacaciones',
    rechazar: '/vacaciones/rechazarVacaciones',
    borrar: '/vacaciones/borrarVacaciones',
  },
  incapacidad: {
    listar: '/incapacidad/listarIncapacidades',
    crear: '/incapacidad/crearIncapacidad',
    aprobar: '/incapacidad/aprobarIncapacidad',
    rechazar: '/incapacidad/rechazarIncapacidad',
    borrar: '/incapacidad/borrarIncapacidad',
  },
  calamidad: {
    listar: '/calamidad/listarCalamidades',
    crear: '/calamidad/crearCalamidad',
    aprobar: '/calamidad/aprobarCalamidad',
    rechazar: '/calamidad/rechazarCalamidad',
    borrar: '/calamidad/borrarCalamidad',
  },
  cumpleanos: {
    listar: '/cumpleanio/listarDiasCumpleanios',
    crear: '/cumpleanio/crearDiaCumpleanio',
    aprobar: '/cumpleanio/aprobarDiaCumpleanio',
    rechazar: '/cumpleanio/rechazarDiaCumpleanio',
    borrar: '/cumpleanio/borrarDiaCumpleanio',
  },
};
