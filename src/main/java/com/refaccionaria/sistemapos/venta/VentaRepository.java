package com.refaccionaria.sistemapos.venta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;



public interface VentaRepository extends JpaRepository<Venta, Integer> {

    List<Venta> findByEstadoNotIn(List<String> estados);
    Optional<Venta> findByEstadoNotInAndIdventa(
            List<String> estados,
            Integer idventa
    );

}
