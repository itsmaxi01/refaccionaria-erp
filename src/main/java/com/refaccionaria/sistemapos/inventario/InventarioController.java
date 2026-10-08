
package com.refaccionaria.sistemapos.inventario;

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
    public List<InventarioDTO> ListarInventario() {
        return inventarioservice.ListarInventario().stream()
                .map(InventarioDTO::fromEntity)
                .toList();
    }

    @PostMapping
    public InventarioDTO GuardarInventario(@RequestBody InventarioDTO inventario){
        return InventarioDTO.fromEntity(inventarioservice.GuardarInventario(inventario.toEntity()));
    }

    @GetMapping("/producto/{idProducto}")
    public List<InventarioDTO> BuscarbyIdProducto(@PathVariable Integer idProducto){
        return inventarioservice.BuscarByIdProduct(idProducto).stream()
                .map(InventarioDTO::fromEntity)
                .toList();
    }

    @PostMapping("/{idInventario}/descontar/{cantidad}")
    public InventarioDTO descontar(
            @PathVariable Integer idInventario,
            @PathVariable Integer cantidad
    ){
        return InventarioDTO.fromEntity(inventarioservice.descontar(idInventario, cantidad));
    }

    @DeleteMapping("/{idInventario}")
    public InventarioDTO borrarById(@PathVariable Integer idInventario) {
        return InventarioDTO.fromEntity(inventarioservice.BorrarById(idInventario));
    }
}
