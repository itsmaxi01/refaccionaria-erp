
package com.refaccionaria.sistemapos.inventario;

import com.refaccionaria.sistemapos.producto.Producto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Inventario")
public class InventarioController {

    private final InventarioService inventarioservice;

    public InventarioController(InventarioService inventarioservice){
        this.inventarioservice = inventarioservice;
    }

    @GetMapping
    public List<Inventario> ListarInventario() {
        return inventarioservice.ListarInventario();
    }

    @PostMapping
    public Inventario GuardarInventario(@RequestBody Inventario inventario){
        return inventarioservice.GuardarInventario(inventario);
    }

    @GetMapping("/producto/{idProducto}")
    public List<Inventario> BuscarbyIdProducto(@PathVariable Integer idProducto){
        return inventarioservice.BuscarByIdProduct(idProducto);
    }

    @PostMapping("/{idInventario}/descontar/{cantidad}")
    public Inventario descontar(
            @PathVariable Integer idInventario,
            @PathVariable Integer cantidad
    ){
        return inventarioservice.descontar(idInventario, cantidad);
    }
}
