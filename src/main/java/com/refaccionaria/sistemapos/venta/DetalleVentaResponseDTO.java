package com.refaccionaria.sistemapos.venta;

import com.refaccionaria.sistemapos.inventario.InventarioDTO;

import java.math.BigDecimal;

public class DetalleVentaResponseDTO {
    private Integer idDetalle;
    private VentaResponseDTO venta;
    private InventarioDTO inventario;
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public static DetalleVentaResponseDTO fromEntity(DetalleVenta detalle) {
        if (detalle == null) {
            return null;
        }
        DetalleVentaResponseDTO dto = new DetalleVentaResponseDTO();
        dto.setIdDetalle(detalle.getIdDetalle());
        dto.setVenta(VentaResponseDTO.fromEntity(detalle.getVenta()));
        dto.setInventario(InventarioDTO.fromEntity(detalle.getInventario()));
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());
        return dto;
    }

    public Integer getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public VentaResponseDTO getVenta() {
        return venta;
    }

    public void setVenta(VentaResponseDTO venta) {
        this.venta = venta;
    }

    public InventarioDTO getInventario() {
        return inventario;
    }

    public void setInventario(InventarioDTO inventario) {
        this.inventario = inventario;
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

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
