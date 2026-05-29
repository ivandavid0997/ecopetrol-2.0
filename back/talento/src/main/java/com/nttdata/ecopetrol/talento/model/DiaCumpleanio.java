package com.nttdata.ecopetrol.talento.model;

import com.nttdata.ecopetrol.talento.enums.Estado;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "dias_cumpleanios")
public class DiaCumpleanio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroEmpleado;
    private String nombreEmpleado;
    private String unidadNegocio;
    private Date fechaCumpleanio;       // Fecha del cumpleaños
    private String comentario;          // Comentario adicional sin validación

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

    // Getters y setters omitiendo validación
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumeroEmpleado() { return numeroEmpleado; }
    public void setNumeroEmpleado(String numeroEmpleado) { this.numeroEmpleado = numeroEmpleado; }

    public String getNombreEmpleado() { return nombreEmpleado; }
    public void setNombreEmpleado(String nombreEmpleado) { this.nombreEmpleado = nombreEmpleado; }

    public Date getFechaCumpleanio() { return fechaCumpleanio; }
    public void setFechaCumpleanio(Date fechaCumpleanio) { this.fechaCumpleanio = fechaCumpleanio; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public String getUnidadNegocio() {
        return unidadNegocio;
    }

    public void setUnidadNegocio(String unidadNegocio) {
        this.unidadNegocio = unidadNegocio;
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

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
