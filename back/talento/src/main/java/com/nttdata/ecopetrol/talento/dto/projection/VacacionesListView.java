package com.nttdata.ecopetrol.talento.dto.projection;

import com.nttdata.ecopetrol.talento.enums.Estado;

import java.util.Date;

/**
 * R-02: Proyección de interfaz con sub-proyección anidada para el lider.
 *
 * Spring Data JPA genera automáticamente un LEFT JOIN que selecciona SOLO
 * las columnas declaradas en esta interfaz y en LiderView:
 *
 *   SELECT v.numero_empleado, v.nombre_empleado, v.unidad_negocio,
 *          v.fecha_inicio, v.fecha_fin, v.total_dias, v.estado,
 *          u.id, u.nombre                        ← solo 2 cols del lider
 *   FROM vacaciones v
 *   LEFT JOIN usuario u ON v.lider_id = u.id
 *
 * Ventaja frente a findAllWithLider() (R-04):
 *   - R-04: SELECT v.*, u.*  → carga password, role, activo, etc. del usuario
 *   - R-02: SELECT v.cols, u.id, u.nombre → mínimo de datos necesarios
 */
public interface VacacionesListView {

    Long getId();

    String getNumeroEmpleado();

    String getNombreEmpleado();

    String getUnidadNegocio();

    Date getFechaInicio();

    Date getFechaFin();

    Integer getTotalDias();

    Estado getEstado();

    /**
     * Sub-proyección: Spring Data solo cargará id y nombre del usuario lider.
     * Si lider_id es NULL, este getter retorna null (LEFT JOIN).
     */
    LiderView getLider();

    interface LiderView {
        Long getId();
        String getNombre();
    }
}
