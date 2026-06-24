package com.refaccionaria.sistemapos.venta;
import com.refaccionaria.sistemapos.pago.PagoDTO;

import java.util.List;

public class VentaDto {

    private Integer idCliente; // nullable
    private String tipoVenta;
    private List<DetalleVentaDTO> detalles;
    private PagoDTO pago;

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getTipoVenta() {
        return tipoVenta;
    }

    public void setTipoVenta(String tipoVenta) {
        this.tipoVenta = tipoVenta;
    }

    public List<DetalleVentaDTO> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleVentaDTO> detalles) {
        this.detalles = detalles;
    }

    public PagoDTO getPago() {
        return pago;
    }

    public void setPago(PagoDTO pago) {
        this.pago = pago;
    }
}