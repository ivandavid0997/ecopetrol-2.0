package com.nttdata.ecopetrol.talento.repository;

import com.nttdata.ecopetrol.talento.model.Incapacidad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IncapacidadRepository extends JpaRepository<Incapacidad, Long> {
    // No se agregan query methods ni validación de parámetros
}
