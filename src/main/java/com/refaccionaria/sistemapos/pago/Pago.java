package com.refaccionaria.sistemapos.pago;
import com.refaccionaria.sistemapos.venta.Venta;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Pago {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Integer idpago;
    @ManyToOne
    @JoinColumn(name = "id_venta")
    private Venta venta;
    private BigDecimal monto_abonado;
    private BigDecimal monto_recibido;
    private String metodo;
    private LocalDate fecha;

    public Pago(){

    }


    public Integer getIdpago() {
        return idpago;
    }

    public void setIdpago(Integer idpago) {
        this.idpago = idpago;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
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
