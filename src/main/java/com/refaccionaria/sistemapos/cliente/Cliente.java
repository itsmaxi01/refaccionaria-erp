package com.refaccionaria.sistemapos.cliente;

import jakarta.persistence.*;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer idCliente;

    private String nombre;

    private String telefono;

    private String direccion;

    @Column(name = "tipo_cliente")
    private String tipoCliente;

    private Boolean activo = true;

    public Cliente() {
    }

    public Cliente(String nombre, String telefono, String direccion, String tipoCliente) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.direccion = direccion;
        this.tipoCliente = tipoCliente;
        this.activo = true;
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}