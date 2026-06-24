package com.refaccionaria.sistemapos.venta;
import com.refaccionaria.sistemapos.inventario.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {

    List<DetalleVenta> findByVenta_Idventa(Integer idventa);
}
