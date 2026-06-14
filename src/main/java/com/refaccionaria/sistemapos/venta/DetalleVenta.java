package com.refaccionaria.sistemapos.venta;
import com.refaccionaria.sistemapos.inventario.Inventario;
import jakarta.persistence.*;

@Entity
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer iddetalle;
    @ManyToOne
    @JoinColumn(name = "id_venta")
    private Venta venta;
    @ManyToOne
    @JoinColumn(name = "id_inventario")
    private Inventario inventario;
    private Integer cantidad;
    private Integer Precio_unitario;
    private Integer subtotal;
}
