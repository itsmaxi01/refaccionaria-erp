package com.refaccionaria.sistemapos.pago;
import com.refaccionaria.sistemapos.excepciones.ConflictException;
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
        pagoR.setMonto_recibido(pago.getMonto_recibido());
        pagoR.setMonto_abonado(pago.getMonto_abonado());
        pagoR.setVenta(ventaService.VentaPendienteById(idVenta));// Busca si la venta es esta saldada
        List<Pago> pagos = pagoRepository.findByVenta_Idventa(idVenta); //regresa todos los pagos de una venta
        BigDecimal Total = new BigDecimal(0);
        BigDecimal TotalVenta = detalleVentaService.TotalByVenta(idVenta);
        for(int i=0;i<pagos.size();i++){
            Pago pagoSelect = pagos.get(i);
            Total = Total.add(pagoSelect.getMonto_abonado()); //remplazar despues por total pagado
        }
        Total = Total.add(pago.getMonto_abonado());
        if(Total.compareTo(TotalVenta)>0){
            BigDecimal resto = new BigDecimal(0);
            resto = Total.subtract(pago.getMonto_abonado());
            resto = TotalVenta.subtract(resto);
            throw new ConflictException("El pago excede el saldo pendiente de " + resto);
        }
        else if(Total.compareTo(TotalVenta) == 0){
            ventaService.actualizarVenta(idVenta, EstadoVenta.SALDADA);
        }
        else{
            ventaService.actualizarVenta(idVenta, EstadoVenta.PARCIAL);
        }
        pagoRepository.save(pagoR);
        return pagoR;
    }



    public List<Pago> ListarPagos(){
        return pagoRepository.findAll();
    }

    public BigDecimal totalPagadoById(Integer id){

        List<Pago> pagos = pagoRepository.findByVenta_Idventa(id);
        BigDecimal totalPagadoById = new BigDecimal(0);
        for(int i=0; i<pagos.size();i++){
            Pago pagoActual = pagos.get(i);
            totalPagadoById = totalPagadoById.add(pagoActual.getMonto_abonado());
        }
        return totalPagadoById;
    }








}
