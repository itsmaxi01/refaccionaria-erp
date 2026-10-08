package com.refaccionaria.sistemapos.venta;

import com.refaccionaria.sistemapos.cliente.ClienteService;
import com.refaccionaria.sistemapos.excepciones.BadRequestException;
import com.refaccionaria.sistemapos.excepciones.ConflictException;
import com.refaccionaria.sistemapos.inventario.Inventario;
import com.refaccionaria.sistemapos.inventario.InventarioService;
import com.refaccionaria.sistemapos.pago.Pago;
import com.refaccionaria.sistemapos.pago.PagoDTO;
import com.refaccionaria.sistemapos.pago.PagoRepository;
import com.refaccionaria.sistemapos.producto.Producto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;
    @Mock
    private InventarioService inventarioService;
    @Mock
    private DetalleVentaService detalleVentaService;
    @Mock
    private PagoRepository pagoRepository;
    @Mock
    private ClienteService clienteService;

    private VentaService ventaService;

    @BeforeEach
    void setUp() {
        ventaService = new VentaService(
                ventaRepository,
                inventarioService,
                clienteService,
                detalleVentaService,
                pagoRepository
        );
    }

    @Test
    void crearVentaPagadaDescuentaInventarioYConservaPrecio() {
        when(ventaRepository.save(any(Venta.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Producto producto = new Producto();
        producto.setPrecio(new BigDecimal("25.50"));
        Inventario inventario = mock(Inventario.class);
        when(inventario.getId_inventario()).thenReturn(7);
        when(inventario.getProducto()).thenReturn(producto);
        when(inventarioService.BuscarById(7)).thenReturn(inventario);

        Venta resultado = ventaService.crearVenta(ventaSinCliente(
                detalle(7, 2, null),
                pago("EFECTIVO", "60.00", "51.00")
        ));

        assertEquals(EstadoVenta.PAGADA, resultado.getEstado());
        verify(inventarioService).descontar(7, 2);

        ArgumentCaptor<DetalleVenta> detalleCaptor = ArgumentCaptor.forClass(DetalleVenta.class);
        verify(detalleVentaService).GuardarDetalle(detalleCaptor.capture());
        assertEquals(0, new BigDecimal("25.50").compareTo(detalleCaptor.getValue().getPrecioUnitario()));
        assertEquals(0, new BigDecimal("51.00").compareTo(detalleCaptor.getValue().getSubtotal()));

        ArgumentCaptor<Pago> pagoCaptor = ArgumentCaptor.forClass(Pago.class);
        verify(pagoRepository).save(pagoCaptor.capture());
        assertEquals("EFECTIVO", pagoCaptor.getValue().getMetodo());
    }

    @Test
    void ventaParcialSinClienteEsRechazada() {
        when(ventaRepository.save(any(Venta.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(inventarioService.BuscarById(1)).thenReturn(inventarioConPrecio("100.00", 3));

        BadRequestException error = assertThrows(BadRequestException.class, () ->
                ventaService.crearVenta(ventaSinCliente(
                        detalle(1, 1, null),
                        pago("EFECTIVO", "50.00", "50.00")
                ))
        );

        assertTrue(error.getMessage().contains("cliente"));
        verify(pagoRepository, never()).save(any(Pago.class));
    }

    @Test
    void pagoMayorAlTotalEsRechazado() {
        when(ventaRepository.save(any(Venta.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(inventarioService.BuscarById(1)).thenReturn(inventarioConPrecio("40.00", 5));

        assertThrows(ConflictException.class, () ->
                ventaService.crearVenta(ventaSinCliente(
                        detalle(1, 1, null),
                        pago("EFECTIVO", "100.00", "50.00")
                ))
        );
    }

    @Test
    void metodoDePagoInvalidoEsRechazadoAntesDePersistir() {
        assertThrows(BadRequestException.class, () ->
                ventaService.crearVenta(ventaSinCliente(
                        detalle(1, 1, new BigDecimal("10.00")),
                        pago("CRIPTOMONEDA", "10.00", "10.00")
                ))
        );

        verify(ventaRepository, never()).save(any(Venta.class));
    }

    private VentaDto ventaSinCliente(DetalleVentaDTO detalle, PagoDTO pago) {
        VentaDto venta = new VentaDto();
        venta.setTipoVenta("CONTADO");
        venta.setDetalles(List.of(detalle));
        venta.setPago(pago);
        return venta;
    }

    private DetalleVentaDTO detalle(int inventarioId, int cantidad, BigDecimal precio) {
        DetalleVentaDTO detalle = new DetalleVentaDTO();
        detalle.setIdInventario(inventarioId);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precio);
        return detalle;
    }

    private PagoDTO pago(String metodo, String recibido, String abonado) {
        PagoDTO pago = new PagoDTO();
        pago.setMetodo(metodo);
        pago.setMonto_recibido(new BigDecimal(recibido));
        pago.setMonto_abonado(new BigDecimal(abonado));
        return pago;
    }

    private Inventario inventarioConPrecio(String precio, int cantidad) {
        Producto producto = new Producto();
        producto.setPrecio(new BigDecimal(precio));
        Inventario inventario = new Inventario();
        inventario.setProducto(producto);
        inventario.setCantidad(cantidad);
        return inventario;
    }
}
