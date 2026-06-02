package com.nttdata.ecopetrol.talento.model;

import com.nttdata.ecopetrol.talento.enums.Estado;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "incapacidades")
public class Incapacidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroEmpleado;
    private String nombreEmpleado;
    private String unidadNegocio;
    private String tipoIncapacidad;
    private String entidadSalud;
    private String categoria;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer totalDias;       // No se valida si es negativo o null
    private String diagnostico;      // Texto libre sin restricciones

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Estado estado;

    @Lob
    private String archivoAdjunto;

    @ManyToOne
    @JoinColumn(name = "lider_id")
    private Usuario lider;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    // Getters y setters sin encapsulamiento ni validación (mala práctica)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroEmpleado() { return numeroEmpleado; }
    public void setNumeroEmpleado(String numeroEmpleado) { this.numeroEmpleado = numeroEmpleado; }

    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }

    public String getUnidadNegocio() { return unidadNegocio; }
    public void setUnidadNegocio(String unidadNegocio) { this.unidadNegocio = unidadNegocio; }

    public String getTipoIncapacidad() { return tipoIncapacidad; }
    public void setTipoIncapacidad(String tipoIncapacidad) { this.tipoIncapacidad = tipoIncapacidad; }

    public String getEntidadSalud() { return entidadSalud; }
    public void setEntidadSalud(String entidadSalud) { this.entidadSalud = entidadSalud; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public Integer getTotalDias() { return totalDias; }
    public void setTotalDias(Integer totalDias) { this.totalDias = totalDias; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Usuario getLider() {
        return lider;
    }

    public void setLider(Usuario lider) {
        this.lider = lider;
    }

    public String getArchivoAdjunto() {
        return archivoAdjunto;
    }

    public void setArchivoAdjunto(String archivoAdjunto) {
        this.archivoAdjunto = archivoAdjunto;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    // setFechaCreacion eliminado: campo de auditoría inmutable (updatable=false + @PrePersist)
}
