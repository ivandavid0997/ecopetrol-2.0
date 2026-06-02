package com.nttdata.ecopetrol.talento.model;

import com.nttdata.ecopetrol.talento.enums.Rol;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Rol rol;
    private String usuario;
    @Column(name = "numero_empleado", length = 30)
    private String numeroEmpleado;
    private String unidadNegocio;

    @Column(nullable = false)
    private String password;

    @ColumnDefault("0")
    private int intentosFallidos;

    private LocalDateTime bloqueadoHasta;

    /**
     * C-02: Soft delete — false indica usuario eliminado lógicamente.
     * Nunca usar DELETE físico; usar setActivo(false).
     */
    @Column(nullable = false)
    @ColumnDefault("1")
    private boolean activo = true;

    // Getters y setters omitiendo validación por malas prácticas intencionales
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }


    public String getNumeroEmpleado() {
        return numeroEmpleado;
    }

    public void setNumeroEmpleado(String numeroEmpleado) {
        this.numeroEmpleado = numeroEmpleado;
    }

    public String getUnidadNegocio() {
        return unidadNegocio;
    }

    public void setUnidadNegocio(String unidadNegocio) {
        this.unidadNegocio = unidadNegocio;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getIntentosFallidos() { return intentosFallidos; }
    public void setIntentosFallidos(int intentosFallidos) { this.intentosFallidos = intentosFallidos; }

    public LocalDateTime getBloqueadoHasta() { return bloqueadoHasta; }
    public void setBloqueadoHasta(LocalDateTime bloqueadoHasta) { this.bloqueadoHasta = bloqueadoHasta; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
