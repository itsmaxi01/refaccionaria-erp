package com.refaccionaria.sistemapos.venta;
import com.refaccionaria.sistemapos.excepciones.BadRequestException;
import com.refaccionaria.sistemapos.excepciones.ConflictException;
import com.refaccionaria.sistemapos.excepciones.ResourceNotFoundException;
import com.refaccionaria.sistemapos.inventario.Inventario;
import com.refaccionaria.sistemapos.cliente.ClienteService;
import com.refaccionaria.sistemapos.cliente.Cliente;
import com.refaccionaria.sistemapos.pago.PagoRepository;
import com.refaccionaria.sistemapos.pago.Pago;
import com.refaccionaria.sistemapos.pago.PagoDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import com.refaccionaria.sistemapos.inventario.InventarioService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class VentaService {

    private static final Set<String> METODOS_PAGO = Set.of(
            "EFECTIVO",
            "TARJETA",
            "TRANSFERENCIA"
    );
    private static final List<EstadoVenta> ESTADOS_LIQUIDADOS = List.of(
            EstadoVenta.PAGADA,
            EstadoVenta.SALDADA
    );

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
    public void validarVenta(VentaDto venta){
        validarVentaDto(venta);
        validarDetallesDto(venta.getDetalles());
        validarPagoDto(venta.getPago());
    }
    public Venta creacionVenta(VentaDto venta){
        Venta ventaR= new Venta();
        if(venta.getIdCliente()!=null){
            ventaR.setCliente(clienteService.BuscarById(venta.getIdCliente()));
        }
        ventaR.setFecha(LocalDate.now());
        ventaR.setTipo_venta(venta.getTipoVenta());
        ventaR.setEstado(EstadoVenta.PENDIENTE); //se calcula como pendiente de forma predeterminada
        ventaRepository.save(ventaR); //guardo venta para poder utilizar el id de detalle
        return ventaR;
    }
    public BigDecimal  guardarDetalles(Venta ventaR, VentaDto venta){
        BigDecimal total = new BigDecimal(0);//variable para guardar el total de la venta
        //id_detalle	id_venta	id_inventario	cantidad	precio_unitario	subtotal
        for (int i = 0; i < venta.getDetalles().size(); i++) {
            DetalleVenta detalle= new DetalleVenta(); //inicializo detalle
            detalle.setVenta(ventaR);
            DetalleVentaDTO  detalleVentaDTO= venta.getDetalles().get(i);
            Inventario inventario = inventarioService.BuscarById(detalleVentaDTO.getIdInventario()); //Checo si el inventario existe
            detalle.setInventario(inventario); //relleno si existe
            inventarioService.descontar(inventario.getId_inventario(),detalleVentaDTO.getCantidad()); //descuento despues de rellenar salta error si la cantidad no es suficiente
            detalle.setCantidad(detalleVentaDTO.getCantidad());
            if(detalleVentaDTO.getPrecioUnitario() !=null ){
                detalle.setPrecioUnitario(detalleVentaDTO.getPrecioUnitario());
            }else{
                detalle.setPrecioUnitario(inventario.getProducto().getPrecio());

            }
            if (detalle.getPrecioUnitario() == null || detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                throw new BadRequestException("El precio unitario del detalle " + (i + 1) + " debe ser mayor a 0");
            }
            BigDecimal subtotal = detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad()));
            detalle.setSubtotal(subtotal);
            detalleVentaService.GuardarDetalle(detalle);
            total = total.add(subtotal);
        }
        return total;

    }
    public void ProcesarPago(Venta ventaR,BigDecimal total, VentaDto venta  ){
        Pago pago = new Pago();
        pago.setFecha(LocalDate.now());
        pago.setVenta(ventaR);
        pago.setMetodo(venta.getPago().getMetodo());
        pago.setMonto_recibido(venta.getPago().getMonto_recibido());
        pago.setMonto_abonado(venta.getPago().getMonto_abonado());
        BigDecimal zero = new BigDecimal(0);

        if(total.compareTo(pago.getMonto_abonado()) == 0){
            ventaR.setEstado(EstadoVenta.PAGADA);
        }
        else if(total.compareTo(pago.getMonto_abonado()) < 0){
            throw new ConflictException("El monto abonado no puede superar el total de la venta");
        }
        else if(pago.getMonto_abonado().compareTo(zero) > 0 ){
            if(ventaR.getCliente() == null){
                throw new BadRequestException("No se puede realizar una venta parcial sin un cliente");
            }
            ventaR.setEstado(EstadoVenta.PARCIAL);
        }
        pagoRepository.save(pago);
    }
    @Transactional
    public Venta crearVenta(VentaDto venta){
       validarVenta(venta);
       Venta ventaR = creacionVenta(venta);
       BigDecimal total = guardarDetalles(ventaR,venta);
       ProcesarPago(ventaR,total,venta);
      return ventaRepository.save(ventaR);
    }

    public void actualizarVenta(Integer idVenta, EstadoVenta estado){
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada"));
        venta.setEstado(estado);
    }

    //refactoirzar despues
    private void validarVentaDto(VentaDto venta){
        if(venta == null){
            throw new BadRequestException("La venta no puede ser nula");
        }
        if(venta.getTipoVenta() == null){
            throw new BadRequestException("Debe especificar el tipo de venta");
        }

    }
    private void validarDetallesDto(List<DetalleVentaDTO> detalles){
        if(detalles == null || detalles.isEmpty() ){
            throw new BadRequestException("La venta debe incluir al menos un detalle");
        }
        for(int i=0; i<detalles.size();i++){
           DetalleVentaDTO detalle = detalles.get(i);
           if (detalle == null) {
               throw new BadRequestException("El detalle " + (i + 1) + " no puede ser nulo");
           }
           if( detalle.getCantidad() == null || detalle.getCantidad()<=0  ){
               throw new BadRequestException("La cantidad del detalle " + (i + 1) + " debe ser mayor a 0");
           }
           if (detalle.getPrecioUnitario() != null && detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
               throw new BadRequestException("El precio unitario del detalle " + (i + 1) + " debe ser mayor a 0");
           }
           if(detalle.getIdInventario() == null){
               throw new BadRequestException("El detalle " + (i + 1) + " debe especificar un inventario");
           }

        }
    }
    public void validarPagoDto(PagoDTO pago){       //se reutilizara en pagos
        BigDecimal zero = new BigDecimal(0);
        if(pago == null || pago.getMonto_abonado() == null ||  pago.getMonto_recibido() == null){
            throw new BadRequestException("El pago y sus montos son obligatorios");
        }
        if(pago.getMetodo() == null || pago.getMetodo().isBlank()){
            throw new BadRequestException("Debe elegir un método de pago");
        }

        String metodo = pago.getMetodo().trim().toUpperCase(Locale.ROOT);
        if (!METODOS_PAGO.contains(metodo)) {
            throw new BadRequestException("Método de pago no válido");
        }
        pago.setMetodo(metodo);

        if(pago.getMonto_abonado().compareTo(pago.getMonto_recibido()) > 0){
            throw new BadRequestException("El monto a abonar no puede ser mayor que el monto recibido");
        }
        if(pago.getMonto_recibido().compareTo(zero) <= 0){
            throw new BadRequestException("El monto recibido debe ser mayor a 0");
        }
        if(pago.getMonto_abonado().compareTo(zero) <= 0){
            throw new BadRequestException("El monto abonado debe ser mayor a 0");
        }

        if (!"EFECTIVO".equals(metodo)
                && pago.getMonto_abonado().compareTo(pago.getMonto_recibido()) != 0) {
            throw new BadRequestException(
                    "En tarjeta o transferencia el monto abonado debe ser igual al monto recibido"
            );
        }
    }
    public Venta BuscarById(Integer id){
        return ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada"));
    }

    public Venta VentaPendienteById(Integer id){
        return ventaRepository.findByEstadoNotInAndIdventa(ESTADOS_LIQUIDADOS,id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta pendiente no encontrada"));
    }

    public List<Venta> ventasPendientes(){
        return ventaRepository.findByEstadoNotIn(ESTADOS_LIQUIDADOS);
    }
















}
