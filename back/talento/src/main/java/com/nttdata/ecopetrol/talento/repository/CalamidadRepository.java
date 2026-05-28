package com.nttdata.ecopetrol.talento.repository;

import com.nttdata.ecopetrol.talento.model.Calamidad;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalamidadRepository extends JpaRepository<Calamidad, Long> {
    // Sin métodos personalizados ni validación
}
