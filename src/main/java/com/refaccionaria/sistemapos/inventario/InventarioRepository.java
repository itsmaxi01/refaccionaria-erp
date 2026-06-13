package com.refaccionaria.sistemapos.inventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface InventarioRepository extends JpaRepository<Inventario, Integer> {
    List<Inventario> findByProducto_Idproducto(Integer idproducto);
}
