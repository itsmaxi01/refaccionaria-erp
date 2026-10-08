package com.refaccionaria.sistemapos.producto;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<ProductoDTO> listarProductos() {

        return productoService.listarProductos().stream()
                .map(ProductoDTO::fromEntity)
                .toList();
    }

    @GetMapping("/{id}")
    public ProductoDTO BuscarById(@PathVariable Integer id) {
        return ProductoDTO.fromEntity(productoService.BuscarById(id));
    }

    @PostMapping
    public ProductoDTO AgregarProducto(@RequestBody ProductoDTO producto) {
        return ProductoDTO.fromEntity(productoService.AgregarProducto(producto.toEntity()));
    }

    @DeleteMapping("/{id}")
    public void EliminarById(@PathVariable Integer id) {
        productoService.EliminarById(id);
    }
}
