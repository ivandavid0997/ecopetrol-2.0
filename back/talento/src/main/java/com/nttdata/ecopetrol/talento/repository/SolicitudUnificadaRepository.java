package com.nttdata.ecopetrol.talento.repository;

import com.nttdata.ecopetrol.talento.model.SolicitudUnificada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SolicitudUnificadaRepository extends JpaRepository<SolicitudUnificada, Long> {

}
