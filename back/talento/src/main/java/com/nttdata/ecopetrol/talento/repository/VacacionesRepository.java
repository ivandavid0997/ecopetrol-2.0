package com.nttdata.ecopetrol.talento.repository;

import com.nttdata.ecopetrol.talento.model.Vacaciones;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VacacionesRepository extends JpaRepository<Vacaciones, Long> {
    // Sin métodos personalizados ni validación
}
