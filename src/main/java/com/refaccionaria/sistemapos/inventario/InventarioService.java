package com.refaccionaria.sistemapos.inventario;
import com.refaccionaria.sistemapos.excepciones.BadRequestException;
import com.refaccionaria.sistemapos.excepciones.ConflictException;
import com.refaccionaria.sistemapos.producto.Producto;
import com.refaccionaria.sistemapos.producto.ProductoRepository;
import com.refaccionaria.sistemapos.excepciones.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioService {

    private static final int LONGITUD_MAXIMA_UBICACION = 100;
    private static final String MARCADOR_INACTIVO = " FALSE ";

    private final InventarioRepository inventarioRepository;
    private final ProductoRepository productoRepository;

    public InventarioService(InventarioRepository inventarioRepository, ProductoRepository productoRepository
                ){
        this.inventarioRepository = inventarioRepository;
        this.productoRepository = productoRepository;

    }
    //listar inventario
    public List<Inventario> ListarInventario(){
        return inventarioRepository.findByActivoTrue();
    }
    //pasar IsActivo a producto
    public void validarProducto(Producto producto) {

        if (producto == null || producto.getIdproducto() == null) {
            throw new BadRequestException("Debe especificar un producto");
        }

        if (!IsActivo(producto.getIdproducto())) {
            throw new ConflictException(
                    "El producto no está activo y no puede agregarse al inventario"
            );
        }

    }
    public Boolean IsActivo(Integer id){

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));
        return Boolean.TRUE.equals(producto.getActivo());
    }
    //guardar inventario
    @Transactional
    public Inventario GuardarInventario(Inventario inventario){
        if (inventario == null) {
            throw new BadRequestException("Debe especificar un inventario");
        }

        validarProducto(inventario.getProducto());
        if(inventario.getCantidad() == null || inventario.getCantidad() <= 0){
            throw new BadRequestException("La cantidad debe ser mayor a 0");
        }

        if (inventario.getUbicacion() == null || inventario.getUbicacion().isBlank()) {
            throw new BadRequestException("Debe especificar una ubicación");
        }

        inventario.setUbicacion(inventario.getUbicacion().trim());
        if (inventario.getUbicacion().length() > LONGITUD_MAXIMA_UBICACION) {
            throw new BadRequestException("La ubicación no puede superar los 100 caracteres");
        }
        inventario.setActivo(true);

        List<Inventario> inventarios = inventarioRepository
                .findByProducto_IdproductoAndActivoTrue(inventario.getProducto().getIdproducto());

        for (Inventario inventarioExistente : inventarios) {
            if (inventarioExistente.getUbicacion() != null
                    && inventarioExistente.getUbicacion().equalsIgnoreCase(inventario.getUbicacion())) {
                throw new ConflictException("El producto ya existe en esa ubicación");
            }
        }
        return inventarioRepository.save(inventario);
    }
    //buscar inventario por id de producto
    public List<Inventario> BuscarByIdProduct(Integer idProducto){
        if (idProducto == null) {
            throw new BadRequestException("Debe especificar un producto");
        }
        return inventarioRepository.findByProducto_IdproductoAndActivoTrue(idProducto);
    }

    //descontar
    public Inventario descontar(Integer idInventario,Integer cantidad){
        if(cantidad == null || cantidad <= 0){
            throw new BadRequestException("Ingrese una cantidad mayor a 0");
        }
        Inventario inventario = BuscarById(idInventario);
        if (inventario.getCantidad() == null) {
            throw new ConflictException("El inventario no tiene una cantidad válida");
        }
        if(inventario.getCantidad() < cantidad){
            throw new ConflictException("No hay stock suficiente");
        }
        else{
            inventario.setCantidad(inventario.getCantidad() - cantidad);
        }
        return inventarioRepository.save(inventario);
    }

    public Inventario BorrarById(Integer id){
        Inventario inventario = BuscarById(id);
        if(!Integer.valueOf(0).equals(inventario.getCantidad())){
            throw new ConflictException("No se puede eliminar el inventario mientras tenga stock");
        }

        String ubicacionOriginal = inventario.getUbicacion();
        if (ubicacionOriginal == null || ubicacionOriginal.isBlank()) {
            ubicacionOriginal = "SIN UBICACION";
        } else {
            ubicacionOriginal = ubicacionOriginal.trim();
        }

        String sufijoInactivo = MARCADOR_INACTIVO + inventario.getId_inventario();
        int longitudDisponible = LONGITUD_MAXIMA_UBICACION - sufijoInactivo.length();
        if (ubicacionOriginal.length() > longitudDisponible) {
            ubicacionOriginal = ubicacionOriginal.substring(0, longitudDisponible).trim();
        }

        inventario.setUbicacion(ubicacionOriginal + sufijoInactivo);
        inventario.setActivo(false);
        return inventarioRepository.save(inventario);
    }



    public Inventario BuscarById(Integer id){
        if (id == null) {
            throw new BadRequestException("Debe especificar un inventario");
        }
        return inventarioRepository.findByIdinventarioAndActivoTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventario activo no encontrado"));
    }

    public boolean ExistenciaByProducto(Integer idProducto){
        if (idProducto == null) {
            throw new BadRequestException("Debe especificar un producto");
        }
        List<Inventario> inventarios = inventarioRepository
                .findByProducto_IdproductoAndActivoTrue(idProducto);

        return inventarios.stream()
                .anyMatch(inventario -> inventario.getCantidad() != null
                        && inventario.getCantidad() > 0);
    }







}
