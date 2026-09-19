package com.refaccionaria.sistemapos.inventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface InventarioRepository extends JpaRepository<Inventario, Integer> {
    List<Inventario> findByActivoTrue();

    List<Inventario> findByProducto_IdproductoAndActivoTrue(Integer idproducto);

    Optional<Inventario> findByIdinventarioAndActivoTrue(Integer idInventario);
}
