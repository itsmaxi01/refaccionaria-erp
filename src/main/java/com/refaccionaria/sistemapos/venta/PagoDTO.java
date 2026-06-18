package com.refaccionaria.sistemapos.venta;

import java.math.BigDecimal;

public class PagoDTO {

    private BigDecimal monto;
    private String metodo;

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }
}