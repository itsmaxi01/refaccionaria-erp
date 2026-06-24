package com.refaccionaria.sistemapos.pago;
import com.refaccionaria.sistemapos.venta.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Service
public class PagoService {
    private final PagoRepository pagoRepository;
    private final VentaService ventaService;

    private final DetalleVentaService detalleVentaService;


    public PagoService(PagoRepository pagoRepository, VentaService ventaService,
                       DetalleVentaService detalleVentaService){
        this.pagoRepository=pagoRepository;
        this.detalleVentaService = detalleVentaService;
        this.ventaService = ventaService;

    }

    @Transactional
    public Pago Registrar_Pago(PagoDTO pago,Integer idVenta) {
        ventaService.validarPagoDto(pago);
        Pago pagoR = new Pago();
        pagoR.setMetodo(pago.getMetodo());
        pagoR.setFecha(LocalDate.now());
        pagoR.setMonto(pago.getMonto());
        pagoR.setVenta(ventaService.VentaPendienteById(idVenta));// Busca si la venta es esta saldada
        pagoRepository.save(pagoR);
        List<Pago> pagos = pagoRepository.findByVenta_Idventa(idVenta);
        BigDecimal Total = new BigDecimal(0);
        BigDecimal TotalVenta = detalleVentaService.TotalByVenta(idVenta);
        for(int i=0;i<pagos.size();i++){
            Pago pagoSelect = pagos.get(i);
            Total = Total.add(pagoSelect.getMonto());
        }
        if(Total.compareTo(TotalVenta)>=0){
            ventaService.actualizarVenta(idVenta,"SALDADA");
            BigDecimal resultado =Total.subtract(TotalVenta);
            System.out.println("El resto es " + resultado);
        }
        else{
            BigDecimal resultado =TotalVenta.subtract(Total);
            System.out.println("El resto es " + resultado);

        }
        return pagoR;
    }



    public List<Pago> ListarPagos(){
        return pagoRepository.findAll();
    }








}
