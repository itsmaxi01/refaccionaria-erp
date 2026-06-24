package com.refaccionaria.sistemapos.venta;
import com.refaccionaria.sistemapos.inventario.Inventario;
import com.refaccionaria.sistemapos.cliente.ClienteService;
import com.refaccionaria.sistemapos.cliente.Cliente;
import com.refaccionaria.sistemapos.pago.PagoRepository;
import com.refaccionaria.sistemapos.pago.Pago;
import com.refaccionaria.sistemapos.pago.PagoService;
import com.refaccionaria.sistemapos.pago.PagoDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import com.refaccionaria.sistemapos.inventario.InventarioService;
import java.math.BigDecimal;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final InventarioService inventarioService;
    private final PagoRepository pagoRepository;
    private final ClienteService clienteService;
    private final DetalleVentaService detalleVentaService;

    //id_venta	id_cliente	fecha	total	estado	tipo_venta

    public VentaService(VentaRepository ventaRepository, InventarioService inventarioService, ClienteService clienteService,
                        DetalleVentaService detalleVentaService, PagoRepository pagoRepository) {
        this.ventaRepository = ventaRepository;
        this.inventarioService = inventarioService;
        this.pagoRepository = pagoRepository;
        this.clienteService = clienteService;
        this.detalleVentaService = detalleVentaService;
    }
    @Transactional
    public Venta crearVenta(VentaDto venta){
        //validaciones edge cases
        validarVentaDto(venta);
        validarDetallesDto(venta.getDetalles());
        validarPagoDto(venta.getPago());
      Venta ventaR= new Venta();
      if(venta.getIdCliente()!=null){
          ventaR.setcliente(clienteService.BuscarById(venta.getIdCliente()));
      }
      ventaR.setFecha(LocalDate.now());
      ventaR.setTipo_venta(venta.getTipoVenta());
      ventaR.setEstado("Pendiente"); //se calcula como pendiente de forma predeterminada
      ventaRepository.save(ventaR); //guardo venta para poder utilizar el id de detalle
      //guardar detalleV
        //id_detalle	id_venta	id_inventario	cantidad	precio_unitario	subtotal
        BigDecimal total = new BigDecimal(0);//variable para guardar el total de la venta
        for (int i = 0; i < venta.getDetalles().size(); i++) {
            DetalleVenta detalle= new DetalleVenta(); //inicializo detalle
            detalle.setVenta(ventaR);
            DetalleVentaDTO  detalleVentaDTO= venta.getDetalles().get(i);
            Inventario inventario = inventarioService.BuscarById(detalleVentaDTO.getIdInventario()); //Checo si el inventario hay stock
            detalle.setInventario(inventario); //relleno si hay stock
            inventarioService.descontar(inventario.getId_inventario(),detalleVentaDTO.getCantidad()); //descuento despues de rellenar salta error si la cantidad no es suficiente
            detalle.setCantidad(detalleVentaDTO.getCantidad());
            if(detalleVentaDTO.getPrecioUnitario() !=null ){
                detalle.setPrecioUnitario(detalleVentaDTO.getPrecioUnitario());
            }else{
                detalle.setPrecioUnitario(inventario.getProducto().getPrecio());

            }
            BigDecimal subtotal = detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad()));
            detalle.setSubtotal(subtotal);
            detalleVentaService.GuardarDetalle(detalle);
            total = total.add(subtotal); //version MVP sin busqueda en la db para debuggear
        }
        //construir pago
        Pago pago = new Pago();
        pago.setFecha(LocalDate.now());
        pago.setVenta(ventaR);
        pago.setMetodo(venta.getPago().getMetodo());
        pago.setMonto(venta.getPago().getMonto());
        BigDecimal zero = new BigDecimal(0);
        if(total.compareTo(pago.getMonto()) <= 0){
            ventaR.setEstado("PAGADA");
            System.out.println("El saldo a regresar es de " + pago.getMonto().subtract(total));
        }
        //con edge cases de credito refactor despues
        else if(pago.getMonto().compareTo(zero) > 0 ){
            if(ventaR.getcliente() == null){
                throw new RuntimeException("No se puedehacer una venta a credito sin ser cliente");
            }
            ventaR.setEstado("PARCIAL");
            System.out.println("El saldo a pagar es de " + total.subtract(pago.getMonto()));
        }
        else if(pago.getMonto().compareTo(zero) <= 0 ){
            if(ventaR.getcliente() == null){
                throw new RuntimeException("No se puedehacer una venta a credito sin ser cliente");
            }
            ventaR.setEstado("PENDIENTE");
            System.out.println("El saldo a pagar es de " + total.subtract(pago.getMonto()));
        }
        pagoRepository.save(pago);
      return ventaRepository.save(ventaR);
    }

    public void actualizarVenta(Integer idVenta,String Estado){
        Venta venta = ventaRepository.findById(idVenta).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        venta.setEstado(Estado);
    }

    //refactoirzar despues
    private void validarVentaDto(VentaDto venta){
        if(venta == null || venta.getTipoVenta() == null){
            throw new RuntimeException("venta es null o se necesita especificar tipo de venta");
        }

    }
    private void validarDetallesDto(List<DetalleVentaDTO> detalles){
        if(detalles == null || detalles.isEmpty() ){
            throw new RuntimeException("Detalles nulos");
        }
        for(int i=0; i<detalles.size();i++){
           DetalleVentaDTO detalle = detalles.get(i);
           if(detalle.getCantidad()<=0 || detalle.getCantidad() == null){
               throw new RuntimeException("La cantidad del detalle %d no es valida" +(i+1));
           }
           if(detalle.getIdInventario() == null){
               throw new RuntimeException("El detalle vene tener inventario" );
           }

        }
    }
    public void validarPagoDto(PagoDTO pago){       //se reutilizara en pagos
        BigDecimal zero = new BigDecimal(0);
        if(pago == null || pago.getMonto() == null){
            throw new RuntimeException("El pago o el monto no puede ser nulo");
        }
        if(pago.getMonto() == null){
            throw new RuntimeException("Elija un metodo");
        }
        if(pago.getMonto().compareTo(zero) < 0){
            throw new RuntimeException("El pago es de 0 ");
        }
    }
    public Venta BuscarById(Integer id){
        return ventaRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

     //DEFINO CONSTANTE PARA PAGADA
     public static final String PAGADA = "PAGADA";
    public List<Venta> VentasPendientes(){
        return ventaRepository.findByEstadoNot(PAGADA); //da pendientes, y parciales
    }
    /*public Venta VentaPendienteById(Integer id){
        List<Venta> ventas = ventaRepository.findByEstadoNot(PAGADA);
        Venta venta = new Venta();
        for(int i=0;i< ventas.size();i++){
             venta = ventas.get(i);

            if(venta.getIdventa()==id){
                break;
            }

        }
        return venta;
    }*/

    public Venta VentaPendienteById(Integer id){
        return ventaRepository.findByEstadoNotAndIdventa(PAGADA,id).orElseThrow(() -> new RuntimeException("Pues alch no hay nada we"));
    }











}
