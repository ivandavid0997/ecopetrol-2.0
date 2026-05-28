package com.nttdata.ecopetrol.talento.model;

import jakarta.persistence.*;

import java.util.Date;

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
    private Date fechaInicio;        // No se valida el formato ni el null
    private Date fechaFin;           // Lo mismo aquí
    private Integer totalDias;       // No se valida si es negativo o null
    private String diagnostico;      // Texto libre sin restricciones
    private String estado;

    @Lob
    private String archivoAdjunto;

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

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }

    public Integer getTotalDias() { return totalDias; }
    public void setTotalDias(Integer totalDias) { this.totalDias = totalDias; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
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

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}