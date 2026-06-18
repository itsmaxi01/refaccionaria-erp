package com.refaccionaria.sistemapos.venta;
import com.refaccionaria.sistemapos.inventario.Inventario;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import com.refaccionaria.sistemapos.inventario.InventarioService;
import java.math.BigDecimal;

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
            total = total.add(subtotal); //version MVP sin busqueda en la base para debuggear
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





}
