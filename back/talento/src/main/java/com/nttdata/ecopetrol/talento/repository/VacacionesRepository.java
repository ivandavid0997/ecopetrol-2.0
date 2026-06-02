package com.nttdata.ecopetrol.talento.repository;

import com.nttdata.ecopetrol.talento.dto.projection.VacacionesListView;
import com.nttdata.ecopetrol.talento.enums.Estado;
import com.nttdata.ecopetrol.talento.model.Vacaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VacacionesRepository extends JpaRepository<Vacaciones, Long> {

    /**
     * Retorna todas las vacaciones cuyo estado coincide con el valor dado.
     * JPA genera: SELECT v FROM Vacaciones v WHERE v.estado = :estado
     * Evita el full-table-scan + filtro en memoria del método listarVacacionesPendientes.
     */
    List<Vacaciones> findByEstado(Estado estado);

    /**
     * R-04: Evita el problema N+1 al listar vacaciones pendientes por estado.
     * Carga el lider en el mismo JOIN en lugar de disparar una query por cada registro.
     * SQL resultante: SELECT v.*, u.* FROM vacaciones v LEFT JOIN usuario u ON v.lider_id = u.id WHERE v.estado = :estado
     */
    @Query("SELECT v FROM Vacaciones v LEFT JOIN FETCH v.lider WHERE v.estado = :estado")
    List<Vacaciones> findByEstadoWithLider(@Param("estado") Estado estado);

    /**
     * R-04: Evita el problema N+1 al listar todas las vacaciones.
     * Carga el lider en el mismo JOIN en lugar de disparar una query por cada registro.
     * SQL resultante: SELECT v.*, u.* FROM vacaciones v LEFT JOIN usuario u ON v.lider_id = u.id
     */
    @Query("SELECT v FROM Vacaciones v LEFT JOIN FETCH v.lider")
    List<Vacaciones> findAllWithLider();

    /**
     * R-02: Proyección de listado completo — selecciona SOLO las columnas declaradas
     * en VacacionesListView y su sub-proyección LiderView (u.id, u.nombre).
     * Spring Data genera internamente:
     *   SELECT v.cols..., u.id, u.nombre
     *   FROM vacaciones v LEFT JOIN usuario u ON v.lider_id = u.id
     * Más eficiente que findAllWithLider() porque no carga password, role, activo, etc.
     */
    List<VacacionesListView> findAllProjectedBy();

    /**
     * R-02: Proyección filtrada por estado — equivalente a findByEstadoWithLider pero
     * sin cargar columnas innecesarias del usuario lider.
     * Spring Data genera:
     *   SELECT v.cols..., u.id, u.nombre
     *   FROM vacaciones v LEFT JOIN usuario u ON v.lider_id = u.id
     *   WHERE v.estado = :estado
     */
    List<VacacionesListView> findProjectedByEstado(Estado estado);
}
