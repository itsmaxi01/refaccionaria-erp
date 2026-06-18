package com.refaccionaria.sistemapos.producto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idproducto;
   // @JsonProperty("codigo_barras")// parche para codigo de barras
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
    public Integer getIdproducto() {
        return idproducto;
    }

    public void setCodigo_barras(String codigo_barras){this.codigo_barras=codigo_barras;
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
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }




    public Boolean getActivo() {
        return activo;
    }
}