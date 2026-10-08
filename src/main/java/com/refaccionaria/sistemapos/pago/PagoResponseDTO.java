package com.refaccionaria.sistemapos.pago;

import com.refaccionaria.sistemapos.venta.VentaResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PagoResponseDTO {
    private Integer idpago;
    private VentaResponseDTO venta;
    private BigDecimal monto_abonado;
    private BigDecimal monto_recibido;
    private String metodo;
    private LocalDate fecha;

    public static PagoResponseDTO fromEntity(Pago pago) {
        if (pago == null) {
            return null;
        }
        PagoResponseDTO dto = new PagoResponseDTO();
        dto.setIdpago(pago.getIdpago());
        dto.setVenta(VentaResponseDTO.fromEntity(pago.getVenta()));
        dto.setMonto_abonado(pago.getMonto_abonado());
        dto.setMonto_recibido(pago.getMonto_recibido());
        dto.setMetodo(pago.getMetodo());
        dto.setFecha(pago.getFecha());
        return dto;
    }

    public Integer getIdpago() {
        return idpago;
    }

    public void setIdpago(Integer idpago) {
        this.idpago = idpago;
    }

    public VentaResponseDTO getVenta() {
        return venta;
    }

    public void setVenta(VentaResponseDTO venta) {
        this.venta = venta;
    }

    public BigDecimal getMonto_abonado() {
        return monto_abonado;
    }

    public void setMonto_abonado(BigDecimal monto_abonado) {
        this.monto_abonado = monto_abonado;
    }

    public BigDecimal getMonto_recibido() {
        return monto_recibido;
    }

    public void setMonto_recibido(BigDecimal monto_recibido) {
        this.monto_recibido = monto_recibido;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
