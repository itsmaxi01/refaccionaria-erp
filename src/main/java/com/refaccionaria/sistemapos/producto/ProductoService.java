package com.refaccionaria.sistemapos.producto;
import com.refaccionaria.sistemapos.producto.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

@Service
public class ProductoService{

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository){
        this.productoRepository = productoRepository;
    }

    public List<Producto> ListarProductos(){

        return productoRepository.findAll();
    }
    public Producto Agregar_Producto(Producto){

        return productoRepository.save(Producto);
    }


}