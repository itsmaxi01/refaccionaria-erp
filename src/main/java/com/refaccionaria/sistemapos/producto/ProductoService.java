package com.refaccionaria.sistemapos.producto;

import com.refaccionaria.sistemapos.inventario.InventarioService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final InventarioService inventarioService;

    public ProductoService(ProductoRepository productoRepository, InventarioService inventarioService)
    {
        this.inventarioService  = inventarioService;
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Producto AgregarProducto(Producto producto) {
        /*System.out.println("ID: " + producto.getIdproducto());
        System.out.println("Código de barras: " + producto.getCodigo_barras());
        System.out.println("Nombre: " + producto.getNombre());
        System.out.println("Tipo: " + producto.getTipo());
        System.out.println("Precio: " + producto.getPrecio());
        System.out.println("Activo: " + producto.getActivo());*/ //debugg
        return productoRepository.save(producto);
    }

    public Producto BuscarById(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public Producto EliminarById(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        if(inventarioService.ExistenciaByProducto(id)){
            throw new RuntimeException("Hay un producto en stock, intente mas tarde");
        }
        producto.setActivo(false);
        return productoRepository.save(producto);
    }
}