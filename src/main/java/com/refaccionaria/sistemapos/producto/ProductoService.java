package com.refaccionaria.sistemapos.producto;
import com.refaccionaria.sistemapos.excepciones.ConflictException;

import com.refaccionaria.sistemapos.excepciones.ResourceNotFoundException;
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
        return productoRepository.save(producto);
    }

    public Producto BuscarById(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
    }

    public Producto EliminarById(Integer id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
        if(inventarioService.ExistenciaByProducto(id)){
            throw new ConflictException("No se puede desactivar el producto mientras tenga inventario con stock");
        }
        producto.setActivo(false);
        return productoRepository.save(producto);
    }


    }


