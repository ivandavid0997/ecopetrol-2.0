package com.nttdata.ecopetrol.talento.dto.request;

public class CierreMesRequestDTO {
    private String tipo;
    private Long id;
    private String nombreEmpleado;
    // getters/setters

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String nombreEmpleado) {
        this.nombreEmpleado = nombreEmpleado;
    }
}

