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
    public List<Producto> listarProductos() {
        return productoService.listarProductos();
    }

    @GetMapping("/{id}")
    public Producto BuscarById(@PathVariable Integer id) {
        return productoService.BuscarById(id);
    }

    @PostMapping
    public Producto AgregarProducto(@RequestBody Producto producto) {
        return productoService.AgregarProducto(producto);
    }

    @DeleteMapping("/{id}")
    public void EliminarById(@PathVariable Integer id) {
        productoService.EliminarById(id);
    }
}