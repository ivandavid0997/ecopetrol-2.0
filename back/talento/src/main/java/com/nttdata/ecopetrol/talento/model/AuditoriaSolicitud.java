package com.nttdata.ecopetrol.talento.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria_solicitudes")
public class AuditoriaSolicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tabla_origen", nullable = false, length = 50)
    private String tablaOrigen;

    @Column(name = "registro_id", nullable = false)
    private Long registroId;

    @Column(name = "accion", nullable = false, length = 20)
    private String accion;

    @ManyToOne
    @JoinColumn(name = "usuario_id", foreignKey = @ForeignKey(name = "fk_auditoria_usuario"))
    private Usuario usuario;

    @Column(name = "ip_origen", length = 45)
    private String ipOrigen;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "detalle", columnDefinition = "TEXT")
    private String detalle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTablaOrigen() {
        return tablaOrigen;
    }

    public void setTablaOrigen(String tablaOrigen) {
        this.tablaOrigen = tablaOrigen;
    }

    public Long getRegistroId() {
        return registroId;
    }

    public void setRegistroId(Long registroId) {
        this.registroId = registroId;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getIpOrigen() {
        return ipOrigen;
    }

    public void setIpOrigen(String ipOrigen) {
        this.ipOrigen = ipOrigen;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
}