package com.refaccionaria.sistemapos.venta;
import java.math.BigDecimal;


import java.math.BigDecimal;

public class DetalleVentaDTO {

    private Integer idInventario;
    private Integer cantidad;
    private BigDecimal precioUnitario;

    public Integer getIdInventario() {
        return idInventario;
    }

    public void setIdInventario(Integer idInventario) {
        this.idInventario = idInventario;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}