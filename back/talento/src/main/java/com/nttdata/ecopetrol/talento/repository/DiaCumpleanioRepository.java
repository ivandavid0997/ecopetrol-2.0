package com.nttdata.ecopetrol.talento.repository;

import com.nttdata.ecopetrol.talento.model.DiaCumpleanio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaCumpleanioRepository extends JpaRepository<DiaCumpleanio, Long> {
    // Sin métodos personalizados ni validaciones
}
