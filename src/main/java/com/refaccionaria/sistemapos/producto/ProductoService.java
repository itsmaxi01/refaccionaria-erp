package com.refaccionaria.sistemapos.producto;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarProductos() {
        List<Producto> productos = productoRepository.findAll();

        System.out.println("hola");
        return productos;

    }

    public Producto AgregarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto BuscarById(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public void EliminarById(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setActivo(false);
        productoRepository.save(producto);
    }
}