package com.nttdata.ecopetrol.talento.services.impl;

import com.nttdata.ecopetrol.talento.config.NominaApiClient;
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
    private final NominaApiClient nominaApiClient;

    public AdministracionServiceImpl(SolicitudUnificadaRepository solicitudUnificadaRepository, VacacionesRepository vacacionesRepository, IncapacidadRepository incapacidadRepository, CalamidadRepository calamidadRepository, DiaCumpleanioRepository diaCumpleanioRepository, NominaApiClient nominaApiClient) {
        this.solicitudUnificadaRepository = solicitudUnificadaRepository;
        this.vacacionesRepository = vacacionesRepository;
        this.incapacidadRepository = incapacidadRepository;
        this.calamidadRepository = calamidadRepository;
        this.diaCumpleanioRepository = diaCumpleanioRepository;
        this.nominaApiClient = nominaApiClient;
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

        vacacionesRepository.findAll()
                .stream().filter(v -> "APROBADA".equalsIgnoreCase(String.valueOf(v.getEstado())))
                .forEach(v -> {
                    resultados.add(nominaApiClient.enviarCierreMes("VACACIONES", v.getId(), v.getNombreEmpleado()));
                });

        incapacidadRepository.findAll()
                .stream().filter(i -> "APROBADA".equalsIgnoreCase(String.valueOf(i.getEstado())))
                .forEach(i -> {
                    resultados.add(nominaApiClient.enviarCierreMes("INCAPACIDAD", i.getId(), i.getNombreEmpleado()));
                });

        calamidadRepository.findAll()
                .stream().filter(c -> "APROBADA".equalsIgnoreCase(String.valueOf(c.getEstado())))
                .forEach(c -> {
                    resultados.add(nominaApiClient.enviarCierreMes("CALAMIDAD", c.getId(), c.getNombreEmpleado()));
                });

        diaCumpleanioRepository.findAll()
                .stream().filter(d -> "APROBADA".equalsIgnoreCase(String.valueOf(d.getEstado())))
                .forEach(d -> {
                    resultados.add(nominaApiClient.enviarCierreMes("DIA_CUMPLEANIO", d.getId(), d.getNombreEmpleado()));
                });

        return resultados;
    }
}

