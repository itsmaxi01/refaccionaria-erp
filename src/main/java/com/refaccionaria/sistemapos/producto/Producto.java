package com.refaccionaria.sistemapos.producto;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idproducto;
    private String codigo_barras;
    private String nombre;
    private String tipo;
    private BigDecimal precio;
    private Boolean activo = true;

    public Producto() {
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    public Integer getidproducto() {
        return idproducto;
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

    public BigDecimal getPrecio() {return precio;}

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }
    public void setIdproducto(Integer idproducto) {
        this.idproducto = idproducto;
    }




    public Boolean getActivo() {
        return activo;
    }
}