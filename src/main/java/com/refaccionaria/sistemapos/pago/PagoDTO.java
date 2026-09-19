package com.refaccionaria.sistemapos.pago;
import com.refaccionaria.sistemapos.venta.VentaDto;

import java.math.BigDecimal;

public class PagoDTO {

    private BigDecimal monto_abonado;
    private BigDecimal monto_recibido;
    private String metodo;

    public BigDecimal getMonto_recibido() {
        return monto_recibido;
    }

    public void setMonto_recibido(BigDecimal monto_recibido) {
        this.monto_recibido = monto_recibido;
    }

    public BigDecimal getMonto_abonado() {
        return monto_abonado;
    }
    public void setMonto_abonado(BigDecimal monto_abonado) {
        this.monto_abonado = monto_abonado;
    }


    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }
}