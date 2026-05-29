package com.nttdata.ecopetrol.talento.services;

import com.nttdata.ecopetrol.talento.dto.request.CierreMesResultadoDTO;
import com.nttdata.ecopetrol.talento.model.SolicitudUnificada;

import java.util.List;

public interface AdministracionService {
    List<SolicitudUnificada> listarConsolidadoSolicitud();
    List<CierreMesResultadoDTO> cierreMes();
}
