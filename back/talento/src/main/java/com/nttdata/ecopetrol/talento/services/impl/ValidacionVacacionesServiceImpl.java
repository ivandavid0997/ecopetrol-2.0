package com.nttdata.ecopetrol.talento.services.impl;

import com.nttdata.ecopetrol.talento.model.Calamidad;
import com.nttdata.ecopetrol.talento.model.DiaCumpleanio;
import com.nttdata.ecopetrol.talento.model.Incapacidad;
import com.nttdata.ecopetrol.talento.model.Vacaciones;
import com.nttdata.ecopetrol.talento.services.ValidacionVacacionesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ValidacionVacacionesServiceImpl implements ValidacionVacacionesService {

    private static final Logger logger = LoggerFactory.getLogger(ValidacionVacacionesServiceImpl.class);

    // Método para vacaciones
    public boolean validarDiasVacacionesDisponibles(Vacaciones vacaciones) throws InterruptedException {
        logger.info("Iniciando simulación de consulta de días disponibles para empleado {}", vacaciones.getNumeroEmpleado());
        int diasDisponibles = obtenerDiasDisponibles(vacaciones.getNumeroEmpleado());
        logger.info("Días disponibles recuperados para empleado {}: {}", vacaciones.getNumeroEmpleado(), diasDisponibles);
        boolean resultado = vacaciones.getTotalDias() <= diasDisponibles;
        logger.info("Resultado validación empleado {}: solicitado={}, disponibles={}, resultado={}",
                vacaciones.getNumeroEmpleado(), vacaciones.getTotalDias(), diasDisponibles, resultado);
        return resultado;
    }

    // Método para incapacidad
    public boolean validarDiasIncapacidadDisponibles(Incapacidad incapacidad) throws InterruptedException {
        logger.info("Iniciando validación de incapacidad para empleado {} (solicitado: {})",
                incapacidad.getNumeroEmpleado(), incapacidad.getTotalDias());

        int diasDisponibles = obtenerDiasDisponibles(incapacidad.getNumeroEmpleado());
        logger.info("Incapacidad - Días disponibles recuperados para empleado {}: {}",
                incapacidad.getNumeroEmpleado(), diasDisponibles);

        boolean resultado = incapacidad.getTotalDias() <= diasDisponibles;
        logger.info("Resultado validación incapacidad empleado {}: solicitado={}, disponibles={}, resultado:{}",
                incapacidad.getNumeroEmpleado(), incapacidad.getTotalDias(), diasDisponibles, resultado);

        return resultado;
    }

    // Método para calamidad
    public boolean validarDiasCalamidadDisponibles(Calamidad calamidad) throws InterruptedException {
        logger.info("Iniciando validación de calamidad para empleado {} (solicitado: {})",
                calamidad.getNumeroEmpleado(), calamidad.getTotalDias());

        int diasDisponibles = obtenerDiasDisponibles(calamidad.getNumeroEmpleado());
        logger.info("Calamidad - Días disponibles recuperados para empleado {}: {}",
                calamidad.getNumeroEmpleado(), diasDisponibles);

        boolean resultado = calamidad.getTotalDias() <= diasDisponibles;
        logger.info("Resultado validación calamidad empleado {}: solicitado={}, disponibles={}, resultado:{}",
                calamidad.getNumeroEmpleado(), calamidad.getTotalDias(), diasDisponibles, resultado);

        return resultado;
    }

    // Método para días de cumpleaños
    public boolean validarDiasCumpleanioDisponibles(DiaCumpleanio diaCumpleanio) throws InterruptedException {
        logger.info("Iniciando validación de día cumpleaños para empleado {}", diaCumpleanio.getNumeroEmpleado());

        int diasDisponibles = obtenerDiasDisponibles(diaCumpleanio.getNumeroEmpleado());
        logger.info("Día cumpleaños - Días disponibles recuperados para empleado {}: {}",
                diaCumpleanio.getNumeroEmpleado(), diasDisponibles);

        boolean resultado = diasDisponibles >= 1;
        logger.info("Resultado validación día cumpleaños empleado {}: disponibles={}, resultado:{}",
                diaCumpleanio.getNumeroEmpleado(), diasDisponibles, resultado);

        return resultado;
    }

    private int obtenerDiasDisponibles(String numeroEmpleado) {
        // Número aleatorio entre 1 y 20
        return (int) (Math.random() * 20) + 1;
    }

}
