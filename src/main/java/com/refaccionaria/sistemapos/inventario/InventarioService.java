package com.refaccionaria.sistemapos.inventario;
import com.refaccionaria.sistemapos.producto.Producto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioService {

    private InventarioRepository inventarioRepository;

    public InventarioService(InventarioRepository inventarioRepository){
        this.inventarioRepository = inventarioRepository;
    }
    //listar inventario
    public List<Inventario> ListarInventario(){
        return inventarioRepository.findAll();
    }
    //guardar inventario
    public Inventario GuardarInventario(Inventario inventario){

        System.out.println("Cantidad: " + inventario.getCantidad());
        System.out.println("Ubicacion: " + inventario.getUbicacion());

        if(inventario.getProducto() != null){
            System.out.println(
                    "Id Producto: " +
                            inventario.getProducto().getIdproducto()
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
        Inventario inventario = inventarioRepository.findById(idInventario).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        if(inventario.getCantidad() < cantidad){
            throw new RuntimeException("Sin stock");
        }
        else{
            inventario.setCantidad(inventario.getCantidad() - cantidad);
        }
        return inventarioRepository.save(inventario);
    }
    public Inventario BuscarById(Integer id){
        return inventarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }
   /* public boolean ExistenciaByProducto(Integer idProducto){
        Inventario inventario = inventarioRepository.findById()

    }*/







}
