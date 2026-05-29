package com.nttdata.ecopetrol.talento.model;

import com.nttdata.ecopetrol.talento.enums.Estado;
import jakarta.persistence.*;

import java.util.Date;

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
    private Date fechaInicio;
    private Date fechaFin;
    private Integer totalDias;
    private String comentario;


    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Estado estado;

    @ManyToOne
    @JoinColumn(name = "lider_id")
    private Usuario lider;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private Date fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = new Date();
    }

    // Getters y setters omiten validación y encapsulamiento (mala práctica)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }

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

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}