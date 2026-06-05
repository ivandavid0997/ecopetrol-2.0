package com.nttdata.ecopetrol.talento.config;

import com.nttdata.ecopetrol.talento.dto.request.CierreMesRequestDTO;
import com.nttdata.ecopetrol.talento.dto.request.CierreMesResultadoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@Service
public class NominaApiClient {

    private static final Logger logger = LoggerFactory.getLogger(NominaApiClient.class);

    @Value("${nomina.api.url:http://52.233.91.10:8888/api/nomina/cierreMes}")
    private String nominaEndpointUrl;

    @Value("${nomina.api.key:bS7Gz2HkX6NaP9aQrLvEw5jFu6VmY1T}")
    private String nominaApiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public CierreMesResultadoDTO enviarCierreMes(String tipo, Long id, String nombreEmpleado) {
        CierreMesRequestDTO dto = new CierreMesRequestDTO();
        dto.setTipo(tipo);
        dto.setId(id);
        dto.setNombreEmpleado(nombreEmpleado);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-KEY", nominaApiKey);

        HttpEntity<CierreMesRequestDTO> requestEntity = new HttpEntity<>(dto, headers);

        try {
            return restTemplate.postForObject(
                    nominaEndpointUrl,
                    requestEntity,
                    CierreMesResultadoDTO.class
            );
        } catch (HttpStatusCodeException ex) {
            logger.error("Error HTTP al llamar a nómina: status={}, body={}", ex.getStatusCode(), ex.getResponseBodyAsString());
            CierreMesResultadoDTO error = new CierreMesResultadoDTO();
            error.setTipoSolicitud(tipo);
            error.setId(id);
            error.setNombreEmpleado(nombreEmpleado);
            error.setEstadoEnvio("FALLIDO");
            error.setMensaje("Error remoto: " + ex.getStatusText());
            return error;
        } catch (Exception ex) {
            logger.error("Error inesperado en API de nómina", ex);
            CierreMesResultadoDTO error = new CierreMesResultadoDTO();
            error.setTipoSolicitud(tipo);
            error.setId(id);
            error.setNombreEmpleado(nombreEmpleado);
            error.setEstadoEnvio("FALLIDO");
            error.setMensaje("Error de comunicación con nómina");
            return error;
        }
    }
}