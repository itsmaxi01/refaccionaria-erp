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
        if(cantidad<=0){
            throw new RuntimeException("Ingrese una cantidad mayor a 0");
        }
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
    public boolean ExistenciaByProducto(Integer idProducto){
        boolean Existencia = false;
        List<Inventario> inventarios = inventarioRepository.findByProducto_Idproducto(idProducto);
        for(int i=0;i< inventarios.size();i++){
            Inventario inventario = inventarios.get(i);
            if(inventario.getCantidad()>0){
                Existencia = true;
                break;
            }
        }
        return Existencia;
    }







}
