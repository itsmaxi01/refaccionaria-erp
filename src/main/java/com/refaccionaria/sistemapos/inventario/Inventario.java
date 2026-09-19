package com.refaccionaria.sistemapos.inventario;

import com.refaccionaria.sistemapos.producto.Producto;
import jakarta.persistence.*;

@Entity
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inventario")
    private Integer idinventario;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    private Integer cantidad;

    private String ubicacion;

    private Boolean activo = true;

    public Inventario() {
    }

    public Integer getId_inventario() {
        return idinventario;
    }

    public Producto getProducto() {
        return producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public String getUbicacion() {
        return ubicacion;
    }
    public Boolean getActivo(){
        return activo;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public void setActivo(Boolean activo){ this.activo = activo;}
}
