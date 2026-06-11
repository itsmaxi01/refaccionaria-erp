package com.refaccionaria.sistemapos.producto;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_producto;

    private String codigo_barras;
    private String nombre;
    private String tipo;
    private BigDecimal precio_menudeo;
    private BigDecimal precio_mayoreo;
    private Boolean activo = true;

    public Producto() {
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    public Integer getId_producto() {
        return id_producto;
    }

    public String getCodigo_barras() {
        return codigo_barras;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public BigDecimal getPrecio_menudeo() {
        return precio_menudeo;
    }

    public BigDecimal getPrecio_mayoreo() {
        return precio_mayoreo;
    }

    public Boolean getActivo() {
        return activo;
    }
}