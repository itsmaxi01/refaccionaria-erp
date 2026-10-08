package com.refaccionaria.sistemapos.pago;

import com.refaccionaria.sistemapos.excepciones.ConflictException;
import com.refaccionaria.sistemapos.venta.DetalleVentaService;
import com.refaccionaria.sistemapos.venta.EstadoVenta;
import com.refaccionaria.sistemapos.venta.Venta;
import com.refaccionaria.sistemapos.venta.VentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;
    @Mock
    private VentaService ventaService;
    @Mock
    private DetalleVentaService detalleVentaService;

    private PagoService pagoService;

    @BeforeEach
    void setUp() {
        pagoService = new PagoService(pagoRepository, ventaService, detalleVentaService);
    }

    @Test
    void pagoQueCompletaSaldoMarcaVentaComoSaldada() {
        Venta venta = new Venta();
        when(ventaService.VentaPendienteById(5)).thenReturn(venta);
        when(pagoRepository.findByVenta_Idventa(5)).thenReturn(List.of(pagoRegistrado("30.00")));
        when(detalleVentaService.TotalByVenta(5)).thenReturn(new BigDecimal("100.00"));

        pagoService.Registrar_Pago(pagoDto("70.00"), 5);

        verify(ventaService).actualizarVenta(5, EstadoVenta.SALDADA);
        verify(pagoRepository).save(any(Pago.class));
    }

    @Test
    void pagoParcialMantieneEstadoParcial() {
        when(ventaService.VentaPendienteById(5)).thenReturn(new Venta());
        when(pagoRepository.findByVenta_Idventa(5)).thenReturn(List.of());
        when(detalleVentaService.TotalByVenta(5)).thenReturn(new BigDecimal("100.00"));

        pagoService.Registrar_Pago(pagoDto("25.00"), 5);

        verify(ventaService).actualizarVenta(5, EstadoVenta.PARCIAL);
        verify(pagoRepository).save(any(Pago.class));
    }

    @Test
    void pagoQueExcedeSaldoNoSeGuarda() {
        when(ventaService.VentaPendienteById(5)).thenReturn(new Venta());
        when(pagoRepository.findByVenta_Idventa(5)).thenReturn(List.of(pagoRegistrado("80.00")));
        when(detalleVentaService.TotalByVenta(5)).thenReturn(new BigDecimal("100.00"));

        assertThrows(ConflictException.class, () -> pagoService.Registrar_Pago(pagoDto("30.00"), 5));

        verify(pagoRepository, never()).save(any(Pago.class));
        verify(ventaService, never()).actualizarVenta(any(), any());
    }

    private PagoDTO pagoDto(String monto) {
        PagoDTO pago = new PagoDTO();
        pago.setMetodo("TRANSFERENCIA");
        pago.setMonto_abonado(new BigDecimal(monto));
        pago.setMonto_recibido(new BigDecimal(monto));
        return pago;
    }

    private Pago pagoRegistrado(String monto) {
        Pago pago = new Pago();
        pago.setMonto_abonado(new BigDecimal(monto));
        return pago;
    }
}
