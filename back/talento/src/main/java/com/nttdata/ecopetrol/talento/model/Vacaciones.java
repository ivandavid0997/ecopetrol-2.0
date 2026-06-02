package com.nttdata.ecopetrol.talento.model;

import com.nttdata.ecopetrol.talento.enums.Estado;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vacaciones")
public class Vacaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Extras para la creación
    private String numeroEmpleado;
    private String nombreEmpleado;
    private String unidadNegocio;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Integer totalDias;
    private String comentario;


    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Estado estado;

    @ManyToOne
    @JoinColumn(name = "lider_id")
    private Usuario lider;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }

    // Getters y setters omiten validación y encapsulamiento (mala práctica)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }

    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }

    public Integer getTotalDias() { return totalDias; }
    public void setTotalDias(Integer totalDias) { this.totalDias = totalDias; }

    public String getNumeroEmpleado() { return numeroEmpleado; }
    public void setNumeroEmpleado(String numeroEmpleado) { this.numeroEmpleado = numeroEmpleado; }

    public String getUnidadNegocio() { return unidadNegocio; }
    public void setUnidadNegocio(String unidadNegocio) { this.unidadNegocio = unidadNegocio; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

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

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    // setFechaCreacion eliminado: campo de auditoría inmutable (updatable=false + @PrePersist)
}
