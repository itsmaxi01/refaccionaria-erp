package com.refaccionaria.sistemapos.pago;


import com.refaccionaria.sistemapos.inventario.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagoRepository extends JpaRepository<Pago, Integer>
{
    List<Pago> findByVenta_Idventa(Integer idventa);
}
