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
        System.out.println("ENTRO A /productos");
        return productoService.listarProductos();
    }

    @GetMapping("/{id}")
    public Producto BuscarById(@PathVariable Integer id) {
        return productoService.BuscarById(id);
    }

    @PostMapping
    public Producto AgregarProducto(@RequestBody Producto producto) {
        System.out.println("Codigo: " + producto.getCodigo_barras());
        System.out.println("Nombre: " + producto.getNombre());
        return productoService.AgregarProducto(producto);
    }

    @DeleteMapping("/{id}")
    public void EliminarById(@PathVariable Integer id) {
        System.out.println("Id " + id);
        productoService.EliminarById(id);
    }
}