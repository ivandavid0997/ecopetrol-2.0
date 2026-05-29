package com.nttdata.ecopetrol.talento.services.impl;

import com.nttdata.ecopetrol.talento.dto.request.CierreMesResultadoDTO;
import com.nttdata.ecopetrol.talento.model.*;
import com.nttdata.ecopetrol.talento.repository.*;
import com.nttdata.ecopetrol.talento.services.AdministracionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class AdministracionServiceImpl implements AdministracionService {

    private static final Logger logger = LoggerFactory.getLogger(AdministracionServiceImpl.class);

    private final SolicitudUnificadaRepository solicitudUnificadaRepository;
    private final VacacionesRepository vacacionesRepository;
    private final IncapacidadRepository incapacidadRepository;
    private final CalamidadRepository calamidadRepository;
    private final DiaCumpleanioRepository diaCumpleanioRepository;

    public AdministracionServiceImpl(SolicitudUnificadaRepository solicitudUnificadaRepository, VacacionesRepository vacacionesRepository, IncapacidadRepository incapacidadRepository, CalamidadRepository calamidadRepository, DiaCumpleanioRepository diaCumpleanioRepository) {
        this.solicitudUnificadaRepository = solicitudUnificadaRepository;
        this.vacacionesRepository = vacacionesRepository;
        this.incapacidadRepository = incapacidadRepository;
        this.calamidadRepository = calamidadRepository;
        this.diaCumpleanioRepository = diaCumpleanioRepository;
    }

    @Override
    public List<SolicitudUnificada> listarConsolidadoSolicitud() {
        logger.info("Listando consolidado de solicitudes unificadas con delay aleatorio");
        return solicitudUnificadaRepository.findAll();
    }

    @Override
    public List<CierreMesResultadoDTO> cierreMes() {
        logger.info("Iniciando proceso de cierre de mes y envío a nómina (mock)");

        List<CierreMesResultadoDTO> resultados = new ArrayList<>();

        List<Vacaciones> vacacionesAprobadas = vacacionesRepository.findAll()
                .stream().filter(v -> "APROBADA".equalsIgnoreCase(String.valueOf(v.getEstado()))).toList();
        for (Vacaciones v : vacacionesAprobadas) {
            resultados.add(simularEnvioNomina("VACACIONES", v.getId(), v.getNombreEmpleado()));
        }

        List<Incapacidad> incapacidadAprobadas = incapacidadRepository.findAll()
                .stream().filter(i -> "APROBADA".equalsIgnoreCase(String.valueOf(i.getEstado()))).toList();
        for (Incapacidad i : incapacidadAprobadas) {
            resultados.add(simularEnvioNomina("INCAPACIDAD", i.getId(), i.getNombreEmpleado()));
        }

        List<Calamidad> calamidadAprobadas = calamidadRepository.findAll()
                .stream().filter(c -> "APROBADA".equalsIgnoreCase(String.valueOf(c.getEstado()))).toList();
        for (Calamidad c : calamidadAprobadas) {
            resultados.add(simularEnvioNomina("CALAMIDAD", c.getId(), c.getNombreEmpleado()));
        }

        List<DiaCumpleanio> cumpleAprobados = diaCumpleanioRepository.findAll()
                .stream().filter(d -> "APROBADA".equalsIgnoreCase(String.valueOf(d.getEstado()))).toList();
        for (DiaCumpleanio d : cumpleAprobados) {
            resultados.add(simularEnvioNomina("DIA_CUMPLEANIO", d.getId(), d.getNombreEmpleado()));
        }

        return resultados;
    }

    private CierreMesResultadoDTO simularEnvioNomina(String tipo, Long id, String nombreEmpleado) {
        Random random = new Random();
        boolean exito = random.nextBoolean();

        CierreMesResultadoDTO dto = new CierreMesResultadoDTO();
        dto.setTipoSolicitud(tipo);
        dto.setId(id);
        dto.setNombreEmpleado(nombreEmpleado);

            if (exito) {
                dto.setEstadoEnvio("EXITOSO");
                dto.setMensaje("Enviado correctamente a nómina");
                logger.info("Solicitud {} ID {} enviada a nómina correctamente", tipo, id);
            } else {
                dto.setEstadoEnvio("FALLIDO");
                dto.setMensaje("Error al enviar solicitud al sistema de nómina (simulado). ");
                logger.warn("Solicitud {} ID {} falló en envío a nómina (mock)", tipo, id);
            }

        return dto;
    }
}

