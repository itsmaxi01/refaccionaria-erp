package com.refaccionaria.sistemapos.inventario;
import com.refaccionaria.sistemapos.excepciones.BadRequestException;
import com.refaccionaria.sistemapos.excepciones.ConflictException;
import com.refaccionaria.sistemapos.producto.Producto;
import com.refaccionaria.sistemapos.producto.ProductoRepository;
import com.refaccionaria.sistemapos.excepciones.ResourceNotFoundException;
import com.refaccionaria.sistemapos.producto.ProductoService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioService {

    private InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;

    public InventarioService(InventarioRepository inventarioRepository, ProductoRepository productoRepository
                ){
        this.inventarioRepository = inventarioRepository;
        this.productoRepository = productoRepository;

    }
    //listar inventario
    public List<Inventario> ListarInventario(){
        return inventarioRepository.findAll();
    }
    //pasar IsActivo a producto
    public void validarProducto(Producto producto) {

        if (producto == null || producto.getIdproducto() == null) {
            throw new BadRequestException("Debe especificar un producto");
        }

        if (!IsActivo(producto.getIdproducto())) {
            throw new ConflictException(
                    "El producto no está activo, no se puede añadir al inventario"
            );
        }

    }
    public Boolean IsActivo(Integer id){

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
        if(producto.getActivo() == true){
            return true;
        }
        else{
            return false;
        }
    }
    //guardar inventario
    @Transactional
    public Inventario GuardarInventario(Inventario inventario){
        validarProducto(inventario.getProducto());
        if(inventario.getCantidad()<=0){
            throw new ConflictException(
                    "Seleccione una cantidad valida"
            );

        }
        return inventarioRepository.save(inventario);
    }
    //buscar inventario por id de producto
    public List<Inventario> BuscarByIdProduct(Integer idProducto){
        return inventarioRepository.findByProducto_Idproducto(idProducto);
    }

    //descontar
    public Inventario descontar(Integer idInventario,Integer cantidad){
        if(cantidad<=0){
            throw new BadRequestException("Ingrese una cantidad mayor a 0");
        }
        Inventario inventario = inventarioRepository.findById(idInventario).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
        if(inventario.getCantidad() < cantidad){
            throw new ConflictException("Sin stock");
        }
        else{
            inventario.setCantidad(inventario.getCantidad() - cantidad);
        }
        return inventarioRepository.save(inventario);
    }



    public Inventario BuscarById(Integer id){
        return inventarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado Pendejo"));
    }

    public boolean ExistenciaByProducto(Integer idProducto){
        boolean Existencia = false;
        List<Inventario> inventarios = inventarioRepository.findByProducto_Idproducto(idProducto);
        for(int i=0;i< inventarios.size();i++){
            Inventario inventario = inventarios.get(i);
            if(inventario.getCantidad()>0){
                Existencia = true;
            }
        }
        return Existencia;
    }







}
