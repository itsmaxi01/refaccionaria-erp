/*package com.refaccionaria.sistemapos.venta;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDate;


@Service
public class PagoService {
    private final PagoRepository pagoRepository;
    private final VentaService ventaService;
    public PagoService(PagoRepository pagoRepository, VentaService ventaService ){
        this.pagoRepository=pagoRepository;
        this.ventaService = ventaService;
    }


    public Pago Registrar_Pago(PagoDTO pago,Integer idVenta) {
        ventaService.validarPagoDto(pago);
        Pago pagoR = new Pago();
        pagoR.setMetodo(pago.getMetodo());
        pagoR.setFecha(LocalDate.now());
        pagoR.setMonto(pago.getMonto());
        pagoR.setVenta(ventaService.VentaPendienteById(idVenta));
        return pagoRepository.save(pagoR);
    }








}
*/